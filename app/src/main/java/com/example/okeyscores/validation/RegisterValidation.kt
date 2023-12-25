package com.example.okeyscores.validation


sealed class RegisterValidation(){
    object Success: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()
}

data class RegisterFieldState(
    val email: RegisterValidation,
    val username : RegisterValidation,
    val name: RegisterValidation,
    val password: RegisterValidation
)