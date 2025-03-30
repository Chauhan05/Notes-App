package com.example.notesapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notesapp.AppViewModel
import com.example.notesapp.screens.AddNoteScreen
import com.example.notesapp.screens.HomeScreen
import com.example.notesapp.screens.UpdateNoteScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    navController: NavHostController
) {

    NavHost(navController, startDestination = Screens.HomeScreen.route) {
        composable(Screens.HomeScreen.route) {
            HomeScreen(navController,viewModel)
        }
        composable(Screens.AddNoteScreen.route) {
            AddNoteScreen(navController,viewModel)
        }
        composable(Screens.UpdateNoteScreen.route) {
            UpdateNoteScreen(navController,viewModel)
        }
    }

}