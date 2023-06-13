package com.example.capstoneprojectm3.apihandler

import com.google.gson.annotations.SerializedName

data class DeleteNoteResponse (
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)