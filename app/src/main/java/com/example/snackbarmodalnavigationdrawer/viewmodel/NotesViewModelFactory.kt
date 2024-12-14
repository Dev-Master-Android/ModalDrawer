package com.example.snackbarmodalnavigationdrawer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.snackbarmodalnavigationdrawer.ui.utils.NotePreferences

class NotesViewModelFactory(private val notePrefs: NotePreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(notePrefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
