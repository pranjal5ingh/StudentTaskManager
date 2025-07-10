package com.example.studenttaskmanager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studenttaskmanager.Database.TaskList
import com.example.studenttaskmanager.databinding.FragmentTaskDetailBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TaskDetailBottomFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTaskDetailBottomBinding? = null
    private val binding get() = _binding!!

    private var task: TaskList? = null


    companion object {
        fun newInstance(task: TaskList): TaskDetailBottomFragment {
            val fragment = TaskDetailBottomFragment()
            val args = Bundle()
            args.putSerializable("task", task)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = arguments?.getSerializable("task") as? TaskList

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTaskDetailBottomBinding.inflate(inflater, container, false)


        task?.let {
            binding.textTitle.text = it.title
            binding.textSubject.text = "Subject: ${it.subject}"
            binding.textDueDate.text = "Due Date: ${it.dueDate}"
            binding.textCompletionDate.text = "Completed On: ${it.completionDate}"
            binding.textPriority.text = "Priority: ${it.priority}"
            binding.textDescription.text = "Description: ${it.description}"
        }

        return binding.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

