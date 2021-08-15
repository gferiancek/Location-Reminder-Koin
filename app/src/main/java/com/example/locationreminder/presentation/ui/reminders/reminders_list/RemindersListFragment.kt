package com.example.locationreminder.presentation.ui.reminders.reminders_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.locationreminder.R
import com.example.locationreminder.databinding.FragmentRemindersListBinding
import com.example.locationreminder.domain.Reminder
import com.example.locationreminder.presentation.adapters.ReminderAdapter
import com.example.locationreminder.presentation.adapters.ReminderListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemindersListFragment : androidx.fragment.app.Fragment() {

    private val remindersViewModel: RemindersListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentRemindersListBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_reminders_list,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = remindersViewModel
        binding.rvReminders.adapter = ReminderAdapter(ReminderListener { currentReminder ->
            remindersViewModel.onNavigateToEditScreen(currentReminder)
        })

        binding.fabRemindersAdd.setOnClickListener {
            remindersViewModel.onNavigateToEditScreen(Reminder())
        }

        remindersViewModel.navigateToEditScreen.observe(viewLifecycleOwner) { currentReminder ->
            currentReminder?.let {
                findNavController().navigate(
                    RemindersListFragmentDirections.actionRemindersListFragmentToEditReminderFragment(
                        currentReminder
                    )
                )
                remindersViewModel.onNavigateToEditScreenFinished()
            }
        }
        return binding.root
    }
}