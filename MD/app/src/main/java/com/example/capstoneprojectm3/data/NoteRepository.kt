package com.example.capstoneprojectm3.data

import com.example.capstoneprojectm3.ui.data.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NoteRepository {
    fun getDummyListNote(): Flow<List<Note>> {
        return flowOf(getHomeNoteListExample())
    }

    companion object {
        @Volatile
        private var instance: NoteRepository? = null

        fun getInstance(): NoteRepository =
            instance ?: synchronized(this) {
                NoteRepository().apply {
                    instance = this
                }
            }
    }
}

fun getHomeNoteListExample(): List<Note> {
    val title = "Note Title"
    val date = "DD/MM/YYYY 12:34:56"
    val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor"
    val listNoteExample = mutableListOf<Note>()
    for (id in 1..100) {
        val note = Note(
            id,
            "$title $id",
            date,
            description
        )
        listNoteExample.add(note)
    }
    return listNoteExample
}