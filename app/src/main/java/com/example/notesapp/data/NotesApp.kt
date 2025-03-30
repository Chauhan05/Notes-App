package com.example.notesapp.data

import android.app.Application

class NotesApp :Application() {
    //    Providing the context to the application
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}