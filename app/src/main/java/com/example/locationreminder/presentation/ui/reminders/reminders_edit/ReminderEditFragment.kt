package com.example.locationreminder.presentation.ui.reminders.reminders_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.locationreminder.R
import com.example.locationreminder.databinding.FragmentReminderEditBinding
import com.example.locationreminder.presentation.ui.reminders.reminders_edit.ReminderEditEvent.AddNewReminderEvent
import com.example.locationreminder.presentation.ui.reminders.reminders_edit.ReminderEditEvent.EditCurrentReminderEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderEditFragment : Fragment() {

    private val viewModel: ReminderEditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentReminderEditBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_reminder_edit,
            container,
            false
        )
        viewModel.currentReminder = requireArguments().getParcelable("currentReminder")!!
        binding.lifecycleOwner = viewLifecycleOwner
        binding.currentReminder = viewModel.currentReminder

        binding.fabSave.setOnClickListener {
            viewModel.currentReminder.id.let { id ->
                when (id) {
                    0L -> viewModel.onTriggerEvent(AddNewReminderEvent)
                    else -> viewModel.onTriggerEvent(EditCurrentReminderEvent)
                }
            }
        }
        viewModel.snackbarMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarMessageDisplayed()
            }
        }
        viewModel.eventSuccess.observe(viewLifecycleOwner) { isSuccessful ->
            if (isSuccessful) {
                findNavController().popBackStack()
                viewModel.onEventSuccessHandled()
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}