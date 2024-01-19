package com.example.okeyscores.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.okeyscores.R
import com.example.okeyscores.databinding.ActivityMainBinding
import com.example.okeyscores.repo.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val MARKER = "Activity: Main Activity"
    private var _binding: ActivityMainBinding?=null
    private val binding get() = _binding!!
    @Inject
    lateinit var auth:AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(MARKER)
        if (auth.getUser() != null) {
            println("Auth user: " + auth!!.getUser()!!.email)
            val intent = Intent(applicationContext,ScoresActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
        else{
            _binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}