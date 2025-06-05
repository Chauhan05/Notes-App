package com.example.notesapp.screens

//package com.example.notesapp.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notesapp.viewmodels.AppViewModel
import com.example.notesapp.uistates.NoteUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
) {
    val uiState = viewModel.uiState.collectAsState()
    val isPasswordSwitchEnabled = viewModel.isPasswordSwitchEnabled.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
//    var isSaving by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Note", fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TopRowActions(uiState) {
                        viewModel.toggleIsStarred()
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f),
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val message = viewModel.onSaveClick()
                    if (message == "Entry Added") {
//                        isSaving = true
                        navController.popBackStack()
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save Note"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DateDisplay()

                Spacer(modifier = Modifier.height(16.dp))

                NoteContentCard(
                    title = uiState.value.title,
                    onTitleChange = { viewModel.updateTitle(it) },
                    description = uiState.value.description,
                    onDescriptionChange = { viewModel.updateDescription(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordSecurityCard(
                    isSwitchEnabled = isPasswordSwitchEnabled.value,
                    onToggle = { newValue -> viewModel.updatePasswordEnableSwitch(newValue) },
                    password = uiState.value.password,
                    onPasswordChange = { newValue -> viewModel.updatePassword(newValue) }
                )

                Spacer(modifier = Modifier.height(80.dp)) // Extra space at bottom for FAB
            }

            // Success animation
//            AnimatedVisibility(
//                visible = isSaving,
//                enter = fadeIn(),
//                exit = fadeOut(),
//                modifier = Modifier.fillMaxSize()
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.Black.copy(alpha = 0.5f)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Card(
//                        modifier = Modifier.size(100.dp),
//                        shape = RoundedCornerShape(16.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = MaterialTheme.colorScheme.primaryContainer
//                        )
//                    ) {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Rounded.Check,
//                                contentDescription = "Saved",
//                                tint = MaterialTheme.colorScheme.primary,
//                                modifier = Modifier.size(48.dp)
//                            )
//                        }
//                    }
//                }
//            }
        }
    }
}

@Composable
fun NoteContentCard(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Note Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            TitleTextField(
                value = title,
                onValueChange = onTitleChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            DescriptionTextField(
                value = description,
                onValueChange = onDescriptionChange
            )
        }
    }
}

@Composable
fun TitleTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Title") },
        placeholder = { Text("Enter note title...") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        singleLine = true
    )
}

@Composable
fun DescriptionTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Description") },
        placeholder = { Text("Enter note description...") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        minLines = 5,
        maxLines = 10
    )
}

@Composable
fun PasswordSecurityCard(
    isSwitchEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    password: String?,
    onPasswordChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSwitchEnabled)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Security",
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Security",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Password Protection",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Switch(
                    checked = isSwitchEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }

            AnimatedVisibility(
                visible = isSwitchEnabled,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password ?: "",
                        onValueChange = onPasswordChange,
                        label = { Text("Password") },
                        placeholder = { Text("Enter secure password...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "This note will be locked and require a password to view",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun DateDisplay() {
    val currentDate = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = "Date",
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = currentDate,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TopRowActions(uiState: State<NoteUiState>, toggleFav: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Favorite button
        IconButton(onClick = toggleFav) {
            Icon(
                imageVector = if (uiState.value.isStarred) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (uiState.value.isStarred) Color(0xFFE91E63) else MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        // Share button
        IconButton(onClick = { /* Share functionality */ }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share"
            )
        }

        // More options button
        IconButton(onClick = { /* More options */ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More Options"
            )
        }
    }
}

@Composable
fun IconCmp(icon: ImageVector, title: String, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}











//
//import android.widget.Toast
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.FavoriteBorder
//import androidx.compose.material.icons.filled.MoreHoriz
//import androidx.compose.material.icons.filled.Share
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Switch
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.State
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.example.notesapp.viewmodels.AppViewModel
//import com.example.notesapp.uistates.NoteUiState
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//
//@Composable
//fun AddNoteScreen(
//    navController: NavHostController,
//    viewModel: AppViewModel,
////    viewModel: AppViewModel=viewModel()
//) {
//
//    val uiState = viewModel.uiState.collectAsState()
//    val isPasswordSwitchEnabled = viewModel.isPasswordSwitchEnabled.collectAsState()
//    val context = LocalContext.current
//    Scaffold(
//        modifier = Modifier.fillMaxSize()
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .padding(innerPadding)
//                .padding(4.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            TopRowCmp(uiState) {
//                viewModel.toggleIsStarred()
//            }
//            DateCmp()
//            AddTitleCmp("Add Title", uiState.value.title) {
//                viewModel.updateTitle(it)
//            }
//            NoteDescriptionCmp("Add Description", uiState.value.description) {
//                viewModel.updateDescription(it)
//            }
//            PasswordCmp(
//                isSwitchEnabled = isPasswordSwitchEnabled.value,
//                onToggle = { newValue ->
//                    viewModel.updatePasswordEnableSwitch(newValue)
//                },
//                password = uiState.value.password,
//                onPasswordChange = { newValue ->
//                    viewModel.updatePassword(newValue)
//                }
//            )
//
//            SaveButton {
//                val message = viewModel.onSaveClick()
//                if (message == "Entry Added") navController.popBackStack()
//                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}
//
//@Composable
//fun PasswordCmp(
//    isSwitchEnabled: Boolean,
//    onToggle: (Boolean) -> Unit,
//    password: String?,
//    onPasswordChange: (String) -> Unit
//) {
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text("Enable Password")
//            Spacer(modifier = Modifier.width(16.dp))
//            Switch(
//                checked = isSwitchEnabled,
//                onCheckedChange = {
//                    onToggle(it)
//                }
//            )
//        }
//        if (isSwitchEnabled) {
//            OutlinedTextField(
//                value = password ?: "",
//                onValueChange = onPasswordChange,
//                label = {
//                    Text("Enter Password")
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//            )
//        }
//    }
//}
//
//
//@Composable
//fun SaveButton(
//    onClick: () -> Unit,
//) {
//    Button(
//        onClick = onClick,
//        modifier = Modifier.padding(8.dp),
//        shape = RoundedCornerShape(12.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant,
//            contentColor = Color.White
//        )
//    ) {
//        Text("Save", fontSize = 18.sp)
//    }
//}
//
//@Composable
//fun NoteDescriptionCmp(
//    label: String,
//    value: String,
//    onValueChange: (String) -> Unit,
//) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = {
//            Text(label, textAlign = TextAlign.Center)
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
//        shape = RoundedCornerShape(8.dp),
//        minLines = 10,
//        maxLines = 20
//    )
//
//}
//
//@Composable
//fun AddTitleCmp(
//    label: String,
//    value: String,
//    onValueChange: (String) -> Unit,
//) {
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.Center
//    ) {
//        OutlinedTextField(
//            value = value,
//            onValueChange = onValueChange,
//            label = {
//                Text(label)
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
//            shape = RoundedCornerShape(8.dp),
//        )
//    }
//}
//
//@Composable
//fun DateCmp() {
//    val currentDate = SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(Date())
//    Text(
//        text = currentDate,
//        style = MaterialTheme.typography.titleLarge.copy(color = Color.Gray.copy(0.7f)),
//        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
//    )
//}
//
//@Composable
//fun TopRowCmp(uiState: State<NoteUiState>, toggleFav: () -> Unit) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        IconCmp(Icons.Default.Close, "closeButton") {}
//        Spacer(modifier = Modifier.weight(1f))
//        IconCmp(Icons.Default.Share, "share") {}
//        IconCmp(
//            if (uiState.value.isStarred) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
//            "fav"
//        ) {
////            change the isStarred here
//            toggleFav()
//        }
//        IconCmp(Icons.Default.MoreHoriz, "more") {}
//    }
//}
//
//@Composable
//fun IconCmp(icon: ImageVector, title: String, onClick: () -> Unit) {
//    IconButton(
//        onClick = onClick, modifier = Modifier
//            .padding(16.dp)
//            .size(30.dp)
//    ) {
//        Icon(icon, title)
//    }
//}
//
//
////@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
////@Composable
////private fun AddNoteScreenPreview() {
////    AddNoteScreen(navController = rememberNavController(), viewModel = viewModel)
////}