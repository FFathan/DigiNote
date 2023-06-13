package com.example.capstoneprojectm3.apihandler

import com.google.gson.annotations.SerializedName

data class GetNoteByIdResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("noteId")
    val noteId: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("description")
    val description: String,
)