package com.example.capstoneprojectm3.data

import android.util.Log
import com.example.capstoneprojectm3.apihandler.*
import com.example.capstoneprojectm3.ui.data.Note
import com.example.capstoneprojectm3.utils.extractMessageFromJson
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    suspend fun getAllNotes() {
//        delay(2000)
        homeNoteList = apiService.getAllNotes().noteList
        Log.d("getAllNotes()", "noteList: $homeNoteList")
        isHomeRequireUpdate = false
    }

    suspend fun addNote(imagePart: MultipartBody.Part, title: RequestBody): CreateNoteResponse {
        return apiService.createNote(imagePart, title)
    }

    fun addLocalHomeNote(note: Note) {
        homeNoteList = listOf(note) + homeNoteList
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

    suspend fun mockUpdateNote(authToken: String, noteId: String, title: String, date:String, description: String): UpdatedNoteResponse {
        val updatedNote = UpdatedNote(noteId, "authToken", title, description, "updated at: ")

        var updateResponse: EditNoteResponse
        return try{
            updateResponse = ApiConfig.mockGetApiService().editNote(authToken, noteId, title, date, description)
            updateLocalHomeNoteById(noteId, title, description)
            UpdatedNoteResponse(false, "message", updatedNote)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            updateResponse = EditNoteResponse(true, "Http Error: ${errorBody?.extractMessageFromJson()}", Note("", "", "", "", "", "",))
            UpdatedNoteResponse(true, "message", UpdatedNote("", "", "", "", ""))
        }
    }

    suspend fun updateNote(noteId: String, title: String, description: String): EditNoteResponse {
        var updateResponse: EditNoteResponse
        return try{
            updateResponse = apiService.editNote(noteId, title, description)
            updateLocalHomeNoteById(noteId, title, description)
            updateResponse
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            updateResponse = EditNoteResponse(true, "Http Error: ${errorBody?.extractMessageFromJson()}", Note("", "", "", "", "", "",))
            updateResponse
        } catch (e: Exception) {
            updateResponse = EditNoteResponse(true, "Update Note Failed", Note("", "", "", "", "", "",))
            updateResponse
        }
    }

    data class UpdatedNoteResponse(val error: Boolean, val message: String, val updatedNote: UpdatedNote)
    data class UpdatedNote(val noteId: String, val userId: String, val title: String, val description: String, val updated:String)

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

    private fun updateLocalHomeNoteById(noteId: String, title: String, description: String) {
        var updatedNote = homeNoteList.find { it.noteId == noteId }
        val updatedNoteIndex = homeNoteList.indexOf(updatedNote)
        updatedNote = updatedNote?.copy(title = title, description = description)

        if(updatedNote != null){
            homeNoteList = homeNoteList.toMutableList().apply {
                set(updatedNoteIndex, updatedNote)
            }
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