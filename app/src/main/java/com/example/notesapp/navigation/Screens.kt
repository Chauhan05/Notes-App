package com.example.notesapp.navigation

sealed class Screens (val route:String){
    object HomeScreen: Screens("home")
    object AddNoteScreen:Screens("add")
    object UpdateNoteScreen:Screens("update")
}