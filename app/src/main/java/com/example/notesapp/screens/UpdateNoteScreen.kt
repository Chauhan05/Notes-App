package com.example.notesapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.notesapp.viewmodels.AppViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateNoteScreen(
    noteId: Int,
    navController: NavHostController,
    viewModel: AppViewModel,
) {

//     For the first Composition
    LaunchedEffect(Unit) {
        viewModel.getNoteById(noteId)
    }
    val note = viewModel.noteById.collectAsStateWithLifecycle()

    val title = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    val description = remember {
        mutableStateOf("")
    }


//    updating for the local change
    LaunchedEffect(note.value) {
        note.value?.let {
            title.value = it.title
            description.value = it.description
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Note") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, "arrowBack")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            note.value?.let {
                                viewModel.updateANote(
                                    it.copy(
                                        date=LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM")),
                                        time=LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                                        title = title.value,
                                        description = description.value
                                    )
                                )
                                Toast.makeText(
                                    context,
                                    "Note Updated Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Check, "Save")
                    }
                }
            )
        }
    ) {
        if (note.value == null) {
            Box(Modifier
                .padding(it)
                .fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
                TitleTextField(
                    value = title.value,
                    onValueChange = {
                        title.value=it
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                DescriptionTextField(
                    value = description.value,
                    onValueChange ={
                        description.value=it
                    }
                )
//                OutlinedTextField(
//                    value = title.value,
//                    onValueChange = { title.value = it },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp),
//                    label = { Text("Title") }
//                )
//
//                OutlinedTextField(
//                    value = description.value,
//                    onValueChange = { description.value = it },
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    label = { Text("Content") }
//                )
            }
        }
    }
}


//
//@Composable
//fun TopRowActions(uiState: State<NoteUiState>, toggleFav: () -> Unit) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // Favorite button
//        IconButton(onClick = toggleFav) {
//            Icon(
//                imageVector = if (uiState.value.isStarred) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
//                contentDescription = "Favorite",
//                tint = if (uiState.value.isStarred) Color(0xFFE91E63) else MaterialTheme.colorScheme.onPrimaryContainer
//            )
//        }
//
//        // Share button
//        IconButton(onClick = { /* Share functionality */ }) {
//            Icon(
//                imageVector = Icons.Default.Share,
//                contentDescription = "Share"
//            )
//        }
//
//        // More options button
//        IconButton(onClick = { /* More options */ }) {
//            Icon(
//                imageVector = Icons.Default.MoreVert,
//                contentDescription = "More Options"
//            )
//        }
//    }
//}