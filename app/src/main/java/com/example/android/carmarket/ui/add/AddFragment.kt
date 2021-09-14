package com.example.android.carmarket.ui.add

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import com.example.android.carmarket.R
import com.example.android.carmarket.categories
import com.example.android.carmarket.database.room.CarDB
import com.example.android.carmarket.database.CarRepository
import com.example.android.carmarket.databinding.AddFragmentBinding
import com.example.android.carmarket.model.Car
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AddFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { CarDB.getInstance(requireActivity(), applicationScope) }
    private var _binding: AddFragmentBinding? = null
    private val binding get() = _binding!!
    private var viewModel: AddViewModel? = null

    private val car by lazy { AddFragmentArgs.fromBundle(requireArguments()).car }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddFragmentBinding.inflate(inflater, container, false)

        val application = requireNotNull(this).activity?.application
        val repository = application?.let { CarRepository(database.carDAO(), it) }
        val viewModelFactory = application?.let { repository?.let { it1 -> AddFactory(it1) } }
        viewModel =
            viewModelFactory?.let { ViewModelProvider(this, it) }?.get(AddViewModel::class.java)
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories())
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.categorySpinner.adapter = spinnerAdapter
        binding.categorySpinner.onItemSelectedListener = this

        binding.apply {
            if (car != null) {
                brandEdit.hint = car?.brand.toString()
                infoEdit.hint = car?.info.toString()
                kmEdit.hint = car?.km.toString()
                priceEdit.hint = car?.price.toString()
                categorySpinner.setSelection(categories().indexOf(categories().find { it == car?.category }))
            }

            brandEdit.addTextChangedListener {
                if (car == null) viewModel?.brand = it.toString()
                else car?.brand = it.toString()
            }
            infoEdit.addTextChangedListener {
                if (car == null) viewModel?.info = it.toString()
                else car?.info = it.toString()
            }
            kmEdit.addTextChangedListener {
                if (car == null) viewModel?.km = it.toString().toDouble()
                else car?.km = it.toString().toDouble()
            }
            priceEdit.addTextChangedListener {
                if (car == null) viewModel?.price = it.toString().toDouble()
                else car?.price = it.toString().toDouble()
            }

            btnSave.setOnClickListener {
                if (car == null) {
                    viewModel?.addCar(
                        Car(
                            viewModel?.brand, viewModel?.info, viewModel?.category,
                            viewModel?.km, viewModel?.price
                        )
                    )
                } else {
                    viewModel?.updateCar(car!!)
                }

                view?.findNavController()?.navigate(R.id.action_addFragment_to_listFragment)
            }
        }
        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val category = parent.getItemAtPosition(pos).toString()
        if (car == null) viewModel?.category = category
        else car?.category = category
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}