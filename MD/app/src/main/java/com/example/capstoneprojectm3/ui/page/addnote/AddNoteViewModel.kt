package com.example.capstoneprojectm3.ui.page.addnote

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.utils.extractMessageFromJson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AddNoteViewModel(private val repository: NoteRepository, private val preferences: DatastorePreferences) : ViewModel() {
    fun addNote(context: Context, onNavigateToDetails: () -> Unit) {
        onNavigateToDetails()
    }

    fun login(username: String, password: String, context: Context, onNavigateToHome: () -> Unit) {
        viewModelScope.launch {
//            val loginResponse = repository.mockLogin(username, password)
            try {
                val loginResponse = repository.login(username, password)
                val isLoggedIn = !loginResponse.error
                val authToken = loginResponse.authToken
                if(isLoggedIn) {
                    preferences.setLoginStatus(true)
                    preferences.setAuthToken(authToken)
                    onNavigateToHome()
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Toast.makeText(context, "Login Failed: ${errorBody?.extractMessageFromJson()}", Toast.LENGTH_LONG).show()
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