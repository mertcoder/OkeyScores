package com.example.okeyscores.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okeyscores.datamodels.User
import com.example.okeyscores.repo.AuthRepository
import com.example.okeyscores.repo.BaseAuthRepository
import com.example.okeyscores.repo.FirebaseUserDbRepository
import com.example.okeyscores.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeFragmentViewModel @Inject constructor(
    private val db: FirebaseUserDbRepository,
    private val auth: AuthRepository
): ViewModel() {

    private val _signedUser = MutableSharedFlow<Resource<User?>>()
    val signedUser get() = _signedUser.asSharedFlow()



    fun signIn(email: String, password: String) = viewModelScope.launch{
        _signedUser.emit(Resource.Loading())
        try{
            val loggedInFirebaseUser = auth.signInWithEmailPassword(email,password)
            if(loggedInFirebaseUser!=null){
                val loggedInUser = db.getUserInfo(email)
                _signedUser.emit(Resource.Success(loggedInUser))
            }else{
                _signedUser.emit(Resource.Error("No matching account."))
            }
        }catch (e: Exception){
            _signedUser.emit(Resource.Error(e.message.toString()))
        }
    }
}