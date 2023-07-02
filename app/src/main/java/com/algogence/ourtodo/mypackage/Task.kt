package com.algogence.ourtodo.mypackage

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    var done: Boolean = false
)