package com.pailan.ec4.data.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pailan.ec4.databinding.ItemFirebaseBinding

class DataAdapter (private val dataList: List<DataItems>) :
    RecyclerView.Adapter<DataAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemFirebaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding.root)
    }


    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val dataItem = dataList[position]
        holder.bind(dataItem)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemFirebaseBinding.bind(itemView.rootView)

        fun bind(dataItem: DataItems) {
            binding.txtname.text = dataItem.name
            binding.txtgender.text = dataItem.gender
            binding.txtheight.text = dataItem.height
            binding.txtmass.text = dataItem.mass
            Glide.with(itemView)
                .load(Uri.parse(dataItem.imagen))
                .apply(RequestOptions.centerCropTransform())
                .into(binding.imageView)
        }
    }
}