package com.example.okeyscores.datamodels

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TIMESTAMP EKLE DÖNÜŞÜMLERİ YAP
data class GameData(
    val players: ArrayList<String>,
    val firstTeamAllScores: ArrayList<Int>,
    val firstTeamTotalScore: Int,
    val secondTeamAllScores: ArrayList<Int>,
    val secondTeamTotalScore: Int,
    val timeStamp: String
){
    constructor(): this(arrayListOf(), arrayListOf(),0, arrayListOf(),0,"")

    fun getDateTime(): Date {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return sdf.parse(timeStamp)
    }


}