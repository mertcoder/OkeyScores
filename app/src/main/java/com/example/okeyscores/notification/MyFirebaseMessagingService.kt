package com.example.okeyscores.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println("your remote message" + remoteMessage.notification )
        println("your remote data " +  remoteMessage.data)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("your token" + token)
    }

}