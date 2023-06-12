package com.example.capstoneprojectm3.data

import android.util.Log
import com.example.capstoneprojectm3.apihandler.*
import com.example.capstoneprojectm3.ui.data.Note
import com.example.capstoneprojectm3.utils.extractMessageFromJson
import retrofit2.HttpException

class NoteRepository {
    private var apiService: ApiService = ApiConfig.getApiService()
    var isAuthorized: Boolean = false
    var isHomeRequireUpdate: Boolean = true
    var homeNoteList = listOf<Note>()

    fun authorizeApiService(authToken: String) {
        apiService = ApiConfig.getApiService(authToken)
        isAuthorized = true
        Log.d("NoteRepository", "authorizeApiService: $authToken")
    }

    fun unauthorizeApiService() {
        apiService = ApiConfig.getApiService()
        isAuthorized = false
    }

    suspend fun login(username: String, password: String): LoginResponse {
        return apiService.login(username, password)
    }

    suspend fun signUp(username: String, email: String, password: String): RegisterResponse {
        return apiService.register(username, email, password)
    }

    suspend fun mockSignUp(username: String, email: String, password: String): RegisterResponse {
        return apiService.register(username, email, password)
    }

    suspend fun mockLogin(username: String, password: String): LoginResponse {
        return apiService.login(username, password)
    }

    suspend fun mockGetAllNotes(authToken: String, page: Int, size: Int) {
        homeNoteList = ApiConfig.mockGetApiService().getAllNotes(authToken, page, size).noteList
        isHomeRequireUpdate = false
    }

    suspend fun mockDeleteNote(authToken: String, noteId: String): DeleteNoteResponse {
        var deleteResponse: DeleteNoteResponse
        return try{
            deleteResponse = ApiConfig.mockGetApiService().deleteNote(authToken, noteId)
            deleteLocalHomeNoteById(noteId)
            deleteResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()

            deleteResponse = DeleteNoteResponse(true, "Http Error: ${errorBody?.extractMessageFromJson()}")
            deleteResponse
        }
    }

    private fun deleteLocalHomeNoteById(noteId: String) {
        homeNoteList = homeNoteList.filter { it.noteId != noteId }
    }

    companion object {
        @Volatile
        private var instance: NoteRepository? = null

        fun getInstance(): NoteRepository =
            instance ?: synchronized(this) {
                NoteRepository().apply {
                    instance = this
                }
            }
    }
}