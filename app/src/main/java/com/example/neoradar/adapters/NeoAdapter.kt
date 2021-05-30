package com.example.neoradar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoradar.databinding.ListItemNeoBinding
import com.example.neoradar.domain.Neo

/**
 * DiffUtils isn't really necessary here, since we're always deleting/replacing the data and never
 * adding items to the top.  I did want to practice the pattern though, since it was knew knowledge
 * that I had never applied to a RecyclerView before.  Figured it won't really hurt the app to include it.
 */
class NeoAdapter(private val clickListener: NeoListener) :
    ListAdapter<Neo, NeoAdapter.NeoViewHolder>(NeoDiffCallback()) {

    class NeoViewHolder private constructor(private val binding: ListItemNeoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Neo, clickListener: NeoListener) {
            binding.currentNeo = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): NeoViewHolder {
                val binding = ListItemNeoBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return NeoViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NeoViewHolder {
        return NeoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NeoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

class NeoDiffCallback : DiffUtil.ItemCallback<Neo>() {
    override fun areItemsTheSame(oldItem: Neo, newItem: Neo): Boolean {
        return oldItem.closeApproachDate == newItem.closeApproachDate
    }

    override fun areContentsTheSame(oldItem: Neo, newItem: Neo): Boolean {
        return oldItem == newItem
    }
}

class NeoListener(val clickListener: (neo: Neo) -> Unit) {
    fun onClick(neo: Neo) = clickListener(neo)
}