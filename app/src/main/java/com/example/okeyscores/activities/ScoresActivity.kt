package com.example.okeyscores.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.okeyscores.R
import com.example.okeyscores.databinding.ActivityScoresBinding
import com.example.okeyscores.fragments.ScoresActivityFragments.MatchesFragment
import com.example.okeyscores.fragments.ScoresActivityFragments.ProfileFragment
import com.example.okeyscores.fragments.ScoresActivityFragments.RankFragment
import com.example.okeyscores.util.LoggedUserDataManager
import com.example.okeyscores.viewmodels.FCMViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoresActivity : AppCompatActivity() {
    private val TAG = "Activity: Scores Activity"
    private lateinit var navController: NavController
    private var _binding: ActivityScoresBinding?=null
    private val binding get() = _binding!!
    private val fcmViewModel by viewModels<FCMViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"")
        _binding = ActivityScoresBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomBar()
        fcmViewModel.updateFcmToken(LoggedUserDataManager.getHostEmail().toString())

    }

    private fun setupBottomBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewScores) as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(binding.nvBottom,navController)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}