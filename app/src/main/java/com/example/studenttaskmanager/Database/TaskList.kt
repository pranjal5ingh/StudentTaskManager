package com.example.studenttaskmanager.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskList")
data class TaskList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val subject: String,
    val priority: String,
    val dueDate: String,
    val completionDate: String,
    val isCompleted: Boolean = false
)
