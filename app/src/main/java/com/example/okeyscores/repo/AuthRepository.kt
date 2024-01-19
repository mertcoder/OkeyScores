package com.example.okeyscores.repo

import com.example.okeyscores.auth.BaseAuthenticator
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authenticator: BaseAuthenticator
): BaseAuthRepository {
    override suspend fun signUpWithEmailPassword(email: String, password: String): FirebaseUser? {
        return authenticator.signUpWithEmailPassword(email, password)
    }

    override suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser? {
        return authenticator.signInWithEmailPassword(email, password)
    }

    override fun signOut(): FirebaseUser? {
        return authenticator.signOut()
    }

    override fun getUser(): FirebaseUser? {
        return authenticator.getUser()
    }

    override suspend fun sendPasswordReset(email: String): Boolean {
        authenticator.sendPasswordReset(email)
        return true
    }


}