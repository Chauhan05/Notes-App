package com.example.notesapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.notesapp.AppViewModel
import com.example.notesapp.R
import com.example.notesapp.data.NoteEntity
import com.example.notesapp.navigation.Screens

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
//    viewModel: AppViewModel
) {
    val notesList = viewModel.getAllNotes.collectAsState(emptyList())
    val showAlertDialogBox = remember {
        mutableStateOf(false)
    }
    val selectedNote = remember { mutableStateOf<NoteEntity?>(null) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddNoteScreen.route) {
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(75.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IntroCmp()
            SearchBarCmp(query = "", onValueChange = {})
            Spacer(modifier = Modifier.height(8.dp))
//            NoteCmp(note = NoteEntity(0, "Task Management", "lorem ipsum", false, "28 May", "7:30"))
//            NoteCmp(
//                note = NoteEntity(
//                    0,
//                    "Task Management",
//                    "lorem ipsum",
//                    true,
//                    "28 May",
//                    "7:30",
//                    "nitin"
//                )
//            )
//            NoteCmp(note = NoteEntity(0, "Task Management", "lorem ipsum", false, "28 May", "7:30"))
//        Lazy Column
            LazyColumn {
                items(notesList.value) { note ->
                    NoteCmp(note = note) {
//                        Storing the value of the current note
                        selectedNote.value = note
                        showAlertDialogBox.value = true
                        Log.d("Main",selectedNote.value.toString())
//                        Alert Dialog box click confirm will update the entry and
//                        Cancel with do nothing
                    }

                }

            }
            if (showAlertDialogBox.value && selectedNote.value != null) {
//                            show the dialog box
                AlertDialogCmp(
                    selectedNote.value!!.isStarred,
                    onConfirm = {
//                        Update the note
                        selectedNote.value= selectedNote.value!!.copy(isStarred = !selectedNote.value!!.isStarred)
                        viewModel.updateAWish(selectedNote.value!!)
//                        hide the dialog
                        showAlertDialogBox.value=false
                        Log.d("Main",notesList.value.toString())
                    },
                    onDismiss = {
                        showAlertDialogBox.value=false
                    }
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogCmp(isStarred: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = if (isStarred) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                contentDescription = "favIcon"
            )
        },
        title = { Text(if (isStarred) "Unstar Note?" else "Star Note?") },
        text = {
            Text(if (isStarred) "Do you want to remove this from favorites?" else "Do you want to add this to favorites?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun SearchBarCmp(
    modifier: Modifier = Modifier,
    query: String,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = query,
        onValueChange = onValueChange,
        label = {
            Text("Search Here")
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(Icons.Default.Search, "searchIcon")
        },
        shape = RoundedCornerShape(8.dp)
    )

}

@Composable
fun NoteCmp(
    modifier: Modifier = Modifier,
    note: NoteEntity,
    onHeartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .then(
                        if (note.password != null) Modifier.graphicsLayer {
                            alpha = 0.02f  // Semi-transparent for locked notes
                        } else Modifier
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = note.date,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                        color = Color.Gray
                    )
                    IconButton(onClick = onHeartClick) {
                        Icon(
                            if (note.isStarred) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "fav"
                        )
                    }
                }

                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Description
                Text(
                    text = note.description,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                // Time (Aligned to Bottom Right)
                Text(
                    text = note.time,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                )
            }
        }

        // Overlay for Locked Notes
        if (note.password != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            // Lock Icon in Center
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked",
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )

            // More Options Icon (Top Right)
            IconButton(
                onClick = { /* Handle password unlock */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    Icons.Default.MoreHoriz,
                    "Options",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}


@Composable
fun IntroCmp() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text("Hello,", style = MaterialTheme.typography.titleLarge, fontSize = 42.sp)
            Text("My Notes", style = MaterialTheme.typography.titleLarge, fontSize = 42.sp)
        }
        IconButton(
            onClick = {},

            ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_tune_24),
                "customise",
                modifier = Modifier.size(50.dp)
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun HomeScreenPreview() {
//    HomeScreen(
//        navController = rememberNavController(),
////        viewModel = viewModel,
////        viewModel = AppViewModel()
//    )
//}



