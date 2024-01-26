package com.example.okeyscores.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.okeyscores.BuildConfig
import com.example.okeyscores.R
import com.example.okeyscores.databinding.ActivityMainBinding
import com.example.okeyscores.databinding.SplashFragmentBinding
import com.example.okeyscores.repo.AuthRepository
import com.example.okeyscores.util.Resource
import com.example.okeyscores.viewmodels.PullLoggedUserDataViewModel
import com.example.okeyscores.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "Activity: Main Activity"
    @Inject
    lateinit var auth:AuthRepository

    private val sharedViewModel by viewModels<PullLoggedUserDataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"")
        if (auth.getUser() != null) {
            Log.d(TAG,"Auth user: " + auth!!.getUser()!!.email)
            val intent = Intent(applicationContext,ScoresActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            sharedViewModel.pullUserData()
            //While loading data, shows splash screen
            val binding = SplashFragmentBinding.inflate(layoutInflater)
            setContentView(binding.root)
            lifecycleScope.launchWhenStarted {
                sharedViewModel.state.collectLatest {
                    if(it){
                        startActivity(intent)
                        finish()
                    }
                }
            }


        }
        else{
            val binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
        }
    }

}