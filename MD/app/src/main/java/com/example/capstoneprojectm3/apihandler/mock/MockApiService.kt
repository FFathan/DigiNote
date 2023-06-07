package com.example.capstoneprojectm3.apihandler.mock

import com.example.capstoneprojectm3.apihandler.*
import com.example.capstoneprojectm3.ui.data.Note
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
        return GetAllNotesResponse(false, "getAllNotes succeed",
        listOf( Note(1, "Note 1", "08/06/2023 01:23:45", "description") )
        )
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
        return EditNoteResponse(false, "editNote succeed")
    }

    override suspend fun deleteNote(authToken: String, noteId: String): DeleteNoteResponse {
        return DeleteNoteResponse(false, "deleteNote succeed")
    }
}