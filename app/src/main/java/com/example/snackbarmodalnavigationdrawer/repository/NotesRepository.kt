package com.example.snackbarmodalnavigationdrawer.repository

import com.example.snackbarmodalnavigationdrawer.data.Note

object NotesRepository {
    private val _notes = mutableListOf<Note>()
    val notes: List<Note> get() = _notes

    fun addNote(note: Note) {
        _notes.add(note)
    }

    fun deleteNote(note: Note) {
        _notes.remove(note)
    }

    fun setNotes(notes: List<Note>) {
        _notes.clear()
        _notes.addAll(notes)
    }
}
