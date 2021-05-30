package com.example.neoradar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
            false
        )

        val arguments = NeoDetailFragmentArgs.fromBundle(requireArguments())
        binding.currentNeo = arguments.currentNeo
        binding.detailMagnitudeHelp.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setMessage(getString(R.string.au_description))
            alertDialogBuilder.show()
        }
        return binding.root
    }
}