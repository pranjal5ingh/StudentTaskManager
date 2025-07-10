package com.example.studenttaskmanager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studenttaskmanager.Database.TaskList
import com.example.studenttaskmanager.R
import com.example.studenttaskmanager.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.Locale

class UpcomingTaskAdapter(
    private var taskList: List<TaskList> = emptyList(),
    private val onSingleClick: (TaskList) -> Unit,
    private val onDoubleClickOrLongPress: (TaskList) -> Unit
) : RecyclerView.Adapter<UpcomingTaskAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)


        var lastClickTime = 0L
        holder.itemView.setOnClickListener {
            val now = System.currentTimeMillis()
            if (now - lastClickTime < 300) {
                // Double click detected
                onDoubleClickOrLongPress(task)
            } else {
                onSingleClick(task)
            }
            lastClickTime = now
        }

        holder.itemView.setOnLongClickListener {
            onDoubleClickOrLongPress(task)
            true
        }
    }

    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskList) {
            binding.textTaskTitle.text = task.title
            binding.textSubjectTag.text = task.subject
            binding.textPriority.text = task.priority
            binding.checkboxCompleted.isChecked = task.isCompleted
            binding.imagePriorityIcon.setImageResource(getPriorityIcon(task.priority))

            // 🔍 Log the raw due date value
            Log.d("UpcomingTaskAdapter", "Due Date Raw: ${task.dueDate}")
            // Parse and format the due date safely
            try {
                val parser = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // match the format stored in DB
                val date = parser.parse(task.dueDate)
                val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                binding.textDueDate.text = "Due: ${formatter.format(date!!)}"
            } catch (e: Exception) {
                binding.textDueDate.text = "Due: ${task.dueDate}" // fallback
                Log.e("UpcomingTaskAdapter", "Date parsing failed", e)

            }
        }
    }

    // Helper function to show different icons based on priority
    private fun getPriorityIcon(priority: String): Int {
        return when (priority.lowercase()) {
            "high" -> R.drawable.high_priority
            "medium" -> R.drawable.medium_priority
            "low" -> R.drawable.low_priority
            else -> R.drawable.default_priority
        }
    }

    // Optional: Update data dynamically
    fun setTasks(tasks: List<TaskList>) {
        taskList = tasks
        notifyDataSetChanged()
    }
}
