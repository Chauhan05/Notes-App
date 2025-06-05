package com.example.notesapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notesapp.viewmodels.AppViewModel
import com.example.notesapp.screens.AddNoteScreen
import com.example.notesapp.screens.HomeScreen
import com.example.notesapp.screens.UpdateNoteScreen
import com.example.notesapp.screens.ViewNoteScreen

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
//        composable(Screens.UpdateNoteScreen.route) {backStackEntry->
//            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
//            if (noteId != null) {
//                UpdateNoteScreen(noteId, navController, viewModel)
//            }
//        }

        composable(
            route = "${Screens.UpdateNoteScreen.route}/{noteId}",
            arguments = listOf(navArgument("noteId"){
                type= NavType.IntType
                nullable=false
            })
        ) { backStackEntry ->
            val noteId = requireNotNull(backStackEntry.arguments?.getInt("noteId")) {
                "Note ID is required for UpdateNoteScreen"
            }
            UpdateNoteScreen(noteId, navController, viewModel)

        }


        composable(
            route = "${Screens.ViewNote.route}/{noteId}",
            arguments = listOf(navArgument("noteId"){
                type= NavType.IntType
                nullable=false
            })
        ) { backStackEntry ->
            val noteId = requireNotNull(backStackEntry.arguments?.getInt("noteId")) {
                "Note ID is required for UpdateNoteScreen"
            }
            ViewNoteScreen(noteId, navController, viewModel)

        }
    }

}