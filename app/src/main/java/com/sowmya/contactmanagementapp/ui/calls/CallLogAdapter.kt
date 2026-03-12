package com.sowmya.contactmanagementapp.ui.calls

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sowmya.contactmanagementapp.R
import com.sowmya.contactmanagementapp.data.model.CallLog
import com.sowmya.contactmanagementapp.databinding.ItemCallLogBinding
import java.text.SimpleDateFormat
import java.util.*

class CallLogAdapter(
    private val onDeleteClick: (CallLog) -> Unit,
    private val onSelectionChanged: (CallLog, Boolean) -> Unit
) : ListAdapter<CallLog, CallLogAdapter.CallLogViewHolder>(CallLogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val binding = ItemCallLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    var isEditMode = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class CallLogViewHolder(private val binding: ItemCallLogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(callLog: CallLog) {
            binding.tvContactName.text = callLog.contactName ?: callLog.phoneNumber
            binding.tvPhoneNumber.text = callLog.phoneNumber
            
            val sdf = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
            binding.tvTimestamp.text = sdf.format(Date(callLog.timestamp))
            
            binding.cbDelete.visibility = if (isEditMode) android.view.View.VISIBLE else android.view.View.GONE
            binding.cbDelete.setOnCheckedChangeListener { _, isChecked ->
                onSelectionChanged(callLog, isChecked)
            }

            if (callLog.isMissed) {
                binding.tvContactName.setTextColor(binding.root.context.getColor(R.color.errorColor))
                binding.ivCallType.setImageResource(R.drawable.ic_call_missed)
            } else {
                binding.tvContactName.setTextColor(binding.root.context.getColor(R.color.textPrimary))
                binding.ivCallType.setImageResource(R.drawable.ic_call_made)
            }
        }
    }

    class CallLogDiffCallback : DiffUtil.ItemCallback<CallLog>() {
        override fun areItemsTheSame(oldItem: CallLog, newItem: CallLog): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CallLog, newItem: CallLog): Boolean = oldItem == newItem
    }
}
