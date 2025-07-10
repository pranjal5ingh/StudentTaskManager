package com.example.studenttaskmanager.Database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

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
) : Serializable
