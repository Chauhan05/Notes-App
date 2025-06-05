import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notesapp.navigation.Screens
import com.example.notesapp.screens.AddNoteScreen
import com.example.notesapp.screens.HomeScreen
import com.example.notesapp.screens.UpdateNoteScreen
import com.example.notesapp.screens.ViewNoteScreen
import com.example.notesapp.viewmodels.AppViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    navController: NavHostController
) {
    val duration = 800 // shorter, snappier animation
    val easing = androidx.compose.animation.core.FastOutSlowInEasing

    val enterForward: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(durationMillis = duration, easing = easing)
        ) + fadeIn(animationSpec = tween(durationMillis = duration, easing = easing))
    }

    val exitForward: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(durationMillis = duration, easing = easing)
        ) + fadeOut(animationSpec = tween(durationMillis = duration, easing = easing))
    }

    val enterBackward: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(durationMillis = duration, easing = easing)
        ) + fadeIn(animationSpec = tween(durationMillis = duration, easing = easing))
    }

    val exitBackward: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(durationMillis = duration, easing = easing)
        ) + fadeOut(animationSpec = tween(durationMillis = duration, easing = easing))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        NavHost(navController, startDestination = Screens.HomeScreen.route) {
            composable(
                Screens.HomeScreen.route,
                enterTransition = { enterBackward() },
                exitTransition = { exitForward() }
            ) {
                HomeScreen(navController, viewModel)
            }
            composable(
                Screens.AddNoteScreen.route,
                enterTransition = { enterForward() },
                exitTransition = { exitBackward() }
            ) {
                AddNoteScreen(navController, viewModel)
            }
            composable(
                route = "${Screens.UpdateNoteScreen.route}/{noteId}",
                arguments = listOf(navArgument("noteId") {
                    type = NavType.IntType
                    nullable = false
                }),
                enterTransition = { enterForward() },
                exitTransition = { exitBackward() }
            ) { backStackEntry ->
                val noteId = requireNotNull(backStackEntry.arguments?.getInt("noteId")) {
                    "Note ID is required for UpdateNoteScreen"
                }
                UpdateNoteScreen(noteId, navController, viewModel)
            }

            composable(
                route = "${Screens.ViewNote.route}/{noteId}",
                arguments = listOf(navArgument("noteId") {
                    type = NavType.IntType
                    nullable = false
                }),
                enterTransition = { enterForward() },
                exitTransition = { exitBackward() }
            ) { backStackEntry ->
                val noteId = requireNotNull(backStackEntry.arguments?.getInt("noteId")) {
                    "Note ID is required for ViewNoteScreen"
                }
                ViewNoteScreen(noteId, navController, viewModel)
            }
        }
    }
}
