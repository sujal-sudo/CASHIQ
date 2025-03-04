package com.example.cashiq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashiq.model.NoteModel
import com.example.cashiq.repository.NoteRepository
import com.example.cashiq.repository.NoteRepositoryImpl

class NoteViewModel : ViewModel() {

    private val noteRepository: NoteRepository = NoteRepositoryImpl()

    private val _note = MutableLiveData<NoteModel?>()
    val note: LiveData<NoteModel?> get() = _note

    private val _allNotes = MutableLiveData<List<NoteModel>>()
    val allNotes: LiveData<List<NoteModel>> get() = _allNotes

    private val _operationStatus = MutableLiveData<Pair<Boolean, String>>()
    val operationStatus: LiveData<Pair<Boolean, String>> get() = _operationStatus

    fun addNote(note: NoteModel) {
        noteRepository.addNote(note) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun updateNote(noteId: String, data: MutableMap<String, Any>) {
        noteRepository.updateNote(noteId, data) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun deleteNote(noteId: String) {
        noteRepository.deleteNote(noteId) { success, message ->
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun getNoteById(noteId: String) {
        noteRepository.getNoteById(noteId) { note, success, message ->
            _note.postValue(note)
            _operationStatus.postValue(Pair(success, message))
        }
    }

    fun getAllNotes(userId: String) {
        noteRepository.getAllNotes(userId) { notes, success, message ->
            _allNotes.postValue(notes ?: emptyList())
            _operationStatus.postValue(Pair(success, message))
        }
    }
}
