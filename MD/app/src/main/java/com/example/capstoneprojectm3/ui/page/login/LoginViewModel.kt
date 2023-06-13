package com.example.capstoneprojectm3.ui.page.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.data.NoteRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.example.capstoneprojectm3.utils.extractMessageFromJson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(private val repository: NoteRepository, private val preferences: DatastorePreferences) : ViewModel() {
    private val _uiState: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState>
        get() = _uiState

    fun login(username: String, password: String, context: Context, onNavigateToHome: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)
//            val loginResponse = repository.mockLogin(username, password)
            try {
                val loginResponse = repository.login(username, password)
                val isLoggedIn = !loginResponse.error
                val authToken = loginResponse.authToken
                if(isLoggedIn) {
                    preferences.setLoginStatus(true)
                    preferences.setAuthToken(authToken)
                    onNavigateToHome()
                    _uiState.value = LoginUiState(isLoading = false, isSuccess = true)
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Toast.makeText(context, "Login Failed: ${errorBody?.extractMessageFromJson()}", Toast.LENGTH_LONG).show()
                _uiState.value = LoginUiState(isLoading = false, isFailed = true)
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

data class LoginUiState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false,
)