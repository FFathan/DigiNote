package com.example.capstoneprojectm3.data

import com.example.capstoneprojectm3.apihandler.*
import com.example.capstoneprojectm3.apihandler.mock.MockApiService
import com.example.capstoneprojectm3.ui.data.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NoteRepository {
    private var apiService: ApiService = ApiConfig.mockGetApiService()
    var isAuthorized: Boolean = false
    fun getDummyListNote(): Flow<List<Note>> {
        return flowOf(getHomeNoteListExample())
    }

    fun authorizeApiService(authToken: String) {
        apiService = ApiConfig.mockGetApiService(authToken)
        isAuthorized = true
    }

    suspend fun mockSignUp(username: String, email: String, password: String): RegisterResponse {
        return apiService.register(username, email, password)
    }

    suspend fun mockLogin(username: String, password: String): LoginResponse {
        return apiService.login(username, password)
    }

    suspend fun mockGetAllNotes(authToken: String, page: Int, size: Int): Flow<List<Note>> {
        return flowOf(apiService.getAllNotes(authToken, page, size).noteList)
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

fun getHomeNoteListExample(): List<Note> {
    val title = "Note Title"
    val date = "DD/MM/YYYY 12:34:56"
    val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor"
    val listNoteExample = mutableListOf<Note>()
    for (id in 1..100) {
        val note = Note(
            id,
            "$title $id",
            date,
            description
        )
        listNoteExample.add(note)
    }
    return listNoteExample
}