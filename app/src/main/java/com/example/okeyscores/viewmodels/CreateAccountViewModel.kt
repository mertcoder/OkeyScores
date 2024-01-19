package com.example.okeyscores.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okeyscores.datamodels.User
import com.example.okeyscores.repo.BaseAuthRepository
import com.example.okeyscores.repo.FirebaseUserDbRepository
import com.example.okeyscores.util.Resource
import com.example.okeyscores.validation.RegisterFieldState
import com.example.okeyscores.validation.RegisterValidation
import com.example.okeyscores.validation.validateEmail
import com.example.okeyscores.validation.validateName
import com.example.okeyscores.validation.validateUsername
import com.example.okeyscores.validation.validatePassword
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val repository: BaseAuthRepository,
    private val db: FirebaseUserDbRepository
): ViewModel() {
    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    private val _firebaseUser = MutableSharedFlow<Resource<FirebaseUser?>>()
    val firebaseUser get() = _firebaseUser.asSharedFlow()


    fun validateUser(user: User, password: String) {
        val registerFieldState = RegisterFieldState(
            email = validateEmail(user.email),
            username = validateUsername(user.username),
            name = validateName(user.name),
            validatePassword(password))
        val isValid = validateEmail(user.email) is RegisterValidation.Success &&
                validateUsername(user.username) is RegisterValidation.Success &&
                validateName(user.name) is RegisterValidation.Success &&
                validatePassword(password) is RegisterValidation.Success
        if(isValid
        ){
            // Register user to Firebase Email Pass and Save Infos to Realtime DB
            signUpUser(user,password)

           /*Debug Print */ println("Successfully valid")
        }else {
            viewModelScope.launch {_validation.send(registerFieldState) }
        }

    }

    private fun signUpUser(user: User,password: String) = viewModelScope.launch{
        _firebaseUser.emit(Resource.Loading())
        try{
            val firebaseUser = repository.signUpWithEmailPassword(user.email, password)
            firebaseUser?.let {
                _firebaseUser.emit(Resource.Success(firebaseUser))
                db.createNewUserInDb(user)
            }
        }catch (e: Exception){
                _firebaseUser.emit(Resource.Error(e.message.toString()))
        }

    }
}