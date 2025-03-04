package com.example.cashiq.repository

import com.example.cashiq.model.NoteModel

interface NoteRepository {
    fun addNote(note: NoteModel, callback: (Boolean, String) -> Unit)
    fun getNoteById(noteId: String, callback: (NoteModel?, Boolean, String) -> Unit)
    fun getAllNotes(userId: String, callback: (List<NoteModel>?, Boolean, String) -> Unit)
    fun updateNote(noteId: String, data: Map<String, Any>, callback: (Boolean, String) -> Unit)
    fun deleteNote(noteId: String, callback: (Boolean, String) -> Unit)
}
