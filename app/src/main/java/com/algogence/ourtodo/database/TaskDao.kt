package com.algogence.ourtodo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    fun insert(task: Task)

    @Query("SELECT * FROM task WHERE done != 1")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE done == 1")
    fun getAllDone(): List<Task>

    @Delete
    fun delete(task: Task)

    @Update
    fun update(task: Task)
}