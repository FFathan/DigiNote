package com.example.capstoneprojectm3.data

import com.example.capstoneprojectm3.apihandler.*
import com.example.capstoneprojectm3.apihandler.mock.MockApiService
import com.example.capstoneprojectm3.ui.data.Note
import com.example.capstoneprojectm3.utils.extractMessageFromJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import retrofit2.HttpException

class NoteRepository {
    private var apiService: ApiService = ApiConfig.getApiService()
    var isAuthorized: Boolean = false
    var isHomeRequireUpdate: Boolean = true
    var homeNoteList = flowOf(listOf<Note>())

    fun authorizeApiService(authToken: String) {
        apiService = ApiConfig.mockGetApiService(authToken)
        isAuthorized = true
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
        homeNoteList = flowOf(ApiConfig.mockGetApiService().getAllNotes(authToken, page, size).noteList)
        isHomeRequireUpdate = false
//        return flowOf(ApiConfig.mockGetApiService().getAllNotes(authToken, page, size).noteList)
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
        homeNoteList = homeNoteList.map { noteList ->
            noteList.filter { it.noteId != noteId }
        }
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