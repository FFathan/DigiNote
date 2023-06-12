package com.example.capstoneprojectm3.ui.page.signup

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.ui.page.login.LoginUiState
import com.example.capstoneprojectm3.utils.extractMessageFromJson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(private val repository: NoteRepository, private val preferences: DatastorePreferences) : ViewModel() {
    private val _uiState: MutableStateFlow<SignUpUiState> =
        MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState>
        get() = _uiState

    fun signUp(username: String, email: String, password: String, context: Context, onNavigateToLogin: (username: String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = SignUpUiState(isLoading = true)
            try {
                val signUpResponse = repository.signUp(username, email, password)
                val isSignedUp = !signUpResponse.error
                if(isSignedUp) {
                    Toast.makeText(context, "Sign Up Succeed", Toast.LENGTH_LONG).show()
                    onNavigateToLogin(username)
                }
                _uiState.value = SignUpUiState(isLoading = false, isSuccess = true)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Toast.makeText(context, "Sign Up Failed: ${errorBody?.extractMessageFromJson()}", Toast.LENGTH_LONG).show()
                _uiState.value = SignUpUiState(isLoading = false, isFailed = true)
            }
        }
    }
}
data class SignUpUiState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false,
)