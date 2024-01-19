package com.example.okeyscores.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.okeyscores.R
import com.example.okeyscores.databinding.ActivityScoresBinding

class ScoresActivity : AppCompatActivity() {
    private val MARKER = "Activity: Scores Activity"
    private var _binding: ActivityScoresBinding?=null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(MARKER)
        _binding = ActivityScoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}