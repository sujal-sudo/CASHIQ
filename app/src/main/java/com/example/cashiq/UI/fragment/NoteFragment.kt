package com.example.cashiq.UI.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.cashiq.R
import com.example.cashiq.model.NoteModel
import com.example.cashiq.viewmodel.NoteViewModel
import com.google.firebase.auth.FirebaseAuth

class NoteFragment : Fragment() {

    private lateinit var titleInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var addNoteButton: Button
    private lateinit var noteContainer: LinearLayout

    private val noteViewModel: NoteViewModel by viewModels()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleInput = view.findViewById(R.id.titleInput)
        contentInput = view.findViewById(R.id.contentInput)
        addNoteButton = view.findViewById(R.id.addNoteButton)
        noteContainer = view.findViewById(R.id.noteContainer)

        // Load existing notes for this user
        loadUserNotes()

        // Add Note Button Click Listener
        addNoteButton.setOnClickListener {
            addNote()
        }
    }

    private fun loadUserNotes() {
        if (userId == null) return

        noteViewModel.getAllNotes(userId)
        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer { notes ->
            noteContainer.removeAllViews()
            for (note in notes) {
                addNoteToUI(note)
            }
        })
    }

    private fun addNote() {
        val title = titleInput.text.toString().trim()
        val content = contentInput.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(requireContext(), "Title and Content cannot be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        val newNote = NoteModel(id = "", title = title, comment = content, userId = userId ?: "")

        noteViewModel.addNote(newNote)
        noteViewModel.operationStatus.observe(viewLifecycleOwner, Observer { (success, message) ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            if (success) {
                titleInput.text.clear()
                contentInput.text.clear()
                loadUserNotes()
            }
        })
    }

    private fun addNoteToUI(note: NoteModel) {
        val noteView = LayoutInflater.from(requireContext()).inflate(R.layout.item_note, noteContainer, false)

        val noteTitle = noteView.findViewById<TextView>(R.id.noteTitle)
        val noteContent = noteView.findViewById<TextView>(R.id.noteContent)
        val editButton = noteView.findViewById<ImageButton>(R.id.editNoteButton)
        val deleteButton = noteView.findViewById<ImageButton>(R.id.deleteNoteButton)

        noteTitle.text = note.title
        noteContent.text = note.comment

        editButton.setOnClickListener { showEditDialog(note) }
        deleteButton.setOnClickListener { deleteNote(note.id) }

        noteContainer.addView(noteView)
    }

    private fun showEditDialog(note: NoteModel) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Note")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL

        val inputTitle = EditText(requireContext())
        inputTitle.setText(note.title)
        layout.addView(inputTitle)

        val inputContent = EditText(requireContext())
        inputContent.setText(note.comment)
        layout.addView(inputContent)

        builder.setView(layout)

        builder.setPositiveButton("Save") { _, _ ->
            val updatedData = mutableMapOf<String, Any>(
                "title" to inputTitle.text.toString(),
                "comment" to inputContent.text.toString()
            )
            noteViewModel.updateNote(note.id, updatedData)
            noteViewModel.operationStatus.observe(viewLifecycleOwner, Observer { (success, message) ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                if (success) {
                    loadUserNotes()
                }
            })
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun deleteNote(noteId: String) {
        noteViewModel.deleteNote(noteId)
        noteViewModel.operationStatus.observe(viewLifecycleOwner, Observer { (success, message) ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            if (success) {
                loadUserNotes()
            }
        })
    }
}
