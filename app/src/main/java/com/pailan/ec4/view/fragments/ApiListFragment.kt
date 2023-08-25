package com.pailan.ec4.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pailan.ec4.R
import com.pailan.ec4.RVApiListAdapter
import com.pailan.ec4.databinding.FragmentApiListBinding


class ApiListFragment : Fragment() {
    private lateinit var binding: FragmentApiListBinding
    private lateinit var viewModel: ApiListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ApiListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentApiListBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RVApiListAdapter(listOf()){api ->
            //navegar al detalle
            val apiDetailDirection = ApiListFragmentDirections.actionApiListFragmentToApiDetailFragment(api)
            findNavController().navigate(apiDetailDirection)
        }
        binding.rvApi.adapter = adapter
        binding.rvApi.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        viewModel.cuponList.observe(requireActivity()){
            if (it != null){
                adapter.results = it
                adapter.notifyDataSetChanged()}
        }
        viewModel.getCuponsFromService()
    }

}