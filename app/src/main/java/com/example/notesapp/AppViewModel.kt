package com.example.notesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.Graph
import com.example.notesapp.data.NoteEntity
import com.example.notesapp.data.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val noteRepository: NoteRepository = Graph.noteRepository
) : ViewModel() {

    lateinit var getAllNotes: Flow<List<NoteEntity>>

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState = _uiState.asStateFlow()

    private val _isPasswordSwitchEnabled = MutableStateFlow(false)
    val isPasswordSwitchEnabled = _isPasswordSwitchEnabled.asStateFlow()


    fun updatePasswordEnableSwitch(newValue: Boolean) {
        _isPasswordSwitchEnabled.value = newValue
    }

    fun updateTitle(newValue: String) {
        _uiState.value = _uiState.value.copy(title = newValue)
    }

    fun updateDescription(newValue: String) {
        _uiState.value = _uiState.value.copy(description = newValue)
    }

    fun toggleIsStarred() {
        _uiState.value = _uiState.value.copy(isStarred = !_uiState.value.isStarred)
    }

    fun updatePassword(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue)
    }

    private fun entryIsValid(): Boolean =
        _uiState.value.title.isNotBlank() && _uiState.value.description.isNotBlank()

    fun onSaveClick(): String {
        if (entryIsValid()) {
            val noteEntity = _uiState.value.toEntity()
            viewModelScope.launch {
                noteRepository.addANote(noteEntity)
            }
            _uiState.value = NoteUiState()
            _isPasswordSwitchEnabled.value=false
            return "Entry Added"
        }
        return "Fill all the Details"
    }


    init {

        viewModelScope.launch {
            getAllNotes = noteRepository.getAllNotes()
        }
    }


    fun addWish(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.addANote(note)
        }
    }


    fun updateAWish(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateANote(note)
        }
    }

    fun deleteAWish(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteANote(note)
        }
    }


}