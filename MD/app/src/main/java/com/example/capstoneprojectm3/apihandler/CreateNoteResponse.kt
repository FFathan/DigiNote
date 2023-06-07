package com.example.capstoneprojectm3.apihandler

import com.google.gson.annotations.SerializedName

data class CreateNoteResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
