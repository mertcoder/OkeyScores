package com.example.okeyscores.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okeyscores.datamodels.User
import com.example.okeyscores.repo.AuthRepository
import com.example.okeyscores.util.LoggedUserDataManager
import com.example.okeyscores.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PullLoggedUserDataViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val firestore: FirebaseFirestore

) : ViewModel(){
    val usersCollection = firestore.collection("users")
    private var _state = MutableStateFlow(false)
    val state get() = _state.asStateFlow()

    private var _pulledUserData = MutableSharedFlow<Resource<User>>()
    val pulledUserData get() = _pulledUserData.asSharedFlow()


    fun pullUserData(){
        viewModelScope.launch{_pulledUserData.emit(Resource.Loading())}
        viewModelScope.launch {
            val userDocumentRef = usersCollection.document(auth.getUser()!!.email.toString())
            userDocumentRef.get().addOnSuccessListener {
                val hostUser = it.toObject(User::class.java)
                if(hostUser!=null) {
                    _state.value = true
                    viewModelScope.launch{_pulledUserData.emit(Resource.Success(hostUser))}
                    LoggedUserDataManager.setHostUserData(hostUser)
                }else{
                    _state.value = false
                }

            }.addOnFailureListener{
                viewModelScope.launch { _pulledUserData.emit(Resource.Error(it.message.toString())) }
            }
        }
    }

}