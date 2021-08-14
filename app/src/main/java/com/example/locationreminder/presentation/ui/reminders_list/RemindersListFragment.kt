package com.example.locationreminder.presentation.ui.reminders_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.locationreminder.R

class RemindersListFragment : Fragment() {

    companion object {
        fun newInstance() = RemindersListFragment()
    }

    private lateinit var viewModel: RemindersListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reminders_list_fragment, container, false)
    }
}