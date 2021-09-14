package com.example.android.carmarket.ui.list.adapter

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.example.android.carmarket.model.Car

class ListDiffCallback :
    DiffUtil.ItemCallback<Car>() {

    override fun areItemsTheSame(item: Car, newItem: Car): Boolean {
        return item.id == newItem.id
    }

    override fun areContentsTheSame(item: Car, newItem: Car): Boolean {
        return item == newItem
    }

    @Nullable
    @Override
    override fun getChangePayload(item: Car, newItem: Car): Any? {
        return super.getChangePayload(item, newItem)
    }
}