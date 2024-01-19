package com.example.okeyscores.validation

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation{
    if(email.isEmpty()){
        return RegisterValidation.Failed("Email cannot be empty.")
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return RegisterValidation.Failed("Email is not valid.")
    }
    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation{
    if(password.isEmpty()) {
        return RegisterValidation.Failed("Password cannot be empty.")
    }
    if (password.length<6) {
        return RegisterValidation.Failed("Password must contains minimum 6 characters.")
    }
    if (password.length>18){
        return RegisterValidation.Failed("Password cannot be longer than 18 characters.")
    }
    return RegisterValidation.Success
}

fun validateUsername(username: String): RegisterValidation{
    if(username.isEmpty()){
        return RegisterValidation.Failed("Username cannot be empty.")
    }
    if(username.contains(" ")){
        return RegisterValidation.Failed("Username cannot contain spaces.")
    }
    if (username != username.toLowerCase()) {
        return RegisterValidation.Failed("Username cannot contain upper cases.")
    }
    if (username.length < 3) {
        return RegisterValidation.Failed("Username must be at least 3 characters.")
    }
    if (username.length > 10) {
        return RegisterValidation.Failed("Username cannot be longer than 10 characters.")
    }
    // Özel karakterler kontrolü
    val specialCharacters = setOf('-', '_', '.')
    for (char in username) {
        if (!char.isLetterOrDigit() && char !in specialCharacters) {
            return RegisterValidation.Failed("Username can only contain '. _ -' ")
        }
    }
    return RegisterValidation.Success
}

fun validateName(name: String): RegisterValidation {
    if (name.isEmpty()) {
        return RegisterValidation.Failed("Name cannot be empty.")
    }
    if (name.length < 2) {
        return RegisterValidation.Failed("Name must be at least 2 characters.")
    }
    if (name.length > 15) {
        return RegisterValidation.Failed("Name cannot be longer than 15 characters.")
    }
    // İsim sadece harflerden oluşmalıdır
    if (!name.all { it.isLetter() || it.isWhitespace() }) {
        return RegisterValidation.Failed("Name can only contain letters.")
    }
    return RegisterValidation.Success
}