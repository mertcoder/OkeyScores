package com.example.okeyscores.repo

import com.example.okeyscores.datamodels.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserDbRepository @Inject constructor(
    private val firebaseDatabase: FirebaseFirestore
) : UserDbRepository{
    override suspend fun createNewUserInDb(user: User) {
        firebaseDatabase.collection("users").document(user.email).set(user).await()
    }

    override suspend fun getUserInfo(userId: String): User? {
        val document = firebaseDatabase.collection("users").document(userId).get().await()
        return document.toObject(User::class.java)
    }


}