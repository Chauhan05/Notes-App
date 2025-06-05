package com.example.notesapp.navigation

sealed class Screens (val route:String){
    object HomeScreen: Screens("home")
    object AddNoteScreen:Screens("add")
    object  UpdateNoteScreen:Screens("update"){
        fun withId(noteId:Int)="update/$noteId"
    }

    object ViewNote: Screens("view"){
        fun withId(noteId:Int)="view/$noteId"
    }
}