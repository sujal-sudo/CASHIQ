package com.example.cashiq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.NoteModel
import com.example.cashiq.repository.NoteRepository
import com.example.cashiq.repository.NoteRepositoryImpl

class NoteViewModel : ViewModel() {

    private val noteRepository: NoteRepository = NoteRepositoryImpl()

    private val _notes = MutableLiveData<List<NoteModel>?>()
    val notes: LiveData<List<NoteModel>?> get() = _notes

    private val _operationStatus = MutableLiveData<Pair<Boolean, String>>()
    val operationStatus: LiveData<Pair<Boolean, String>> get() = _operationStatus

    fun addNote(note: NoteModel) {
        noteRepository.addNote(note) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun getNoteById(noteId: String) {
        noteRepository.getNoteById(noteId) { note, success, message ->
            if (success) _notes.postValue(listOf(note!!))
            else _operationStatus.postValue(Pair(false, message))
        }
    }

    fun getAllNotes(userId: String) {
        noteRepository.getAllNotes(userId) { noteList, success, message ->
            if (success) _notes.postValue(noteList)
            else _operationStatus.postValue(Pair(false, message))
        }
    }

    fun updateNote(noteId: String, data: Map<String, Any>) {
        noteRepository.updateNote(noteId, data) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun deleteNote(noteId: String) {
        noteRepository.deleteNote(noteId) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }
}
