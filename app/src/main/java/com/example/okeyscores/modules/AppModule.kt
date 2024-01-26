package com.example.okeyscores.modules

import com.example.okeyscores.auth.BaseAuthenticator
import com.example.okeyscores.auth.FirebaseAuthenticator
import com.example.okeyscores.repo.AuthRepository
import com.example.okeyscores.repo.BaseAuthRepository
import com.example.okeyscores.repo.FirebaseUserDbRepository
import com.example.okeyscores.repo.UserDbRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAuthenticator():  BaseAuthenticator{
        return FirebaseAuthenticator()
    }

    @Singleton
    @Provides
    fun provideAuth():  FirebaseAuth{
        return FirebaseAuth.getInstance()
    }


    @Singleton
    @Provides
    fun provideRepository(authenticator: BaseAuthenticator): BaseAuthRepository{
        return AuthRepository(authenticator)
    }

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseUserDbRepository(db: FirebaseFirestore): UserDbRepository{
        return FirebaseUserDbRepository(db)
    }

    @Singleton
    @Provides
    fun provideFirebaseCloudMessaging(): FirebaseMessaging{
        return FirebaseMessaging.getInstance()
    }

}