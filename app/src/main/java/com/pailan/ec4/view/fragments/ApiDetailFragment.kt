package com.pailan.ec4.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pailan.ec4.R
import com.pailan.ec4.databinding.FragmentApiDetailBinding
import com.pailan.ec4.model.Api


class ApiDetailFragment : Fragment() {
    private lateinit var binding: FragmentApiDetailBinding
    val args: ApiDetailFragmentArgs by navArgs()
    private lateinit var api: Api
    private lateinit var viewModel: ApiDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api= args.api
        viewModel= ViewModelProvider(requireActivity()).get(ApiDetailViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentApiDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(binding.root.context)
            .load(api.displayIcon) // Carga la imagen desde la URL proporcionada por la API
            .into(binding.imgStar)
        binding.txtNameStore.text = api.displayName
        binding.txtDetail.text = api.description
        binding.txtExpiredOn.text = api.developerName
        binding.txtStar.text = api.developerName
        binding.btnAddFavorite.apply {
            text = if (viewModel.isFavorite(api)) "Eliminar" else "Agregar favoritos"
        }
        binding.btnAddFavorite.setOnClickListener {
            if (!viewModel.isFavorite(api)) {
                api.isFavorite = true
                viewModel.addFavorite(api)
                binding.btnAddFavorite.text = "Eliminar"
            } else {
                api.isFavorite = false
                viewModel.removeFavorite(api)
                binding.btnAddFavorite.text = "Agregar a favoritos"
            }
        }
    }


}