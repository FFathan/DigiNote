package com.example.capstoneprojectm3.apihandler

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("usernameORemail") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("notes")
    suspend fun getAllNotes(
        @Query("authToken") authToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): GetAllNotesResponse

    @GET("notes")
    suspend fun getAllNotes(): GetAllNotesResponse

    @GET("notes/{id}")
    suspend fun getNoteById(
        @Path("id") id: String,
        @Query("authToken") authToken: String,
        @Query("idNote") noteId: String,
    ): GetNoteByIdResponse

    @Multipart
    @POST("notes")
    suspend fun createNote(
        @Part image: MultipartBody.Part,
        @Part("title") title: RequestBody,
    ): CreateNoteResponse

    @Multipart
    @POST("notes")
    suspend fun createNote(
        @Part image: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
    ): CreateNoteResponse

    @Multipart
    @POST("predict")
    suspend fun getDescription(
        @Part image: MultipartBody.Part,
    ): GetDescriptionResponse

    data class GetDescriptionResponse(
        @field:SerializedName("predicted_paragraph")
        val description: String
    )

    @FormUrlEncoded
    @POST("notes/edit")
    suspend fun editNote(
        @Field("authToken") authToken: String,
        @Field("noteId") noteId: String,
        @Field("title") title: String,
        @Field("date") date: String,
        @Field("description") description: String,
    ): EditNoteResponse

    @FormUrlEncoded
    @POST("notes/edit/{noteId}")
    suspend fun editNote(
        @Path("noteId") noteId: String,
        @Field("title") title: String,
        @Field("description") description: String,
    ): EditNoteResponse

    @DELETE("notes/delete")
    suspend fun deleteNote(
        @Query("authToken") authToken: String,
        @Query("idNote") noteId: String,
    ): DeleteNoteResponse

    @DELETE("notes/delete/{noteId}")
    suspend fun deleteNote(
        @Path("noteId") noteId: String,
    ): DeleteNoteResponse
}