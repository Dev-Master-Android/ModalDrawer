package com.example.snackbarmodalnavigationdrawer.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.snackbarmodalnavigationdrawer.data.Note
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteListScreen(
    notes: List<Note>,
    selectedNote: Note?,
    onNoteDelete: (Note) -> Unit,
    onAddNote: () -> Unit,
    onNoteSelect: (Note) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    notes.forEach { note ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().clickable {
                                onNoteSelect(note)
                                coroutineScope.launch { drawerState.close() }
                            }
                        ) {
                            Text(
                                text = note.title,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            IconButton(onClick = {
                                if (notes.size > 1) {
                                    onNoteDelete(note)
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Создайте хотя бы еще одну заметку перед удалением последней.")
                                    }
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(selectedNote?.title ?: "Добро пожаловать!") },
                        navigationIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = onAddNote) {
                        Icon(Icons.Default.Add, contentDescription = "Add Note")
                    }
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { paddingValues ->
                if (notes.isEmpty()) {
                    Column(modifier = Modifier.padding(paddingValues).verticalScroll(rememberScrollState()).padding(16.dp)) {
                        Text(text = "Напишите свою первую заметку", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    selectedNote?.let { note ->
                        Column(modifier = Modifier.padding(paddingValues).verticalScroll(rememberScrollState()).padding(16.dp)) {
                            Text(text = note.content, style = MaterialTheme.typography.bodyLarge)
                        }
                    } ?: run {
                        Text(text = "Напишите свою первую заметку", modifier = Modifier.padding(paddingValues).padding(16.dp))
                    }
                }
            }
        }
    )
}
