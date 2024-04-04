package com.example.mobil1

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    AppTheme {
        TodoApp()
    }
}

@Composable
fun TodoApp() {
    var taskText by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf<Task>() }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TodoInput(
            taskText = taskText,
            onTaskTextChanged = { taskText = it },
            onAddTask = {
                if (taskText.isNotBlank()) {
                    tasks.add(Task(taskText))
                    taskText = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskList(
            tasks = tasks,
            onTaskCheckedChange = { task -> task.toggleChecked() }
        )
    }
}

@Composable
fun TodoInput(
    taskText: String,
    onTaskTextChanged: (String) -> Unit,
    onAddTask: () -> Unit
) {
    var focusRequester = remember { FocusRequester() }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = taskText,
            onValueChange = { onTaskTextChanged(it) },
            label = { Text("Enter task") },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAddTask()
                    focusRequester.requestFocus()
                }
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                onAddTask()
                focusRequester.requestFocus()
            },
            modifier = Modifier
                .wrapContentWidth()
        ) {
            Text("Add")
        }
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskCheckedChange: (Task) -> Unit
) {
    Column {
        tasks.forEach { task ->
            TaskItem(
                task = task,
                onCheckedChange = { onTaskCheckedChange(task) }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable { onCheckedChange() }
    ) {
        Checkbox(
            checked = task.checked,
            onCheckedChange = null, // We handle checked change in parent
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = task.text,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis
        )
    }

    if (task.checked) {
        Divider(color = Color.Black, thickness = 1.dp)
    }
}


data class Task(val text: String, var checked: Boolean = false) {
    fun toggleChecked() {
        checked = !checked
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}
