package com.example.okeyscores.datamodels

data class User(
    val email: String,
    val username: String,
    val name: String
){
    constructor(): this("","","")
}
