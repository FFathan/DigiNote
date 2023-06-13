package com.example.capstoneprojectm3.ui.page.addnote

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneprojectm3.DatastorePreferences
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.utils.extractMessageFromJson
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.InputStream

class AddNoteViewModel(private val repository: NoteRepository, private val preferences: DatastorePreferences) : ViewModel() {
    fun addNote(imageUri: Uri, title: String, context: Context, onNavigateToDetails: () -> Unit) {
        viewModelScope.launch {
            try {
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(imageUri)
                val file = File(context.externalCacheDir, "image.jpg")
                file.outputStream().use { output ->
                    inputStream?.copyTo(output)
                }
//                inputStream?.copyInputStreamToFile(file) // Utility function to copy InputStream to File

                val requestTitle = title.toRequestBody("text/plain".toMediaType())
                val requestFile = file.asRequestBody("image/jpg".toMediaType())
                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

                val noteResponse = repository.addNote(
                    imagePart,
                    requestTitle
                )
                val isNoteAdded = !noteResponse.error
                if(isNoteAdded) {
                    Toast.makeText(context, "Note Added", Toast.LENGTH_LONG).show()
                    repository.addLocalHomeNote(noteResponse.note)
                    onNavigateToDetails()
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Toast.makeText(context, "Note Added Failed: ${errorBody?.extractMessageFromJson()}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun InputStream.copyInputStreamToFile(file: File) {
        use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
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