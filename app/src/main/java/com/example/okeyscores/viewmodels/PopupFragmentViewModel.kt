package com.example.okeyscores.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okeyscores.auth.FirebaseAuthenticator
import com.example.okeyscores.datamodels.User
import com.example.okeyscores.repo.AuthRepository
import com.example.okeyscores.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.checkerframework.checker.interning.qual.FindDistinct
import javax.inject.Inject

@HiltViewModel
class PopupFragmentViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    val usersCollection = firestore.collection("users")
    private var _userDocument = MutableSharedFlow<Resource<User>>()
    val userDocument get() = _userDocument.asSharedFlow()

    private var _validation = MutableSharedFlow<Resource<String>>()
    val validation  get() = _validation.asSharedFlow()

    fun findUserWithNickname(username: String) {
        viewModelScope.launch {
            println("popup started")
            coroutineScope {

            }
            val querySnapshot = usersCollection.whereEqualTo("username", username).get().await()
            val document = querySnapshot.documents.firstOrNull()?.toObject(User::class.java)

            println("popup started $document")
            if(document == null){
                _userDocument.emit(Resource.Error("$username is not valid."))
            }else{
                _userDocument.emit(Resource.Success(document))
            }
        }

    }


    fun validatePlayers(playerList: ArrayList<String>){
        println("player list: $playerList")
        val checkDistinct = playerList.distinct()
        if(playerList.size != checkDistinct.size){
            viewModelScope.launch { _validation.emit(Resource.Error("Please select different users.")) }
        }else{
            viewModelScope.launch { _validation.emit(Resource.Success("")) }
        }


    }



}