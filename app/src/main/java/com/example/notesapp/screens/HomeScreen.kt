package com.example.notesapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.notesapp.viewmodels.AppViewModel
import com.example.notesapp.R
import com.example.notesapp.uistates.SearchBarCmp
import com.example.notesapp.data.NoteEntity
import com.example.notesapp.navigation.Screens
import androidx.core.graphics.toColorInt

val darkBackgroundColors = listOf(
    "#1E1E1E", // Jet Black - minimal, modern
    "#2C2C54", // Deep Indigo - cool and focused
    "#3E4A89", // Space Blue - elegant
    "#1A535C", // Teal Blue - calm and professional
    "#2E4053", // Charcoal Blue - business-like
    "#4B4453", // Dark Lavender - creative
    "#3C3F58", // Slate Purple - subtle depth
    "#263238", // Blue Grey - clean and modern
    "#37474F", // Dark Grey Blue - clean tech look
    "#3A3A3A", // Flat Charcoal - UI-friendly
    "#424242", // Grey 800 - Google's Material tone
    "#2D2D2D", // Almost Black - minimalist
    "#232931", // Deep Slate - modern dashboards
    "#1F2937", // Tailwind Slate-800 - modern UI feel
    "#2B2D42"  // Deep Night Blue - serious tone
)

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
//    viewModel: AppViewModel
) {
    val notesList = viewModel.getAllNotes.collectAsStateWithLifecycle(emptyList())


    val showAlertDialogBox = remember {
        mutableStateOf(false)
    }

//    entering password to unlock the note
    val showAlertForPassword = remember {
        mutableStateOf(false)
    }

//  alert to lock the note
    val setPasswordDialog = remember {
        mutableStateOf(false)
    }
    val selectedNote = remember { mutableStateOf<NoteEntity?>(null) }


    val selectedNoteForPassword = remember {
        mutableStateOf<NoteEntity?>(null)
    }

    val isSearchExpanded = remember {
        mutableStateOf(false)
    }


    val noteForSettingPassword = remember {
        mutableStateOf<NoteEntity?>(null)
    }


    val context = LocalContext.current

    val password = viewModel.passwordInAlert.collectAsState()

    val searchBarValue = viewModel.searchBarValue.collectAsStateWithLifecycle()

    val setPasswordValue = viewModel.setPasswordDialogValue.collectAsState()
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
            SearchBarCmp(
                query = searchBarValue.value,
                onValueChange = { viewModel.updateSearchBarValue(it) },
                isSearchBarExpanded = isSearchExpanded.value,
                updateSearchBarState = { isSearchExpanded.value = it },
                notes = notesList.value.filter {
                    (it.password == null || viewModel.isNoteUnlocked(it.id)) &&
                            (it.title.contains(searchBarValue.value, ignoreCase = true) ||
                                    it.description.contains(
                                        searchBarValue.value,
                                        ignoreCase = true
                                    ))
                },
            )
            Spacer(modifier = Modifier.height(8.dp))


            LazyColumn {
                items(notesList.value, key = { note -> note.id }) { note ->
                    val isUnlocked =
                        viewModel.unLockedNotes.collectAsState().value.contains(note.id)
                    NoteItem(
                        note = note,
                        isUnlocked = isUnlocked,
                        onFavClick = {
                            selectedNote.value = note
                            showAlertDialogBox.value = true
                        },
                        onUnlockClick = {
                            selectedNoteForPassword.value = note
                            showAlertForPassword.value = true
                        },
                        onLockClick = { viewModel.lockNote(note.id) },
                        setPasswordDialog = setPasswordDialog,
                        storeNoteForPassSet = noteForSettingPassword,
                        onEditButtonClick = {
                            navController.navigate(Screens.UpdateNoteScreen.withId(note.id))


                        },
                        onViewClick = {
                            navController.navigate(Screens.ViewNote.withId(note.id))
                        }
                    )


                }

            }
//            Alert to for entering the password to unlock the note
            if (showAlertForPassword.value) {
                PasswordDialog(
                    pass = password.value,
                    onValueChange = {
                        viewModel.onEnterPasswordChange(it)
                    },
                    onConfirm = {
//                        Check for the password
                        if (selectedNoteForPassword.value!!.password == password.value) {
                            viewModel.unlockNote(selectedNoteForPassword.value!!.id)
                            showAlertForPassword.value = false
                            Toast.makeText(
                                context,
                                "Note UnLocked",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Password Entered is InValid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        viewModel.onEnterPasswordChange("")
//                        password is correct show the note
//                        else show a toast that password is incorrect
                    },
                    onDismiss = {
                        showAlertForPassword.value = false
                    }
                )
            }
//            Alert to star the note
            if (showAlertDialogBox.value && selectedNote.value != null) {
//                            show the dialog box
                AlertDialogForStarCmp(
                    selectedNote.value!!.isStarred,
                    onConfirm = {
//                        Update the note
                        selectedNote.value =
                            selectedNote.value!!.copy(isStarred = !selectedNote.value!!.isStarred)
                        viewModel.updateANote(selectedNote.value!!)
//                        hide the dialog
                        showAlertDialogBox.value = false
                        Log.d("Main", notesList.value.toString())
                    },
                    onDismiss = {
                        showAlertDialogBox.value = false
                    }
                )
            }
//            Alert to lock the note
            if (setPasswordDialog.value) {
                SetPasswordDialog(
                    pass = setPasswordValue.value,
                    onValueChange = {
//                        onSetPasswordValueChange(it)
                        viewModel.updateSetPasswordDialogValue(it)
                    },
                    onConfirm = {
//                    update the transaction in the db
//                    with the current password
                        if (setPasswordValue.value.isNotBlank()) {
                            noteForSettingPassword.value = noteForSettingPassword.value!!.copy(
                                password = setPasswordValue.value
                            )
                            viewModel.updateANote(noteForSettingPassword.value!!)
                            setPasswordDialog.value = false
                            Toast.makeText(context, "Password Set", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context, "Enter Password", Toast.LENGTH_SHORT).show()
                        }
                        viewModel.updateSetPasswordDialogValue("")
                    },
                    onDismiss = {

                        setPasswordDialog.value = false
                    }
                )
            }
        }
    }


}

@Composable
fun PasswordDialog(
    pass: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = { Text("Enter Password") },
        text = {
            Column {
                Text(
                    "Enter the password to view",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = pass,
                    onValueChange = onValueChange,
                    label = { Text("Password") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Unlock")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun SetPasswordDialog(
    pass: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = { Text("Set Password") },
        text = {
            Column {
                Text(
                    "Enter the password",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = pass,
                    onValueChange = onValueChange,
                    label = { Text("Password") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Lock")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: NoteEntity,
    isUnlocked: Boolean,
    onFavClick: () -> Unit,
    onUnlockClick: () -> Unit,
    onLockClick: () -> Unit,
    setPasswordDialog: MutableState<Boolean>,
    storeNoteForPassSet: MutableState<NoteEntity?>,
    onEditButtonClick: (Int) -> Unit,
    onViewClick: () -> Unit,
) {
    val context = LocalContext.current
    // Common card settings for consistency
    val cardShape = RoundedCornerShape(16.dp)
    val cardElevation = 8.dp
    val cardHeight = 180.dp
    val randomColor = remember(note.id) {
        val colorIndex = note.id % darkBackgroundColors.size
        Color(darkBackgroundColors[colorIndex].toColorInt())
    }

    // First determine if we should show content (unlocked or no password)
    val showContent = isUnlocked || note.password == null

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .height(cardHeight)
            .clickable(
                enabled = showContent,
                onClick = {
                    onViewClick()
                }
            ),
        elevation = CardDefaults.cardElevation(cardElevation),
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Only show content if unlocked or not password-protected


            if (showContent) {
                // Content for unlocked notes
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(randomColor)
                        .padding(16.dp)
                ) {
                    // Header - Date and Favorite Icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = note.date,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
//                        To lock the note
                        IconButton(
                            onClick = {
//                                is password is set
                                if (note.password != null) {
                                    onLockClick()
                                } else {
//                                    Alert to enter the password
                                    storeNoteForPassSet.value = note
                                    setPasswordDialog.value = true
//                                    update in the db


                                }
//                                if password is not set
                            }
                        ) {
                            Icon(Icons.Default.LockOpen, "lockOpen")
                        }
//                        To add or remove favorite
                        IconButton(onClick = onFavClick) {
                            Icon(
                                imageVector = if (note.isStarred) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (note.isStarred) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
//                        To Edit note
                        IconButton(
                            onClick = {
                                onEditButtonClick(note.id)
                            }
                        ) {
                            Icon(Icons.Default.Edit, "editButton")
                        }

                    }

                    // Title
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Description
                    Text(
                        text = note.description,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .weight(1f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Time
                    Text(
                        text = note.time,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 8.dp)
                    )
                }
            } else {
                // Content for locked notes - show date/time but hide title/description
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    // Date and time information displayed at top
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Header with date
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = note.date,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // If note is starred, show indicator
                            if (note.isStarred) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Favorite note",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Time shown at bottom right
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = note.time,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = 8.dp)
                            )
                        }
                    }

                    // Lock icon that acts as a button
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                            .clickable(onClick = onUnlockClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Tap to unlock",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // "Locked" text indicator
                    Text(
                        text = "Protected Note",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogForStarCmp(isStarred: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {
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



