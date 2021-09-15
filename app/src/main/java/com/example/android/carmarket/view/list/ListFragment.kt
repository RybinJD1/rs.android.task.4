package com.example.android.carmarket.view.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.android.carmarket.R
import com.example.android.carmarket.database.CURSOR
import com.example.android.carmarket.database.room.CarDB
import com.example.android.carmarket.database.CarRepository
import com.example.android.carmarket.database.ROOM
import com.example.android.carmarket.databinding.ListFragmentBinding
import com.example.android.carmarket.model.Car
import com.example.android.carmarket.view.list.adapter.ListAdapter
import com.example.android.carmarket.view.list.adapter.ListListener
import com.example.android.carmarket.view.list.adapter.SwipeCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ListFragment : Fragment(), ListListener {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { CarDB.getInstance(requireActivity(), applicationScope) }

    private var _binding: ListFragmentBinding? = null
    private val binding get() = _binding!!

    private var viewModel: ListViewModel? = null
    private val listAdapter = ListAdapter(this)

    private lateinit var management: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListFragmentBinding.inflate(inflater, container, false)
        val application = requireNotNull(this).activity?.application
        val repository = application?.let { CarRepository(database.carDAO(), it) }
        val viewModelFactory =
            application?.let { repository?.let { repository -> ListFactory(repository) } }
        viewModel =
            viewModelFactory?.let { ViewModelProvider(this, it) }?.get(ListViewModel::class.java)

        val preferences = PreferenceManager.getDefaultSharedPreferences(application)
        management = preferences.getString("list_management", ROOM).toString().trim()


        if (management == ROOM) {
            lifecycle.coroutineScope.launch {
                viewModel?.cars?.collect {
                    Log.i("MyLog", "cars?.collect ")
                    listAdapter.submitList(it)
                }
            }
        } else {
            viewModel?.updateCursorCars()
            lifecycle.coroutineScope.launch {
                viewModel?.cursorCars?.collect {
                    Log.i("MyLog", "cursorCars?.collect ")
                    listAdapter.submitList(it)
                }
                viewModel?.updateCursorCars()

            }
        }

        binding.apply {
            recyclerView.adapter = listAdapter
            recyclerView.layoutManager = GridLayoutManager(requireActivity(), 1)

            ItemTouchHelper(object : SwipeCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    super.onSwiped(viewHolder, direction)
                    lifecycle.coroutineScope.launch {
                        val car = viewModel?.cars?.first()?.get(viewHolder.adapterPosition)
                        car?.let { deleteItem(it) }
                    }
                }
            }).attachToRecyclerView(recyclerView)

            floatingActionButton.setOnClickListener {
                view?.findNavController()
                    ?.navigate(ListFragmentDirections.actionListFragmentToAddFragment(null))
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.filter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteItem(car: Car) {
        viewModel?.deleteCar(car)
        if (management == CURSOR) {
            viewModel?.updateCursorCars()
            lifecycle.coroutineScope.launch {
                viewModel?.cursorCars?.collect {
                    listAdapter.submitList(it.toList())
                    Log.i("MyLog", "cars?.collect.delete ")
                }
            }
        }
    }

    override fun onNodeLongClick(id: Int) {
        viewModel?.getCar(id)
        lifecycle.coroutineScope.launch {
            view?.findNavController()
                ?.navigate(
                    ListFragmentDirections
                        .actionListFragmentToAddFragment(viewModel?.car?.first())
                )
        }
    }

}

