package com.example.capstoneprojectm3.di

import com.example.capstoneprojectm3.data.NoteRepository

object Injection {
    fun provideRepository(): NoteRepository {
        return NoteRepository.getInstance()
    }
}