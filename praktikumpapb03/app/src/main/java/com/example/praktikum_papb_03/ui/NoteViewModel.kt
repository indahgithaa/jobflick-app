package com.example.praktikum_papb_03.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.praktikum_papb_03.data.model.Note
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class NoteViewModel : ViewModel() {
    private val db by lazy { com.google.firebase.firestore.FirebaseFirestore.getInstance() }

    //ubah jadi variabel biasa
    var notes: List<Note> = emptyList()

    fun fetchNotes() {
        db.collection("notes").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            notes = snapshot.documents.mapNotNull {
                it.toObject<Note>()?.copy(id = it.id)
            }
        }
    }


    fun addNote(note: Note) {
        val noteWithTime = note.copy(timestamp = com.google.firebase.Timestamp.now())
        db.collection("notes").add(noteWithTime)
    }

    fun updateNote(noteId: String, note: Note) {
        db.collection("notes").document(noteId).set(note)
    }

    fun deleteNote(noteId: String) {
        db.collection("notes").document(noteId).delete()
    }
}
