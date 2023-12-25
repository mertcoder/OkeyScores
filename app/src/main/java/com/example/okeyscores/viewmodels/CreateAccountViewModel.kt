package com.example.okeyscores.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okeyscores.datamodels.User
import com.example.okeyscores.validation.RegisterFieldState
import com.example.okeyscores.validation.RegisterValidation
import com.example.okeyscores.validation.validateEmail
import com.example.okeyscores.validation.validateName
import com.example.okeyscores.validation.validateNickname
import com.example.okeyscores.validation.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

//@HiltViewModel
class CreateAccountViewModel: ViewModel() {
    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()


    fun validateUser(user: User, password: String) {
        val registerFieldState = RegisterFieldState(
            email = validateEmail(user.email),
            username = validateNickname(user.username),
            name = validateName(user.name),
            validatePassword(password))
        val isValid = validateEmail(user.email) is RegisterValidation.Success &&
                validateNickname(user.username) is RegisterValidation.Success &&
                validateName(user.name) is RegisterValidation.Success &&
                validatePassword(password) is RegisterValidation.Success
        if(isValid
        ){
            // Register user to Firebase Email Pass and Save Infos to Realtime DB
           /*Debug Print */ println("Successfully valid")
        }else {
            viewModelScope.launch {_validation.send(registerFieldState) }
        }

    }
}