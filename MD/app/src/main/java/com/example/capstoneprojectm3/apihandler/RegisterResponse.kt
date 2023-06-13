package com.example.capstoneprojectm3.apihandler

import com.google.gson.annotations.SerializedName

//Response:
//{
//    error: Boolean, // false | true
//    message: String, // “berhasil sign up” | “email harus valid”, “email telah terdaftar”
//}

data class RegisterResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)
