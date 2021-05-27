package com.example.neoradar.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.neoradar.R
import com.example.neoradar.databinding.FragmentNeoListBinding

class NeoListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentNeoListBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_neo_list,
            container,
            false)

        // Inflate the layout for this fragment
        return binding.root
    }
}