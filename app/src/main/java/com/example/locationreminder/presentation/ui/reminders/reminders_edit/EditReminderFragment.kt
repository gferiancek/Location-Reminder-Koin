package com.example.locationreminder.presentation.ui.reminders.reminders_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.locationreminder.R
import com.example.locationreminder.databinding.FragmentEditReminderBinding
import com.example.locationreminder.presentation.ui.reminders.RemindersViewModel

class EditReminderFragment : Fragment() {

    private val remindersViewModel: RemindersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentEditReminderBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_edit_reminder,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.currentReminder = remindersViewModel.currentReminder

        binding.fabSave.setOnClickListener {
            remindersViewModel.addReminder()
            findNavController().popBackStack()
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}