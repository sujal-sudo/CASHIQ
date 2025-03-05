package com.example.cashiq.repository

import com.example.cashiq.model.NoteModel
import com.google.firebase.database.*

class NoteRepositoryImpl : NoteRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("notes")

    override fun addNote(note: NoteModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        val noteWithId = note.copy(id = id)

        ref.child(id).setValue(noteWithId).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Note added successfully.")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun updateNote(
        noteId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(noteId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Note updated successfully.")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun deleteNote(noteId: String, callback: (Boolean, String) -> Unit) {
        ref.child(noteId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Note deleted successfully.")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getNoteById(
        noteId: String,
        callback: (NoteModel?, Boolean, String) -> Unit
    ) {
        ref.child(noteId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val note = snapshot.getValue(NoteModel::class.java)
                    callback(note, true, "Note fetched successfully.")
                } else {
                    callback(null, false, "Note not found.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun getAllNotes(userId: String, callback: (List<NoteModel>?, Boolean, String) -> Unit) {
        ref.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val notes = mutableListOf<NoteModel>()
                    for (eachData in snapshot.children) {
                        val note = eachData.getValue(NoteModel::class.java)
                        note?.let { notes.add(it) }
                    }
                    callback(notes, true, "Notes fetched successfully.")
                } else {
                    callback(null, false, "No notes found.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }
}
