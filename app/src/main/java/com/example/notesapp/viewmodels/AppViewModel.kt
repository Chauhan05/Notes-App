package com.example.notesapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.uistates.NoteUiState
import com.example.notesapp.data.Graph
import com.example.notesapp.data.NoteEntity
import com.example.notesapp.data.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val noteRepository: NoteRepository = Graph.noteRepository
) : ViewModel() {

    lateinit var getAllNotes: Flow<List<NoteEntity>>



    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState = _uiState.asStateFlow()

    private val _noteById = MutableStateFlow<NoteEntity?>(null)
    val noteById = _noteById.asStateFlow()


    fun getNoteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = noteRepository.getNoteById(id)
            _noteById.value = note
        }
    }




//    private val _uiStateForDialog = MutableStateFlow(UiStatesForDialog())
//    val uiStateForDialog = _uiStateForDialog.asStateFlow()
//
//
//    fun updateAlertDialogForPasswordEntry(newValue: Boolean) {
//        _uiStateForDialog.value=_uiStateForDialog.value.copy(
//            alertDialogForPasswordEntry = newValue
//        )
//    }
//
//    fun updateAlertDialogToStarNote(newValue: Boolean) {
//        _uiStateForDialog.value=_uiStateForDialog.value.copy(
//            alertDialogToStarNote = newValue
//        )
//    }
//
//    fun updateAlertDialogForLocking(newValue: Boolean) {
//        _uiStateForDialog.value=_uiStateForDialog.value.copy(
//            alertDialogForLocking = newValue
//        )
//    }

    private val _isPasswordSwitchEnabled = MutableStateFlow(false)
    val isPasswordSwitchEnabled = _isPasswordSwitchEnabled.asStateFlow()


    private val _searchBarValue = MutableStateFlow("")
    val searchBarValue = _searchBarValue.asStateFlow()


    private val _passwordInAlert = MutableStateFlow("")
    val passwordInAlert = _passwordInAlert.asStateFlow()


    private val _setPasswordDialogValue = MutableStateFlow("")
    val setPasswordDialogValue = _setPasswordDialogValue.asStateFlow()

    fun updateSetPasswordDialogValue(newValue: String) {
        _setPasswordDialogValue.value = newValue
    }


    private val _unlockedNotes = MutableStateFlow<Set<Int>>(emptySet())
    val unLockedNotes = _unlockedNotes.asStateFlow()

    //    To unlock the note
    fun unlockNote(noteId: Int) {
        _unlockedNotes.update {
            it + noteId
        }
        Log.d("Main", "Unlocked Notes: ${_unlockedNotes.value}")
    }

    fun lockNote(noteId: Int) {
        _unlockedNotes.update {
            it - noteId
        }
    }

    fun updateSearchBarValue(newValue: String) {
        _searchBarValue.value = newValue
    }

    //    to check if the lock is unlocked
    fun isNoteUnlocked(noteId: Int): Boolean {
        return _unlockedNotes.value.contains(noteId)
    }


    fun onEnterPasswordChange(newPassword: String) {
        _passwordInAlert.value = newPassword
    }


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

    private fun entryIsValid(): Boolean {
        if (_isPasswordSwitchEnabled.value) {
            return _uiState.value.title.isNotBlank() && _uiState.value.description.isNotBlank() && _uiState.value.password?.isNotBlank() == true

        }
        return _uiState.value.title.isNotBlank() && _uiState.value.description.isNotBlank()

    }

    fun onSaveClick(): String {
        if (entryIsValid()) {
            val noteEntity = _uiState.value.toEntity()
            viewModelScope.launch {
                noteRepository.addANote(noteEntity)
            }
            _uiState.value = NoteUiState()
            _isPasswordSwitchEnabled.value = false
            return "Entry Added"
        }
        return "Fill all the Details"
    }


    init {

        viewModelScope.launch {
            getAllNotes = noteRepository.getAllNotes()
        }
    }


    fun addANote(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.addANote(note)
        }
    }


    fun updateANote(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateANote(note)
        }
    }

    fun deleteANote(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteANote(note)
        }
    }

//    fun getNoteById(id:Int){
//        viewModelScope.launch(Dispatchers.IO) {
//            getNoteById=noteRepository.getNoteById(id)
//        }
//    }


}