package com.example.snackbarmodalnavigationdrawer.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.snackbarmodalnavigationdrawer.data.Note
import com.example.snackbarmodalnavigationdrawer.ui.utils.NotePreferences
import com.example.snackbarmodalnavigationdrawer.repository.NotesRepository

class NotesViewModel(private val notePrefs: NotePreferences) : ViewModel() {
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> get() = _notes

    init {
        _notes.addAll(notePrefs.loadNotes())
    }

    fun addNote(title: String, content: String) {
        val newNote = Note(
            id = notes.size + 1,
            title = title,
            content = content
        )
        NotesRepository.addNote(newNote)
        notePrefs.saveNotes(NotesRepository.notes)
        _notes.add(newNote)
    }

    fun deleteNote(note: Note) {
        NotesRepository.deleteNote(note)
        notePrefs.saveNotes(NotesRepository.notes)
        _notes.remove(note)
    }
}
