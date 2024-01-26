package com.example.okeyscores.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okeyscores.datamodels.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SharedViewModel: ViewModel() {

    private var _playersList = MutableSharedFlow<ArrayList<String>>()
    val playersList get() = _playersList.asSharedFlow()

    private var _userAllData = MutableSharedFlow<ArrayList<User>>()
    val userAllData get() = _userAllData.asSharedFlow()

    fun commitPlayers(players: ArrayList<String>){
        viewModelScope.launch { _playersList.emit(players) }
    }


    fun passPlayerUsersAllData(playerList: ArrayList<User>){
        viewModelScope.launch { _userAllData.emit(playerList) }
    }


}