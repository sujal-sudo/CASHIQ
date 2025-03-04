package com.example.cashiq.repository

import com.example.cashiq.model.NoteModel
import com.google.firebase.database.*

class NoteRepositoryImpl : NoteRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("notes")

    override fun addNote(note: NoteModel, callback: (Boolean, String) -> Unit) {
        val id = database.push().key.toString()
        val noteWithId = note.copy(id = id)
        database.child(id).setValue(noteWithId).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Note added successfully.")
            else callback(false, it.exception?.message ?: "Error adding note.")
        }
    }

    override fun getNoteById(noteId: String, callback: (NoteModel?, Boolean, String) -> Unit) {
        database.child(noteId).get().addOnSuccessListener {
            if (it.exists()) {
                val note = it.getValue(NoteModel::class.java)
                callback(note, true, "Note fetched successfully.")
            } else {
                callback(null, false, "Note not found.")
            }
        }.addOnFailureListener {
            callback(null, false, it.message ?: "Error fetching note.")
        }
    }

    override fun getAllNotes(userId: String, callback: (List<NoteModel>?, Boolean, String) -> Unit) {
        database.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notes = mutableListOf<NoteModel>()
                for (data in snapshot.children) {
                    val note = data.getValue(NoteModel::class.java)
                    note?.let { notes.add(it) }
                }
                callback(notes, true, "Notes fetched successfully.")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun updateNote(noteId: String, data: Map<String, Any>, callback: (Boolean, String) -> Unit) {
        database.child(noteId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Note updated successfully.")
            else callback(false, it.exception?.message ?: "Update failed.")
        }
    }

    override fun deleteNote(noteId: String, callback: (Boolean, String) -> Unit) {
        database.child(noteId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Note deleted successfully.")
            else callback(false, it.exception?.message ?: "Error deleting note.")
        }
    }
}
