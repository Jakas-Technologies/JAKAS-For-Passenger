package com.miftah.jakasforpassenger.ui.maps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miftah.jakasforpassenger.databinding.ItemDepartmentAngkotBinding
import com.miftah.jakasforpassenger.utils.Angkot

class AngkotDepartmentAdapter(val onClick: (Angkot) -> Unit) :
    ListAdapter<Angkot, AngkotDepartmentAdapter.ViewHolder>(DIF_CALLBACK) {

    inner class ViewHolder(val binding: ItemDepartmentAngkotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(angkot : Angkot) {

        }
    }

    companion object {
        val DIF_CALLBACK = object : DiffUtil.ItemCallback<Angkot>() {
            override fun areItemsTheSame(oldItem: Angkot, newItem: Angkot): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Angkot, newItem: Angkot): Boolean {
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