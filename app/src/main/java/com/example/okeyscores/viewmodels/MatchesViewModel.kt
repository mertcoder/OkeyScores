package com.example.okeyscores.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okeyscores.datamodels.GameData
import com.example.okeyscores.datamodels.SingleScoreListData
import com.example.okeyscores.repo.AuthRepository
import com.example.okeyscores.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore
): ViewModel() {

    private var _usersAllGames = Channel<Resource<ArrayList<GameData>>>()
    val usersAllGames get() = _usersAllGames.consumeAsFlow()



    fun getAllGames() {
        val gameDataList = ArrayList<GameData>()
        viewModelScope.launch { _usersAllGames.send(Resource.Loading()) }
        viewModelScope.launch {
            firestore.collection("users").document(authRepository.getUser()?.email.toString()).get().addOnSuccessListener { dataSnapshot->
                println(dataSnapshot.data)
                var usersGamesList = if(dataSnapshot.get("games") !=null){
                    dataSnapshot.get("games") as ArrayList<HashMap<String,Any>>
                }else{
                    null
                }

                if (usersGamesList != null) {
                    for (hashmap in usersGamesList){
                        val userMatchHistoryList = ArrayList<SingleScoreListData>()
                        val historyHashmap =  hashmap["matchHistory"] as ArrayList<HashMap<Any,Any>>
                        for(it in historyHashmap) {
                            val singleMatch = SingleScoreListData(
                                secondTeamScore = it["secondTeamScore"].toString().toInt() as Int,
                                itemId = it["itemId"].toString().toInt() as Int,
                                firstTeamScore = it["firstTeamScore"].toString().toInt() as Int,
                                )
                            userMatchHistoryList.add(singleMatch)
                        }
                        val gamedata = GameData(
                            hashmap["players"] as ArrayList<String>,
                            hashmap["firstTeamAllScores"] as ArrayList<Int>,
                            hashmap["firstTeamTotalScore"].toString().toInt() as Int,
                            hashmap["secondTeamAllScores"] as ArrayList<Int>,
                            hashmap["secondTeamTotalScore"].toString().toInt() as Int,
                            hashmap["timeStamp"] as String,
                            userMatchHistoryList
                        )

                        gameDataList.add(gamedata)
                    }
                    viewModelScope.launch { _usersAllGames.send(Resource.Success(gameDataList)) }


                }else{
                    viewModelScope.launch { _usersAllGames.send(Resource.Unspecified()) }
                }
            }

        }
    }



}