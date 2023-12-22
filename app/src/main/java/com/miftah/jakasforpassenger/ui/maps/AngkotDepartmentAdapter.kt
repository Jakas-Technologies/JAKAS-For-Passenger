package com.miftah.jakasforpassenger.ui.maps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.DriversItem
import com.miftah.jakasforpassenger.databinding.ItemDepartmentAngkotBinding

class AngkotDepartmentAdapter(val onClick: (DriversItem) -> Unit) :
    ListAdapter<DriversItem, AngkotDepartmentAdapter.ViewHolder>(DIF_CALLBACK) {

    inner class ViewHolder(val binding: ItemDepartmentAngkotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(angkot : DriversItem) {
            binding.keyAngkotPlat.text = angkot.licensePlate
        }
    }

    companion object {
        val DIF_CALLBACK = object : DiffUtil.ItemCallback<DriversItem>() {
            override fun areItemsTheSame(oldItem: DriversItem, newItem: DriversItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DriversItem, newItem: DriversItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDepartmentAngkotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val angkot = getItem(position)
        holder.bind(angkot)
        holder.itemView.setOnClickListener {
            onClick(angkot)
        }
    }
}