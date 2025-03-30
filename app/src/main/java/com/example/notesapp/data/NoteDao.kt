package com.example.notesapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
abstract class NoteDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addANote(note: NoteEntity)

    @Update
    abstract suspend fun updateANote(note: NoteEntity)


    @Delete
    abstract suspend fun deleteANote(note: NoteEntity)

    @Query("SELECT * FROM NOTES_TABLE")
    abstract fun getAllNotes(): Flow<List<NoteEntity>>

}