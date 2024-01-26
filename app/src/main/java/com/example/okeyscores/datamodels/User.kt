package com.example.okeyscores.datamodels

data class User(
    val email: String = "",
    val username: String ="",
    val name: String = "",
    val games: List<GameData>?=null,
    val token: String ?=null
){
    constructor(): this("","","",null,null)
}
