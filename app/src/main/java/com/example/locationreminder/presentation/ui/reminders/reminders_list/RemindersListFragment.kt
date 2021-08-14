package com.example.locationreminder.presentation.ui.reminders.reminders_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.locationreminder.R
import com.example.locationreminder.databinding.FragmentRemindersListBinding
import com.example.locationreminder.domain.Reminder
import com.example.locationreminder.presentation.adapters.ReminderAdapter
import com.example.locationreminder.presentation.adapters.ReminderListener
import com.example.locationreminder.presentation.ui.reminders.RemindersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemindersListFragment : androidx.fragment.app.Fragment() {

    private val remindersViewModel: RemindersViewModel by activityViewModels()

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
        binding.reminderViewModel = remindersViewModel
        binding.rvReminders.adapter = ReminderAdapter(ReminderListener { currentReminder ->
            remindersViewModel.onNavigateToEditScreen(currentReminder)
        })

        binding.fabRemindersAdd.setOnClickListener {
            remindersViewModel.onNavigateToEditScreen(Reminder(remindersViewModel.idCount))
            remindersViewModel.idCount += 1
        }

        remindersViewModel.navigateToEditScreen.observe(viewLifecycleOwner) { isNavigating ->
            if (isNavigating) {
                findNavController().navigate(RemindersListFragmentDirections.actionRemindersListFragmentToEditReminderFragment())
                remindersViewModel.onNavigateToEditScreenFinished()
            }
        }

        remindersViewModel.remindersList.observe(viewLifecycleOwner) { list ->
            list?.let {
                if (list.size > 0) {
                    binding.rvReminders.visibility = View.VISIBLE
                    binding.ivRemindersNoData.visibility = View.GONE
                    binding.tvRemindersNoData.visibility = View.GONE
                } else {
                    binding.rvReminders.visibility = View.GONE
                    binding.ivRemindersNoData.visibility = View.VISIBLE
                    binding.tvRemindersNoData.visibility = View.VISIBLE
                }
            }
        }
        return binding.root
    }
}