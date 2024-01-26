package com.example.okeyscores.util

import com.example.okeyscores.datamodels.User

object LoggedUserDataManager {
    private var hostUser: User?=null

    fun getHostUserData(): User?{
        return hostUser
    }
    fun getHostUsername(): String?{
        return hostUser?.username
    }
    fun getHostEmail(): String?{
        return hostUser?.email
    }
    fun setHostUserData(user: User){
        hostUser = user
    }

}