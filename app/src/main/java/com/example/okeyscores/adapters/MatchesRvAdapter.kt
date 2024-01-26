package com.example.okeyscores.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.okeyscores.databinding.ItemMatchesBinding
import com.example.okeyscores.datamodels.GameData

class MatchesRvAdapter: RecyclerView.Adapter<MatchesRvAdapter.MatchesHolder>() {
    private val TAG = "MatchesRVAdapter"

    private var username: String?=null
    inner class MatchesHolder(val binding: ItemMatchesBinding): ViewHolder(binding.root) {
        fun bind(match: GameData){
            Log.d(TAG,"$match")
            binding.apply {
                tvFirstPlayer.text = match.players[0]
                tvSecondPlayer.text = match.players[1]
                tvThirdPlayer.text = match.players[2]
                tvFourthPlayer.text = match.players[3]
                tvScoreFirstTeam.text = match.firstTeamTotalScore.toString()
                tvScoreSecondTeam.text = match.secondTeamTotalScore.toString()
                tvDate.text = match.timeStamp
            }
            val winnerTeam = returnWinner(match.firstTeamTotalScore,match.secondTeamTotalScore)
            Log.d(TAG,"winner team  $winnerTeam $username")
            val hostIndex = match.players.indexOf(username)
            if((hostIndex == 0) or (hostIndex == 1)){
                if((winnerTeam==-1)){
                    binding.tvWinOrLose.text = "WIN"
                    binding.tvWinOrLose.setTextColor(Color.parseColor("#02DDF0"))

                }
                else if((winnerTeam==-2)){
                    binding.tvWinOrLose.text = "LOSE"
                    binding.tvWinOrLose.setTextColor(Color.parseColor("#db0054"))
                }else{
                    binding.tvWinOrLose.text = "DRAW"
                    binding.tvWinOrLose.setTextColor(Color.parseColor("#ffffff"))
                }
            }else{
                if((winnerTeam==-1)){
                    binding.tvWinOrLose.text = "LOSE"
                    binding.tvWinOrLose.setTextColor(Color.parseColor("#db0054"))

                }
                else if((winnerTeam==-2)){
                    binding.tvWinOrLose.text = "WIN"
                    binding.tvWinOrLose.setTextColor(Color.parseColor("#02DDF0"))
                }else{
                    binding.tvWinOrLose.text = "DRAW"
                    binding.tvWinOrLose.setTextColor(Color.parseColor("#ffffff"))
                }
            }

        }

        private fun returnWinner(firstTeamTotalScore: Int, secondTeamTotalScore: Int): Int {
            if(firstTeamTotalScore>secondTeamTotalScore){
                binding.tvScoreFirstTeam.setTextColor(Color.parseColor("#02DDF0"))
                binding.tvScoreSecondTeam.setTextColor(Color.parseColor("#db0054"))
                return -1 //FIRST TEAM WON
            }else{
                return if(firstTeamTotalScore == secondTeamTotalScore){
                    binding.tvScoreFirstTeam.setTextColor(Color.parseColor("#ffffff"))
                    binding.tvScoreSecondTeam.setTextColor(Color.parseColor("#ffffff"))
                    -3 // NO WINNER
                }else{
                    binding.tvScoreFirstTeam.setTextColor(Color.parseColor("#db0054"))
                    binding.tvScoreSecondTeam.setTextColor(Color.parseColor("#02DDF0"))
                    -2 // SECOND TEAM WON
                }
            }



        }
    }

    private class MatchesDiffCallback : DiffUtil.ItemCallback<GameData>() {
        override fun areItemsTheSame(oldItem: GameData, newItem: GameData): Boolean {
            return oldItem.timeStamp == newItem.timeStamp
        }

        override fun areContentsTheSame(oldItem: GameData, newItem: GameData): Boolean {
            return oldItem==newItem
        }
    }

    private val differ =  AsyncListDiffer(this,MatchesDiffCallback())


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesHolder {
        val binding = ItemMatchesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MatchesHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MatchesHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun submitList(newList: ArrayList<GameData>){
        differ.submitList(newList)
    }

    fun submitUserName(un: String){
        username = un
    }


}