package com.udacity.project4.presentation.ui.reminders.reminders_list

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.R
import com.udacity.project4.databinding.FragmentRemindersListBinding
import com.udacity.project4.domain.model.Reminder
import com.udacity.project4.domain.utils.NotificationUtils
import com.udacity.project4.presentation.adapters.ReminderAdapter
import com.udacity.project4.presentation.adapters.ReminderListener
import org.koin.android.ext.android.inject

class RemindersListFragment : androidx.fragment.app.Fragment() {

    private val remindersViewModel: RemindersListViewModel by inject()

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
                if (currentReminder.hasBlankTextFields()) {
                    findNavController().navigate(
                        RemindersListFragmentDirections.actionRemindersListFragmentToEditReminderFragment(
                            getString(R.string.label_add_reminder)
                        )
                    )
                } else {
                    val action =
                        RemindersListFragmentDirections.actionRemindersListFragmentToEditReminderFragment(
                            getString(R.string.label_edit_reminder)
                        )
                    action.currentReminder = currentReminder.copy()
                    findNavController().navigate(action)
                }
                remindersViewModel.onNavigateToEditScreenFinished()
            }
        }
        setHasOptionsMenu(true)
        NotificationUtils().createChannel(requireContext())
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