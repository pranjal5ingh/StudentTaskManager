package com.example.studenttaskmanager

import android.app.DatePickerDialog
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Handle Pick Date button
        binding.btnPickDate.setOnClickListener {
            showDatePicker{ selectedDate ->
                binding.textSelectedDate.text = selectedDate
            }
        }

        // Handle Completion Deadline Button
        binding.btnCompletionDate.setOnClickListener {
            showDatePicker{ selectedDate ->
                binding.textSelectedDeadline.text = selectedDate
            }
        }

        database = TaskDatabase.getDatabase(this)

        binding.btnSaveTask.setOnClickListener {
            val title = binding.inputTitle.text.toString().trim()
            val subject = binding.inputSubject.text.toString().trim()
            val dueDate = binding.btnPickDate.text.toString().trim()
            val completionDate = binding.btnCompletionDate.text.toString().trim()
            val priority = binding.spinnerPriority.selectedItem.toString()
            val description = binding.inputDescription.text.toString().trim()

            if (title.isNotEmpty() && subject.isNotEmpty() && dueDate.isNotEmpty()
                && completionDate.isNotEmpty() && priority.isNotEmpty() && description.isNotEmpty()) {

                val task = TaskList(
                    title = title,
                    subject = subject,
                    dueDate = dueDate,
                    completionDate = completionDate,
                    priority = priority,
                    description = description
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    database.taskDao().insertTask(task)
                    runOnUiThread {
                        Toast.makeText(this@AddTask, "Task Added Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Format date as "dd/MM/yyyy"
            val selectedCal = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = format.format(selectedCal.time)
            onDateSelected(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }
}
