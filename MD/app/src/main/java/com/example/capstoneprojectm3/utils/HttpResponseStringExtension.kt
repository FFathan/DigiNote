package com.example.capstoneprojectm3.utils

fun String.extractMessageFromJson(): String {
    val keyValuePairs = this.trim().removePrefix("{").removeSuffix("}").split(",")
    val jsonObject = mutableMapOf<String, String>()

    for (pair in keyValuePairs) {
        val (key, value) = pair.split(":")
        val cleanedKey = key.trim().removeSurrounding("\"")
        val cleanedValue = value.trim().removeSurrounding("\"")
        jsonObject[cleanedKey] = cleanedValue
    }

    return jsonObject["message"] ?: ""
}