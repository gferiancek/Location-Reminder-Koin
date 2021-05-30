package com.example.neoradar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.neoradar.R
import com.example.neoradar.adapters.NeoAdapter
import com.example.neoradar.adapters.NeoListener
import com.example.neoradar.databinding.FragmentNeoListBinding
import com.example.neoradar.viewmodel.NeoListViewModel


class NeoListFragment : Fragment() {

    private val viewModel: NeoListViewModel by lazy {
        ViewModelProvider(this).get(NeoListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentNeoListBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_neo_list,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.neoListViewModel = viewModel
        binding.neoListRv.adapter = NeoAdapter(NeoListener { currentNeo ->
            viewModel.navigateToDetail(currentNeo)
        })

        viewModel.eventNavigateToDetail.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    NeoListFragmentDirections
                        .actionNeoListFragmentToNeoDetailFragment(it, it.name)
                )
                viewModel.onNavigateToDetailFinished()
            }
        }

        /**
         * When the viewModel gets an update to its neoList LiveData, we set the uiState based on
         * the state of that list as well as the user's internet connection.
         */
        viewModel.neoList.observe(viewLifecycleOwner) {
            viewModel.setUiState()
        }
        return binding.root
    }
}