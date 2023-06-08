package com.example.capstoneprojectm3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneprojectm3.data.NoteRepository
import com.example.capstoneprojectm3.ui.page.home.HomeViewModel
import com.example.capstoneprojectm3.ui.page.login.LoginViewModel

class ViewModelFactory(private val repository: NoteRepository, private val preferences: DatastorePreferences? = null) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository, preferences as DatastorePreferences) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository, preferences as DatastorePreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}