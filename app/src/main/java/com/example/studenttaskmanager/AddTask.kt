package com.example.studenttaskmanager

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studenttaskmanager.Database.TaskDatabase
import com.example.studenttaskmanager.Database.TaskList
import com.example.studenttaskmanager.databinding.ActivityAddTaskBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTask : AppCompatActivity() {
    private val binding: ActivityAddTaskBinding by lazy {
        ActivityAddTaskBinding.inflate(layoutInflater)
    }

    private val calendar = Calendar.getInstance()
    private lateinit var database: TaskDatabase

    private var existingTask: TaskList? = null // 👈 store incoming task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = TaskDatabase.getDatabase(this)

        existingTask = intent.getSerializableExtra("task") as? TaskList

        existingTask?.let { task ->
            binding.inputTitle.setText(task.title)
            binding.inputSubject.setText(task.subject)
            binding.inputDescription.setText(task.description)
            binding.textSelectedDate.text = task.dueDate
            binding.textSelectedDeadline.text = task.completionDate

            val priorityIndex = resources.getStringArray(R.array.priority_levels)
                .indexOf(task.priority)
            if (priorityIndex >= 0) binding.spinnerPriority.setSelection(priorityIndex)

            binding.btnSaveTask.text = "Update Task"
            binding.btnDeleteTask.visibility = View.VISIBLE

            // ✅ Disable editing if task is completed
            if (task.isCompleted) {
                disableEditing()
                binding.btnSaveTask.isEnabled = false
                binding.btnDeleteTask.isEnabled = true
                Toast.makeText(this, "This task is completed and cannot be edited", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnPickDate.setOnClickListener {
            showDatePicker { binding.textSelectedDate.text = it }
        }

        binding.btnCompletionDate.setOnClickListener {
            showDatePicker { binding.textSelectedDeadline.text = it }
        }



        binding.btnDeleteTask.setOnClickListener {
            existingTask?.let { taskToDelete ->
                // Show confirmation alert
                android.app.AlertDialog.Builder(this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes") { _, _ ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            database.taskDao().deleteTask(taskToDelete)
                            runOnUiThread {
                                Toast.makeText(this@AddTask, "Task Deleted", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }

    // 👇 Save or Update Task
        binding.btnSaveTask.setOnClickListener {
            val title = binding.inputTitle.text.toString().trim()
            val subject = binding.inputSubject.text.toString().trim()
            val dueDate = binding.textSelectedDate.text.toString().trim()
            val completionDate = binding.textSelectedDeadline.text.toString().trim()
            val priority = binding.spinnerPriority.selectedItem.toString()
            val description = binding.inputDescription.text.toString().trim()

            if (title.isNotEmpty() && subject.isNotEmpty() && dueDate.isNotEmpty()
                && completionDate.isNotEmpty() && priority.isNotEmpty() && description.isNotEmpty()) {

                val task = TaskList(
                    id = existingTask?.id ?: 0, // Use old ID if editing
                    title = title,
                    subject = subject,
                    dueDate = dueDate,
                    completionDate = completionDate,
                    priority = priority,
                    description = description,
                    isCompleted = existingTask?.isCompleted ?: false
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    if (existingTask == null) {
                        database.taskDao().insertTask(task)
                    } else {
                        database.taskDao().updateTask(task)
                    }
                    runOnUiThread {
                        Toast.makeText(
                            this@AddTask,
                            if (existingTask == null) "Task Added" else "Task Updated",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun disableEditing() {
        binding.inputTitle.isEnabled = false
        binding.inputSubject.isEnabled = false
        binding.inputDescription.isEnabled = false
        binding.spinnerPriority.isEnabled = false
        binding.btnPickDate.isEnabled = false
        binding.btnCompletionDate.isEnabled = false
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedCal = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            onDateSelected(format.format(selectedCal.time))
        }, year, month, day)

        datePickerDialog.show()
    }
}
