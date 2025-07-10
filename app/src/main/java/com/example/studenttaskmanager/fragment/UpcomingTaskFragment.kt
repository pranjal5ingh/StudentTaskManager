package com.example.studenttaskmanager.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studenttaskmanager.AddTask
import com.example.studenttaskmanager.Database.TaskDatabase
import com.example.studenttaskmanager.Database.TaskList
import com.example.studenttaskmanager.adapter.UpcomingTaskAdapter
import com.example.studenttaskmanager.databinding.FragmentUpcomingTaskBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpcomingTaskFragment : Fragment() {

    private var _binding: FragmentUpcomingTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: UpcomingTaskAdapter
    private lateinit var db: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingTaskBinding.inflate(inflater, container, false)
        db = TaskDatabase.getDatabase(requireContext())

        binding.fabAddTask.setOnClickListener {
            val intent = Intent(requireContext(), AddTask::class.java)
            startActivity(intent)
        }

        taskAdapter = UpcomingTaskAdapter(
            onSingleClick = { task ->
                val bottomSheet = TaskDetailBottomFragment.newInstance(task)
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            },
            onDoubleClickOrLongPress = { task ->
                val intent = Intent(requireContext(), AddTask::class.java)
                intent.putExtra("task", task)
                startActivity(intent)
            }
        )

        binding.recyclerIncompleteTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerIncompleteTasks.adapter = taskAdapter

        loadTasksFromDb()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadTasksFromDb()
    }

    private fun loadTasksFromDb() {
        lifecycleScope.launch {
            val now = System.currentTimeMillis()
            val taskList = withContext(Dispatchers.IO) {
                db.taskDao().getUpcomingTasks(now)
            }

            taskAdapter.setTasks(taskList)
            binding.textNoTasks.visibility = if (taskList.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
