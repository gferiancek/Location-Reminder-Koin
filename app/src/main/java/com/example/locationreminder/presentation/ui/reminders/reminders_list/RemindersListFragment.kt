package com.example.locationreminder.presentation.ui.reminders.reminders_list

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.locationreminder.R
import com.example.locationreminder.databinding.FragmentRemindersListBinding
import com.example.locationreminder.presentation.adapters.ReminderAdapter
import com.example.locationreminder.presentation.adapters.ReminderListener
import com.firebase.ui.auth.AuthUI
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
            remindersViewModel.onNavigateToEditScreen(currentReminder.id)
        })

        binding.fabRemindersAdd.setOnClickListener {
            remindersViewModel.onNavigateToEditScreen(0L)
        }

        remindersViewModel.navigateToEditScreen.observe(viewLifecycleOwner) { reminderId ->
            reminderId?.let {
                val label =
                    if (reminderId == 0L) getString(R.string.label_add_reminder) else getString(
                        R.string.label_edit_reminder
                    )
                findNavController().navigate(
                    RemindersListFragmentDirections.actionRemindersListFragmentToEditReminderFragment(
                        // We use Two-Way DataBinding in the Edit Screen, so we send a copy of the reminder.
                        // If we don't, then any changes get written all the way back to the database, even
                        // if the user clicks the back button to cancel editing the reminder.
                        reminderId,
                        label
                    )
                )
                remindersViewModel.onNavigateToEditScreenFinished()
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_reminder_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener {
                        findNavController().navigate(RemindersListFragmentDirections.actionRemindersListFragmentToLoginFragment())
                    }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}