package com.example.okeyscores.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FCMViewModel @Inject constructor(
    private val fcm:  FirebaseMessaging,
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val userCollection = firestore.collection("users")

    private var _fcmToken = Channel<String>()
    val fcmToken get() = _fcmToken.consumeAsFlow()

    private var _spesificUsersFcmToken= Channel<String>()
    val spesificUsersFcmToken get() = _spesificUsersFcmToken.consumeAsFlow()


    init {
        getFcmToken()
    }

    private fun getFcmToken(){
        fcm.token.addOnCompleteListener(OnCompleteListener { task->
            if(task.isSuccessful){
                val token = task.result
                viewModelScope.launch { _fcmToken.send(token) }
                println("your token from fcm view model" + token)
            }else
                println("error occured fcm view model")
        })
    }

    fun updateFcmToken(mail: String){
        viewModelScope.launch {
            getFcmToken()
            val newToken =_fcmToken.receive()
            viewModelScope.launch {
                userCollection.document(mail).update("token",newToken).addOnSuccessListener {
                    println("$mail users token updated")
                }.addOnFailureListener{
                    println("$mail token can't updated ${it.message.toString()}")
                }
            }
        }
    }

    fun findUserToken(mail: String){
        viewModelScope.launch {
            userCollection.document(mail).get().addOnSuccessListener {documentSnapshot->
                if(documentSnapshot.exists() and( documentSnapshot.data!=null)){
                    val token = documentSnapshot.data?.get("token") as String?
                    token?.let {
                        viewModelScope.launch { _spesificUsersFcmToken.send(token) }
                    }
                }
            }
        }
    }


}
