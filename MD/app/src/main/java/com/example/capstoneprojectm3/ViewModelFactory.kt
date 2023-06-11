package com.example.capstoneprojectm3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.ui.page.addnote.AddNoteViewModel
import com.example.capstoneprojectm3.ui.page.details.DetailsViewModel
import com.example.capstoneprojectm3.ui.page.home.HomeViewModel
import com.example.capstoneprojectm3.ui.page.login.LoginViewModel
import com.example.capstoneprojectm3.ui.page.signup.SignUpViewModel

class ViewModelFactory(private val repository: NoteRepository, private val preferences: DatastorePreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository, preferences) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository, preferences) as T
        } else if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(repository, preferences) as T
        } else if (modelClass.isAssignableFrom(AddNoteViewModel::class.java)) {
            return AddNoteViewModel(repository, preferences) as T
        } else if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(repository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}