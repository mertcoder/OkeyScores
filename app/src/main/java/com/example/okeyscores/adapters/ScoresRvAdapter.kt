package com.example.okeyscores.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.okeyscores.databinding.ItemScoresBinding
import com.example.okeyscores.datamodels.SingleScoreListData

class ScoresRvAdapter() : RecyclerView.Adapter<ScoresRvAdapter.ScoresHolder>() {
    inner class ScoresHolder(private val binding: ItemScoresBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SingleScoreListData){
            // check data id
            println("Data id: ${data.itemId}" )

            // Update Layout
            binding.tvFirstTeamScore.text = data.firstTeamScore.toString()
            binding.tvSecondTeamScore.text = data.secondTeamScore.toString()

        }
    }

    private class ScoresDiffCallback: DiffUtil.ItemCallback<SingleScoreListData>() {
        override fun areItemsTheSame(oldItem: SingleScoreListData, newItem: SingleScoreListData): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: SingleScoreListData, newItem: SingleScoreListData): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this,ScoresDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoresHolder {
        val binding = ItemScoresBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ScoresHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ScoresHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


    fun submitList(newList: List<SingleScoreListData>){
        differ.submitList(newList)
    }
    fun currentList() = differ.currentList


}