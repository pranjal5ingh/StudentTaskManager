package com.example.studenttaskmanager.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface TaskDAO {

    @Insert
    suspend fun insertTask(taskList: TaskList)

    @Update
    suspend fun updateTask(taskList: TaskList)

    @Delete
    suspend fun deleteTask(taskList: TaskList)

    @Query("SELECT * FROM tasklist")
    suspend fun getAllTasks(): List<TaskList>

    @Query("SELECT * FROM tasklist WHERE isCompleted = 0 AND dueDate >= :currentTime ORDER BY dueDate ASC")
    suspend fun getUpcomingTasks(currentTime: Long): List<TaskList>



}