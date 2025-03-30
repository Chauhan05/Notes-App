package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.navigation.AppNavigation
import com.example.notesapp.ui.theme.NotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AppViewModel= viewModel()
            val navController= rememberNavController()
            NotesAppTheme(darkTheme = true) {
                AppNavigation(viewModel=viewModel, navController = navController)
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
////                    AppNavigation(modifier=Modifier.padding(innerPadding), viewModel = viewModel, navController =navController )
//                }
            }
        }
    }
}

