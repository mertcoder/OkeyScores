package com.example.okeyscores.repo

import com.example.okeyscores.datamodels.User

interface UserDbRepository {
    suspend fun createNewUserInDb(user: User)
    suspend fun getUserInfo(userId: String): User?
}