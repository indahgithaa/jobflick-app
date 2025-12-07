package com.example.prak_papb_7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    // Inisialisasi Database dan ViewModel
    private val database by lazy { TaskDatabase.getDatabase(application) }
    private val repository by lazy { TaskRepository(database.taskDao()) }
    private val viewModelFactory by lazy { TaskViewModel.TaskViewModelFactory(repository) }
    private val viewModel: TaskViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            androidx.compose.material3.MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Ambil data Flow dan konversi menjadi Compose State
                    val tasks = viewModel.allTasks.collectAsState(initial = emptyList()).value

                    TaskScreen(
                        tasks = tasks,
                        onAddTask = { title -> viewModel.addNewTask(title) },
                        onUpdateTask = { task, completed ->
                            viewModel.updateTaskStatus(task, completed)
                        },
                        onDeleteTask = { task -> viewModel.deleteTask(task) },
                        onRenameTask = { task, newTitle -> viewModel.renameTask(task, newTitle) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    tasks: List<Task>,
    onAddTask: (String) -> Unit,
    onUpdateTask: (Task, Boolean) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onRenameTask: (Task, String) -> Unit // baru
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Daftar Tugas (Room Compose)") }) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            TaskInput(onAddTask)
            Spacer(modifier = Modifier.height(8.dp))
            TaskList(
                tasks = tasks,
                onUpdateTask = onUpdateTask,
                onDeleteTask = onDeleteTask,
                onRenameTask = onRenameTask // baru
            )
        }
    }
}


@Composable
fun TaskInput(onAddTask: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Tugas Baru") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onAddTask(text)
                    text = ""
                }
            },
            enabled = text.isNotBlank()
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Tambah Tugas")
        }
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onUpdateTask: (Task, Boolean) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onRenameTask: (Task, String) -> Unit // ✅ baru
) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(tasks, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onCheckedChange = { isChecked -> onUpdateTask(task, isChecked) },
                onDelete = { onDeleteTask(task) },
                onRename = { newTitle -> onRenameTask(task, newTitle) } // ✅ baru
            )
            Divider()
        }
    }
}


@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onRename: (String) -> Unit // ✅ baru
) {
    var showEdit by remember { mutableStateOf(false) }
    var tempTitle by remember { mutableStateOf(task.title) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!task.isCompleted) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onCheckedChange,
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        // ✏️ Icon Edit
        IconButton(onClick = {
            tempTitle = task.title
            showEdit = true
        }) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit Tugas")
        }

        // 🗑️ Icon Delete
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Hapus Tugas")
        }
    }

    if (showEdit) {
        AlertDialog(
            onDismissRequest = { showEdit = false },
            title = { Text("Ubah Nama Tugas") },
            text = {
                TextField(
                    value = tempTitle,
                    onValueChange = { tempTitle = it },
                    singleLine = true,
                    label = { Text("Nama Tugas") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val trimmed = tempTitle.trim()
                        if (trimmed.isNotEmpty() && trimmed != task.title) {
                            onRename(trimmed)
                        }
                        showEdit = false
                    }
                ) { Text("Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showEdit = false }) { Text("Batal") }
            }
        )
    }
}
