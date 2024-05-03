package com.hfad.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TasksViewModel(val dao: TaskDao) : ViewModel() {
    val newTaskName = MutableLiveData<String>()
    private val tasks: LiveData<List<Task>> = dao.getAll()

    // Use map extension function directly on tasks LiveData
    val tasksString: LiveData<String> = tasks.map { tasks -> formatTasks(tasks) }

    fun addTask() {
        viewModelScope.launch {
            val task = Task()
            task.taskName = newTaskName.value ?: "" // Handle potential null value
            dao.insert(task)
        }
    }

    fun formatTasks(tasks: List<Task>): String {
        return tasks.fold("") { str, item -> str + '\n' + formatTask(item) }
    }

    fun formatTask(task: Task): String {
        var str = "ID: ${task.taskId}"
        str += '\n' + "Name: ${task.taskName}"
        str += '\n' + "Complete: ${task.taskDone}" + '\n'
        return str
    }
}