package com.example.okeyscores.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okeyscores.datamodels.User
import com.example.okeyscores.repo.AuthRepository
import com.example.okeyscores.repo.BaseAuthRepository
import com.example.okeyscores.repo.FirebaseUserDbRepository
import com.example.okeyscores.util.Resource
import com.google.firebase.auth.FirebaseAuth
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
    private val auth: FirebaseAuth
): ViewModel() {

    private val _signedUser = MutableSharedFlow<Resource<User?>>()
    val signedUser get() = _signedUser.asSharedFlow()



    fun signIn(email: String, password: String) = viewModelScope.launch{
        _signedUser.emit(Resource.Loading())
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            if(it!=null){
                viewModelScope.launch {
                    val loggedInUser = db.getUserInfo(email)
                    _signedUser.emit(Resource.Success(loggedInUser))
                }
            }else{
                viewModelScope.launch {_signedUser.emit(Resource.Error("No matching account.")) }
            }

        }.addOnFailureListener{
            viewModelScope.launch {_signedUser.emit(Resource.Error("${it.message.toString()}")) }

        }
    }
}