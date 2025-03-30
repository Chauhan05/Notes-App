package com.example.notesapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.notesapp.AppViewModel
import com.example.notesapp.NoteUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun AddNoteScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
//    viewModel: AppViewModel=viewModel()
) {

    val uiState = viewModel.uiState.collectAsState()
    val isPasswordSwitchEnabled = viewModel.isPasswordSwitchEnabled.collectAsState()
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopRowCmp(uiState) {
                viewModel.toggleIsStarred()
            }
            DateCmp()
            AddTitleCmp("Add Title", uiState.value.title) {
                viewModel.updateTitle(it)
            }
            NoteDescriptionCmp("Add Description", uiState.value.description) {
                viewModel.updateDescription(it)
            }
            PasswordCmp(
                isSwitchEnabled = isPasswordSwitchEnabled.value,
                onToggle = { newValue ->
                    viewModel.updatePasswordEnableSwitch(newValue)
                },
                password = uiState.value.password,
                onPasswordChange = { newValue ->
                    viewModel.updatePassword(newValue)
                }
            )

            SaveButton {
                val message = viewModel.onSaveClick()
                if (message == "Entry Added") navController.popBackStack()
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun PasswordCmp(
    isSwitchEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    password: String?,
    onPasswordChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Enable Password")
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = isSwitchEnabled,
                onCheckedChange = {
                    onToggle(it)
                }
            )
        }
        if (isSwitchEnabled) {
            OutlinedTextField(
                value = password ?: "",
                onValueChange = onPasswordChange,
                label = {
                    Text("Enter Password")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}


@Composable
fun SaveButton(
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = Color.White
        )
    ) {
        Text("Save", fontSize = 18.sp)
    }
}

@Composable
fun NoteDescriptionCmp(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label, textAlign = TextAlign.Center)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
        shape = RoundedCornerShape(8.dp),
        minLines = 10,
        maxLines = 20
    )

}

@Composable
fun AddTitleCmp(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(label)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
        )
    }
}

@Composable
fun DateCmp() {
    val currentDate = SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(Date())
    Text(
        text = currentDate,
        style = MaterialTheme.typography.titleLarge.copy(color = Color.Gray.copy(0.7f)),
        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
    )
}

@Composable
fun TopRowCmp(uiState: State<NoteUiState>, toggleFav: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconCmp(Icons.Default.Close, "closeButton") {}
        Spacer(modifier = Modifier.weight(1f))
        IconCmp(Icons.Default.Share, "share") {}
        IconCmp(
            if (uiState.value.isStarred) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            "fav"
        ) {
//            change the isStarred here
            toggleFav()
        }
        IconCmp(Icons.Default.MoreHoriz, "more") {}
    }
}

@Composable
fun IconCmp(icon: ImageVector, title: String, onClick: () -> Unit) {
    IconButton(
        onClick = onClick, modifier = Modifier
            .padding(16.dp)
            .size(30.dp)
    ) {
        Icon(icon, title)
    }
}


//@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun AddNoteScreenPreview() {
//    AddNoteScreen(navController = rememberNavController(), viewModel = viewModel)
//}