package com.example.android.carmarket.view.list.adapter

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
            price.text = "${(car?.price)}"
            km.text = "${(car?.km)}"
        }
        initButtonsListeners(car)
    }

    private fun initButtonsListeners(car: Car?) {
        itemView.setOnLongClickListener {
            car?.let { it1 -> listener.onNodeLongClick(it1.id) }
            true
        }
    }
}