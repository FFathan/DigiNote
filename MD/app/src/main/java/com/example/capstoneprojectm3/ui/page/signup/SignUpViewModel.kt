package com.example.capstoneprojectm3.ui.page.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.data.NoteRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: NoteRepository, private val preferences: DatastorePreferences) : ViewModel() {
    fun signUp(username: String, email: String, password: String, onNavigateToLogin: (username: String) -> Unit) {
        viewModelScope.launch {
            val signUpResponse = repository.mockSignUp(username, email, password)
            val isSignedUp = !signUpResponse.error
            if(isSignedUp) {
                onNavigateToLogin(username)
            }
        }
    }

    fun navigateIfHasLoggedInBefore(onNavigateToHome: () -> Unit) {
        viewModelScope.launch {
            preferences.getLoginStatus().collect { isLoggedIn ->
                if(isLoggedIn) onNavigateToHome()
            }
        }
    }
}