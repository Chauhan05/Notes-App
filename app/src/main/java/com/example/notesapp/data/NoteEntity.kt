package com.example.notesapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity(tableName = "notes_table")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "note-title")
    val title: String,
    @ColumnInfo(name = "note-description")
    val description: String,
    @ColumnInfo(name = "note-is-starred")
    val isStarred: Boolean = false,
    @ColumnInfo(name = "note-date")
    val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM")),
    @ColumnInfo(name = "note-time")
    val time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),

    @ColumnInfo(name = "note-password")
    val password: String? = null

)