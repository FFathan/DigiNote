package com.example.capstoneprojectm3.apihandler

import com.example.capstoneprojectm3.ui.data.Note
import com.google.gson.annotations.SerializedName

data class GetAllNotesResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listnote")
    val noteList: List<Note>,
)