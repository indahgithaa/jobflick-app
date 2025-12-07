package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.todoapp.data.TodoRepositoryImpl
import com.example.todoapp.ui.TodoScreen
import com.example.todoapp.viewmodel.TodoViewModelFactory
import com.example.todoapp.viewmodel.TodoViewModelWithRepo
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // Inisialisasi repository dan factory
                val repo = TodoRepositoryImpl()
                val factory = TodoViewModelFactory(repo)

                // Buat instance ViewModel lewat factory
                val vm: TodoViewModelWithRepo = viewModel(factory = factory)

                // Panggil UI utama dengan ViewModel yang terhubung ke repo
                TodoScreen(vm)
            }
        }
    }
}
