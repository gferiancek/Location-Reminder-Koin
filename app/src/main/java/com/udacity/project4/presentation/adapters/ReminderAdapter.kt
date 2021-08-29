package com.udacity.project4.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.project4.databinding.ListItemReminderBinding
import com.udacity.project4.domain.model.Reminder

class ReminderAdapter(private val clickListener: ReminderListener) :
    ListAdapter<Reminder, ReminderAdapter.ReminderViewHolder>(
        ReminderDiffCallback()
    ) {

    class ReminderViewHolder private constructor(private val binding: ListItemReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Reminder, clickListener: ReminderListener) {
            binding.currentReminder = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ReminderViewHolder {
                return ReminderViewHolder(
                    ListItemReminderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        return ReminderViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(
            getItem(position),
            clickListener
        )
    }
}

class ReminderDiffCallback : DiffUtil.ItemCallback<Reminder>() {
    override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return oldItem == newItem
    }
}

class ReminderListener(val clickListener: (currentReminder: Reminder) -> Unit) {
    fun onClick(currentReminder: Reminder) = clickListener(currentReminder)
}