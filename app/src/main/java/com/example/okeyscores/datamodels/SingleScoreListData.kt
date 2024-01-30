package com.example.okeyscores.datamodels

import androidx.room.PrimaryKey

data class SingleScoreListData(
    val itemId: Int,
    val firstTeamScore: Int,
    val secondTeamScore: Int
){
    constructor(): this(0,0,0)
}