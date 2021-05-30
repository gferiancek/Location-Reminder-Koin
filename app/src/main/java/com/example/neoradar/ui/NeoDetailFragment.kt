package com.example.neoradar.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.neoradar.R
import com.example.neoradar.databinding.FragmentNeoDetailBinding

class NeoDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentNeoDetailBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_neo_detail,
            container,
            false)

        val arguments = NeoDetailFragmentArgs.fromBundle(requireArguments())
        binding.currentNeo = arguments.currentNeo
        // Inflate the layout for this fragment
        return binding.root
    }
}