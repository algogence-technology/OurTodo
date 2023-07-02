package com.algogence.ourtodo.database

import android.content.Context
import androidx.room.Room
import com.algogence.ourtodo.mypackage.Task

class MyTasks(private val applicationContext: Context) {
    fun getAll(): List<Task>{
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "taskDatabase"
        ).build()

        val taskDao = db.taskDao()
        val dbTasks = taskDao.getAll()
        return dbTasks.map {
            Task(it.id,it.title,it.description,it.date,it.done)
        }
    }

    fun getAllDone(): List<Task>{
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "taskDatabase"
        ).build()

        val taskDao = db.taskDao()
        val dbTasks = taskDao.getAllDone()
        return dbTasks.map {
            Task(it.id,it.title,it.description,it.date,it.done)
        }
    }

    fun insert(task: Task){
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "taskDatabase"
        ).build()

        val taskDao = db.taskDao()
        val dbTask = com.algogence.ourtodo.database.Task(
            title = task.title,
            description = task.description,
            date = task.date,
            done = false
        )
        taskDao.insert(dbTask)
    }

    fun update(task: Task){
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "taskDatabase"
        ).build()

        val taskDao = db.taskDao()
        val dbTask = com.algogence.ourtodo.database.Task(
            title = task.title,
            description = task.description,
            date = task.date,
            done = task.done,
            id = task.id
        )
        taskDao.update(dbTask)
    }
}