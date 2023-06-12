package com.example.capstoneprojectm3.apihandler.mock

import com.example.capstoneprojectm3.apihandler.*
import com.example.capstoneprojectm3.ui.data.Note
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.random.Random

class MockApiService : ApiService {
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): RegisterResponse {
        return RegisterResponse(false, "register succeed")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return LoginResponse(false, "login succeed", "auth-token")
    }

    override suspend fun getAllNotes(authToken: String, page: Int, size: Int): GetAllNotesResponse {
        val mockNoteList = getMockHomeNoteList()
//        delay(2000)
        return GetAllNotesResponse(false, "getAllNotes succeed", mockNoteList)
    }

    override suspend fun getNoteById(
        id: String,
        authToken: String,
        noteId: String
    ): GetNoteByIdResponse {
        return GetNoteByIdResponse(false, "getNoteById succeed", "1", "Note 1", "08/06/2023 01:23:45", "description")
    }

    override suspend fun createNote(
        file: MultipartBody.Part,
        authToken: RequestBody,
        title: RequestBody,
        description: RequestBody
    ): CreateNoteResponse {
        return CreateNoteResponse(false, "createNote succeed")
    }

    override suspend fun editNote(
        authToken: String,
        noteId: String,
        title: String,
        date: String,
        description: String
    ): EditNoteResponse {
        delay(1000)
        return EditNoteResponse(false, "editNote succeed")
    }

    override suspend fun deleteNote(authToken: String, noteId: String): DeleteNoteResponse {
        return DeleteNoteResponse(false, "deleteNote succeed")
    }

    private fun getMockHomeNoteList(): List<Note> {
        val title = "Note Title"
        val date = "DD/MM/YYYY 12:34:56"
        val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor"


        val listNoteExample = mutableListOf<Note>()
        for (id in 1..100) {
            val endIndex = Random.nextInt(60, 100)
            val descriptionVariation = description.substring(0, endIndex)

            val note = Note(
                id.toString(),
                "$title $id",
                date,
                descriptionVariation
            )
            listNoteExample.add(note)
        }
        return listNoteExample
    }
}



