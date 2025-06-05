package com.example.notesapp.uistates

import com.example.notesapp.data.NoteEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class NoteUiState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val isStarred: Boolean = false,
    val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM")),
    val time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
    val password: String? = null
) {
    // Convert UI state to NoteEntity
    fun toEntity(): NoteEntity {
        return NoteEntity(
            id = id,
            title = title,
            description = description,
            isStarred = isStarred,
            date = date,
            time = time,
            password = password
        )
    }

    companion object {
        // Convert NoteEntity to UI State
        fun fromEntity(note: NoteEntity): NoteUiState {
            return NoteUiState(
                id = note.id,
                title = note.title,
                description = note.description,
                isStarred = note.isStarred,
                date = note.date,
                time = note.time,
                password = note.password
            )
        }
    }
}
