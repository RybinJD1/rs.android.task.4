package com.example.android.carmarket.view.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.android.carmarket.databinding.ListItemBinding
import com.example.android.carmarket.model.Car

class ListAdapter(
    private val listener: ListListener
) : ListAdapter<Car, ListViewHolder>(ListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return ListViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition))
    }
}


