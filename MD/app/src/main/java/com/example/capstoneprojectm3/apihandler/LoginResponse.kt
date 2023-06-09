package com.example.capstoneprojectm3.apihandler

import com.google.gson.annotations.SerializedName

//Response:
//{
//    error: Boolean, // false | true
//    message: String, // “berhasil sign up” | “email harus valid”, “email telah terdaftar”
//    authToken: String
//}

data class LoginResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("token")
    val authToken: String
)
