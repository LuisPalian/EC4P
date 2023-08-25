package com.pailan.ec4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pailan.ec4.databinding.ItemApiBinding
import com.pailan.ec4.model.Api

class RVApiListAdapter (var results: List<Api>,val onClick: (Api)->Unit): RecyclerView.Adapter<ApiVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiVH {
        val binding = ItemApiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApiVH(binding, onClick)
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: ApiVH, position: Int) {
        holder.bind(results[position])
    }


}

class ApiVH(private val binding: ItemApiBinding,val onClick: (Api)->Unit): RecyclerView.ViewHolder(binding.root) {
    fun bind(api: Api) {
        Glide.with(binding.root.context)
            .load(api.displayIcon) // Carga la imagen desde la URL proporcionada por la API
            .into(binding.imageView)
        binding.txtNameStore.text = api.displayName
        binding.txtDetail.text = api.developerName
        binding.txtExpiredOn.text = api.description
        binding.root.setOnClickListener {
            onClick(api)
        }
    }
}