package com.example.capstoneprojectm3.apihandler

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("usernameOrEmail") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("notes")
    fun getAllNotes(
        @Query("authToken") authToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Call<GetAllNotesResponse>

    @GET("notes/{id}")
    fun getNoteById(
        @Path("id") id: String,
        @Query("authToken") authToken: String,
        @Query("idNote") noteId: String,
    ): Call<GetNoteByIdResponse>

    @Multipart
    @POST("notes")
    fun createNote(
        @Part file: MultipartBody.Part,
        @Part("authToken") authToken: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody
    ): Call<CreateNoteResponse>

    @FormUrlEncoded
    @POST("notes/edit")
    fun editNote(
        @Field("authToken") authToken: String,
        @Field("noteId") noteId: String,
        @Field("title") title: String,
        @Field("date") date: String,
        @Field("description") description: String,
    ): Call<EditNoteResponse>

    @DELETE("notes/delete")
    fun deleteNote(
        @Query("authToken") authToken: String,
        @Query("idNote") noteId: String,
    ): Call<DeleteNoteResponse>
}