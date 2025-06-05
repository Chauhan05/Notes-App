package com.example.notesapp

import AppNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.ui.theme.NotesAppTheme
import com.example.notesapp.viewmodels.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AppViewModel = viewModel()
            val navController = rememberNavController()
            NotesAppTheme(darkTheme = true) {
                AppNavigation(viewModel = viewModel, navController = navController)
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
////                    AppNavigation(modifier=Modifier.padding(innerPadding), viewModel = viewModel, navController =navController )
//                }
            }
        }
    }
}

