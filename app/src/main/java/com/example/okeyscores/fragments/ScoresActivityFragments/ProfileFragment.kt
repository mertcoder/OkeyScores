package com.example.okeyscores.fragments.ScoresActivityFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.okeyscores.BuildConfig
import com.example.okeyscores.R
import com.example.okeyscores.activities.MainActivity
import com.example.okeyscores.databinding.ProfileFragmentBinding
import com.example.okeyscores.datamodels.User
import com.example.okeyscores.repo.AuthRepository
import com.example.okeyscores.util.LoggedUserDataManager
import com.example.okeyscores.util.Resource
import com.example.okeyscores.viewmodels.PullLoggedUserDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.profile_fragment){
    private val MARKER = "Fragment: Profile Fragment"
    private var _binding : ProfileFragmentBinding?=null
    private val binding get() = _binding!!
    private val pullLoggedUserDataViewModel by activityViewModels<PullLoggedUserDataViewModel>()

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println(MARKER)
        setupUI()
        logOut()
    }

    private fun logOut() {
        binding.btnLogOut.setOnClickListener{
            authRepository.signOut()
            val intent = Intent(requireActivity().applicationContext, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setupUI() {
        pullLoggedUserDataViewModel.pullUserData()
        lifecycleScope.launchWhenStarted {
            pullLoggedUserDataViewModel.pulledUserData.collect{user->
                when(user){
                    is Resource.Loading->{
                        binding.layoutConstraint.visibility = View.INVISIBLE
                        binding.pb.visibility = View.VISIBLE
                    }
                    is Resource.Success->{
                        binding.apply {
                            binding.pb.visibility = View.INVISIBLE
                            tvNameVariable.text = user.data?.name ?: ""
                            tvUsernameVariable.text = user.data?.username ?:""
                            tvEmailVariable.text = user.data?.email ?:""
                            tvTotalMatchesVariable.text = user.data?.let { calculateTotalMatches(it).toString() }
                            calculateWins(user.data)
                            binding.layoutConstraint.visibility = View.VISIBLE
                        }
                    }
                    is Resource.Error->{
                        binding.layoutConstraint.visibility = View.INVISIBLE
                        binding.pb.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "${user.message}", Toast.LENGTH_LONG).show()

                    }
                    else->Unit
                }
            }
        }
    }

    private fun calculateTotalMatches(user: User): Int {
        return if(user.games==null){
            0
        }else{
            user.games.size
        }
    }

    private fun calculateWins(user: User?){
        var wins = 0
        var loses = 0
        user?.games?.forEach {
            val teamIndex = it.players.indexOf(user.username)
            //in first team
            if(it.firstTeamTotalScore>it.secondTeamTotalScore){
                if((teamIndex==0) or (teamIndex==1)){
                    wins+=1
                }else{
                    loses+=1
                }
            }
            if(it.secondTeamTotalScore>it.firstTeamTotalScore){
                if((teamIndex==0) or (teamIndex==1)){
                    loses+=1
                }else{
                    wins+=1
                }
            }

        }
        binding.tvWinsVariable.text = wins.toString()
        binding.tvLosesVariable.text = loses.toString()
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}