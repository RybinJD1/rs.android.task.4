package com.example.android.carmarket.ui.list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.android.carmarket.databinding.ListItemBinding
import com.example.android.carmarket.model.Car

class ListViewHolder(
    private val binding: ListItemBinding,
    private val listener: ListListener,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(car: Car?) {
        binding.apply {
            brand.text = car?.brand
            info.text = car?.info
            category.text = car?.category?.replaceFirstChar { char -> char.uppercase() }
            price.text = "${(car?.price)?.format(0)}"
            km.text = "${(car?.km)?.format(0)}"
        }
        initButtonsListeners(car)
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)

    private fun initButtonsListeners(car: Car?) {
        itemView.setOnLongClickListener(View.OnLongClickListener {
            car?.let { it1 -> listener.onNodeLongClick(it1.id) }
            true
        })
    }
}