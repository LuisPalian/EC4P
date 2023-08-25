package com.pailan.ec4.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pailan.ec4.view.fragments.ApiFavoriteViewModel
import com.pailan.ec4.R
import com.pailan.ec4.RVApiListAdapter
import com.pailan.ec4.databinding.FragmentApiFavoriteBinding
import com.pailan.ec4.model.Api


class ApiFavoriteFragment : Fragment() {
    private lateinit var binding: FragmentApiFavoriteBinding
    private lateinit var viewModel: ApiFavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ApiFavoriteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApiFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RVApiListAdapter(listOf()){michi->
            val apiDetailDirection = ApiFavoriteFragmentDirections.actionApiFavoriteFragmentToApiDetailFragment(michi)
            findNavController().navigate(apiDetailDirection)
        }
        binding.rvApi.adapter = adapter
        binding.rvApi.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        viewModel.favorites.observe(requireActivity()){
            if (it != null){
                adapter.results = it
                adapter.notifyDataSetChanged()}
        }
        viewModel.refreshFavorites()
    }




}