package com.example.studenttaskmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studenttaskmanager.Database.TaskList
import com.example.studenttaskmanager.databinding.TaskDetailsItemsBinding

class TaskDetailAdapter( val taskList: List<TaskList>):RecyclerView.Adapter<TaskDetailAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = TaskDetailsItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val task = taskList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = taskList.size

    class ViewHolder(val binding: TaskDetailsItemsBinding):RecyclerView.ViewHolder( binding.root) {
        fun bind(task: TaskList) {
            binding.textTitle.text = task.title
            binding.textSubject.text = task.subject
            binding.textPriority.text = task.priority
            binding.textDueDate.text = task.dueDate
            binding.textCompletionDate.text = task.completionDate
            binding.textDescription.text = task.description

        }

    }
}
