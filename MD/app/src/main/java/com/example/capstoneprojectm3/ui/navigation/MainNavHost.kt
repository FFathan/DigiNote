package com.example.capstoneprojectm3.ui.navigation

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.capstoneprojectm3.ui.data.Note
import com.example.capstoneprojectm3.ui.page.addnote.AddNote
import com.example.capstoneprojectm3.ui.page.details.Details
import com.example.capstoneprojectm3.ui.page.home.Home
import com.example.capstoneprojectm3.ui.page.login.Login
import com.example.capstoneprojectm3.ui.page.signup.SignUp
import com.example.capstoneprojectm3.ui.theme.CapstoneProjectM3Theme

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            Screen.Login.route,
            arguments = listOf(navArgument("username") { defaultValue = "" })
        ) { backStackEntry ->
            Login(
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                onNavigateToHome = { navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                } },
                justSignedUpUsername = backStackEntry.arguments?.getString("username") as String
            )
        }
        composable(Screen.SignUp.route) {
            SignUp(
                onNavigateToLogin = { username ->
                    navController.navigate(Screen.Login.with(username)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            Home(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                } },
                onNavigateToDetails = { noteId ->
                    navController.navigate(Screen.Details.with(noteId))
                },
                onNavigateToAddNote = { navController.navigate(Screen.AddNote.route) }
            )
        }
        composable(
            Screen.Details.route,
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) { backStackEntry ->
            Details(
                backStackEntry.arguments?.getString("noteId") as String,
                onNavigateToHome = { navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true}
                } },
                onDeleteNote = { navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true}
                } },
            )
        }
        composable(Screen.AddNote.route) {
            AddNote(
                onNavigateToHome = { navController.navigate(Screen.Home.route){
                    popUpTo(Screen.Home.route) { inclusive = true }
                } },
                onNavigateToDetails = { navController.navigate(Screen.Details.route){
                    popUpTo(Screen.Home.route)
                } }
            )
        }
    }
}

@Preview(
    name = "MainNavHost",
    widthDp = 360,
    heightDp = 640,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun MainNavHostPreview() {
    CapstoneProjectM3Theme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MainNavHost()
        }
    }
}