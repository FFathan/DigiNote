package com.example.capstoneprojectm3.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login?username={username}") {
        fun with(username: String): String {
            return route.replace("{username}", username)
        }
    }
    object SignUp : Screen("signup")
    object Home: Screen("home")
    object Details: Screen("details?noteId={noteId}") {
        fun with(noteId: String): String {
            return route.replace("{noteId}", noteId)
        }
    }
    object AddNote: Screen("addnote")
}