package com.example.okeyscores.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.okeyscores.databinding.ItemScoresBinding
import com.example.okeyscores.datamodels.SingleScoreListData

class ChildScoresRvAdapter: RecyclerView.Adapter<ChildScoresRvAdapter.ScoresHolder>() {
    inner class ScoresHolder(val binding: ItemScoresBinding): ViewHolder(binding.root) {
        fun bind(singleScoreList: SingleScoreListData){
            binding.imgDelete.visibility = View.INVISIBLE
            binding.tvFirstTeamScore.text=singleScoreList.firstTeamScore.toString()
            binding.tvSecondTeamScore.text =singleScoreList.secondTeamScore.toString()
        }
    }


    private class ScoresDiffCallback : DiffUtil.ItemCallback<SingleScoreListData>() {
        override fun areItemsTheSame(oldItem: SingleScoreListData, newItem: SingleScoreListData): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: SingleScoreListData, newItem: SingleScoreListData): Boolean {
            return oldItem==newItem
        }
    }

    private val differ =  AsyncListDiffer(this,ScoresDiffCallback())

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

    fun submitList(scoreList: ArrayList<SingleScoreListData>){
        differ.submitList(scoreList)
    }


}