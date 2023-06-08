package com.example.capstoneprojectm3.ui.page.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.ui.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: NoteRepository, private val preferences: DatastorePreferences) : ViewModel() {
//    private val _uiState: MutableStateFlow<LoginUiState> =
//        MutableStateFlow(LoginUiState())
//    val uiState: StateFlow<LoginUiState>
//        get() = _uiState

    fun login(username: String, password: String, onNavigateToHome: () -> Unit) {
        viewModelScope.launch {
            val loginResponse = repository.mockLogin(username, password)
            val isLoggedIn = !loginResponse.error
            val authToken = loginResponse.authToken
            if(isLoggedIn) {
                preferences.setLoginStatus(true)
                preferences.setAuthToken(authToken)
                onNavigateToHome()
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

//data class LoginUiState (
//    val isLoading: Boolean = true,
//    val isSuccess: Boolean = false,
//    val isFailed: Boolean = false,
//    val isLoggedIn: Boolean = false,
//    val authToken: String = ""
//)