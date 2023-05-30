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
import com.example.capstoneprojectm3.ui.page.*
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
                getHomeNoteListExample(),
                onNavigateToDetails = { navController.navigate(Screen.Details.route) },
                onNavigateToAddNote = { navController.navigate(Screen.AddNote.route) }
            )
        }
        composable(Screen.Details.route) {
            Details(getDetailsNoteExample())
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

fun getHomeNoteListExample(): List<Note> {
    val title = "Note Title"
    val date = "DD/MM/YYYY 12:34:56"
    val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor"
    val listNoteExample = mutableListOf<Note>()
    for (id in 1..100) {
        val note = Note(
            id,
            "$title $id",
            date,
            description
        )
        listNoteExample.add(note)
    }
    return listNoteExample
}

fun getDetailsNoteExample(): Note {
    val noteId = 1
    val title = "Note Title 1"
    val date = "DD/MM/YYYY 12:34:56"
    val description =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor"
    return Note(
        noteId,
        title,
        date,
        description
    )
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