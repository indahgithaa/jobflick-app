package com.example.praktikum_papb_03.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.praktikum_papb_03.data.model.Note

class MainActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NotesScreen(noteViewModel)
            }
        }
        noteViewModel.fetchNotes()
    }
}

@Composable
fun NotesScreen(noteViewModel: NoteViewModel) {
    //ga pake observeasstate lagi
    val notes = noteViewModel.notes
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<String?>(null) }

    //lanjutan..

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = if (editingId == null) "Tambah Catatan Baru" else "Ubah Catatan",
            style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(8.dp))

        //diubah ke textfield
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        //diubah ke textfield
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Isi Catatan") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Simpan / Perbarui
        Button(onClick = {
            if (title.isNotBlank() && content.isNotBlank()) {
                val note = Note(title = title, content = content)
                val id = editingId
                if (id == null) {
                    noteViewModel.addNote(note)
                } else {
                    noteViewModel.updateNote(id, note)
                }
                // reset form
                title = ""
                content = ""
                editingId = null
            }
        }) {
            Text(if (editingId == null) "Simpan Catatan" else "Perbarui Catatan")
        }

        Spacer(Modifier.height(16.dp))
        Text("Daftar Catatan", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        if (notes.isEmpty()) {
            Text(
                "Daftar kosong",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        notes.forEach { note ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)) {

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = note.content, style = MaterialTheme.typography.bodyMedium)

                    note.timestamp?.let {
                        Text(
                            text = "Dibuat: ${it.toDate()}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row {
                        TextButton(onClick = {
                            editingId = note.id
                            title = note.title
                            content = note.content
                        }) { Text("Edit") }

                        TextButton(onClick = {
                            note.id?.let { noteViewModel.deleteNote(it) }
                        }) { Text("Hapus") }
                    }
                }
            }
        }
    }
}
