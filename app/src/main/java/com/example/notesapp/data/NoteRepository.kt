package com.example.notesapp.data

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    suspend fun addANote(note: NoteEntity) {
        noteDao.addANote(note)
    }

    suspend fun deleteANote(wish: NoteEntity) {
        noteDao.deleteANote(wish)
    }

    suspend fun updateANote(wish: NoteEntity) {
        noteDao.updateANote(wish)
    }

    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    suspend fun getNoteById(id:Int): NoteEntity{
        return noteDao.getNoteById(id)
    }


}