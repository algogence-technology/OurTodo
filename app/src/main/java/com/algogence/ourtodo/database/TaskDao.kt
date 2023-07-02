package com.algogence.ourtodo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Insert
    fun insert(task: Task)

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Delete
    fun delete(task: Task)
}