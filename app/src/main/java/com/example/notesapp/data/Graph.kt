package com.example.notesapp.data

import android.content.Context
import androidx.room.Room

object Graph {

    lateinit var database: NoteDatabase


    //    Only initialized when required not at the start of the app (by lazy)
    val noteRepository by lazy {
        NoteRepository(database.noteDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(
            context = context,
            klass = NoteDatabase::class.java,
            name = "notes.db"
        ).build()
    }
}