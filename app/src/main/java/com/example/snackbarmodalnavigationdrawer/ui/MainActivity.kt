package com.example.snackbarmodalnavigationdrawer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.snackbarmodalnavigationdrawer.ui.utils.NotePreferences
import com.example.snackbarmodalnavigationdrawer.data.Note
import com.example.snackbarmodalnavigationdrawer.ui.screen.NoteEditorScreen
import com.example.snackbarmodalnavigationdrawer.ui.screen.NoteListScreen
import com.example.snackbarmodalnavigationdrawer.ui.theme.SnackbarModalNavigationDrawerTheme
import com.example.snackbarmodalnavigationdrawer.viewmodel.NotesViewModel
import com.example.snackbarmodalnavigationdrawer.viewmodel.NotesViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val notePrefs = NotePreferences(this)
        notesViewModel = ViewModelProvider(this, NotesViewModelFactory(notePrefs)).get(NotesViewModel::class.java)

        setContent {
            SnackbarModalNavigationDrawerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val (showEditor, setShowEditor) = remember { mutableStateOf(false) }
                    val coroutineScope = rememberCoroutineScope()
                    var selectedNote by remember { mutableStateOf<Note?>(null) }

                    LaunchedEffect(Unit) {
                        val savedNoteId = notePrefs.getSelectedNoteId()
                        if (savedNoteId != -1) {
                            selectedNote = notesViewModel.notes.find { it.id == savedNoteId }
                        }
                    }

                    if (showEditor) {
                        NoteEditorScreen(
                            onSaveNote = { title, content ->
                                notesViewModel.addNote(title, content)
                                setShowEditor(false)
                            },
                            onNavigateBack = { setShowEditor(false) }
                        )
                    } else {
                        NoteListScreen(
                            notes = notesViewModel.notes,
                            selectedNote = selectedNote,
                            onNoteDelete = { note ->
                                notesViewModel.deleteNote(note)
                                coroutineScope.launch {
                                    notePrefs.saveSelectedNoteId(-1)
                                }
                            },
                            onAddNote = { setShowEditor(true) },
                            onNoteSelect = { note ->
                                selectedNote = note
                                coroutineScope.launch {
                                    notePrefs.saveSelectedNoteId(note.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
