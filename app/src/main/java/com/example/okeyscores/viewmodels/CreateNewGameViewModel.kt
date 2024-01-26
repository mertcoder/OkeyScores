package com.example.okeyscores.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okeyscores.datamodels.GameData
import com.example.okeyscores.repo.AuthRepository
import com.example.okeyscores.util.Resource
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CreateNewGameViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository
) : ViewModel() {

    private var _checkStatus = Channel<Resource<String>>()
    val checkStatus get() = _checkStatus.consumeAsFlow()


    fun uploadGameData(players: ArrayList<String>, firstTeamAllScores: ArrayList<Int>, firstTeamTotalScore: Int, secondTeamAllScores: ArrayList<Int>, secondTeamTotalScore: Int,secondUserMail: String,thirdUserMail:String,fourthUserMail: String){
        viewModelScope.launch { _checkStatus.send(Resource.Loading()) }
        if(firstTeamAllScores.isNullOrEmpty() or secondTeamAllScores.isNullOrEmpty()){
            viewModelScope.launch {
                _checkStatus.send(Resource.Error("Please play atleast 1 match."))
            }
        }else{
            val timestampString = timestampToString(getCurrentTime())
            val gameData = GameData(players,firstTeamAllScores,firstTeamTotalScore,secondTeamAllScores,secondTeamTotalScore,timestampString)
            saveGameToUser(gameData,auth.getUser()!!.email.toString())
            saveGameToUser(gameData,secondUserMail)
            saveGameToUser(gameData,thirdUserMail)
            saveGameToUser(gameData,fourthUserMail)

        }
    }

    private fun saveGameToUser(gameData: GameData, userMail: String) {
        val userGameDataDocRef = firestore.collection("users").document(userMail)
        println("GameData $gameData")
        //upload to firebase
        viewModelScope.launch {
            firestore.collection("games").document().set(gameData)
                .addOnSuccessListener { viewModelScope.launch { _checkStatus.send(Resource.Success("")) } }
                .addOnFailureListener{ viewModelScope.launch {_checkStatus.send(Resource.Error("Something went wrong.")) }}
            userGameDataDocRef.get().addOnSuccessListener {
                val currentGamesData = it.get("games")

                val newGamesData = if (currentGamesData != null) {
                    (currentGamesData as ArrayList<GameData>)
                } else {
                    arrayListOf()
                }


                newGamesData.add(gameData)
                val updatedData = mapOf("games" to newGamesData)
                userGameDataDocRef.update(updatedData).addOnSuccessListener {
                    viewModelScope.launch { _checkStatus.send(Resource.Success("Belge Başarıyla Güncellendi")) }
                }.addOnFailureListener {
                    viewModelScope.launch { _checkStatus.send(Resource.Error("Belge güncellenirken bir hata oluştu.")) }
                }

            }

        }
    }

    private fun getCurrentTime() = Timestamp.now()

    private fun timestampToString(timestamp: Timestamp) : String{
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val date = Date(timestamp.seconds*1000 + timestamp.nanoseconds/1000000)
        return sdf.format(date)
    }

    private fun stringToTimeStamp(dateTimeString: String): Timestamp{
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm",Locale.getDefault())
        val date = sdf.parse(dateTimeString)
        return Timestamp(date)
    }






}