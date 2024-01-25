package com.example.okeyscores.notification

import com.google.firebase.messaging.RemoteMessage

open class FirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Gelen bildirimleri işle
        // remoteMessage.notification ve remoteMessage.data üzerinden bilgilere erişebilirsiniz.
    }


}