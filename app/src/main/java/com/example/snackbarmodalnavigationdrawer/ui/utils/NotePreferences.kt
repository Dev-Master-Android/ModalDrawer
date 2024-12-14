package com.example.snackbarmodalnavigationdrawer.ui.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.snackbarmodalnavigationdrawer.data.Note

class NotePreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)

    fun saveNotes(notes: List<Note>) {
        val notesSet = notes.map { "${it.id};;${it.title};;${it.content}" }.toSet()
        prefs.edit().putStringSet("notes", notesSet).apply()
    }

    fun loadNotes(): List<Note> {
        val notesSet = prefs.getStringSet("notes", emptySet()) ?: emptySet()
        return notesSet.map {
            val parts = it.split(";;")
            Note(
                id = parts[0].toInt(),
                title = parts[1],
                content = parts[2]
            )
        }
    }

    fun saveSelectedNoteId(noteId: Int) {
        prefs.edit().putInt("selected_note_id", noteId).apply()
    }

    fun getSelectedNoteId(): Int {
        return prefs.getInt("selected_note_id", -1)
    }
}
