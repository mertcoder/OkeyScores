package com.example.okeyscores.fragments.ScoresActivityFragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.okeyscores.R
import com.example.okeyscores.adapters.ScoresRvAdapter
import com.example.okeyscores.databinding.CreateNewGameFragmentBinding
import com.example.okeyscores.datamodels.SingleScoreListData
import com.example.okeyscores.fragments.popup.PopupFragment
import com.example.okeyscores.util.Resource
import com.example.okeyscores.viewmodels.CreateNewGameViewModel
import com.example.okeyscores.viewmodels.SharedViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


@AndroidEntryPoint
class CreateNewGameFragment: Fragment(R.layout.create_new_game_fragment){
    private var _binding: CreateNewGameFragmentBinding?=null
    private val binding get() = _binding!!
    private val scoresList = ArrayList<SingleScoreListData>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val viewModel by viewModels<CreateNewGameViewModel>()
    private lateinit var adapter: ScoresRvAdapter



    private val firstTeamAllScoresList = ArrayList<Int>()
    private val secondTeamAllScoresList = ArrayList<Int>()
    private var players = ArrayList<String>()
    private var firstTeamTotalScore = 0
    private var secondTeamTotalScore = 0
    private var secondUserMail =""
    private var thirdUserMail = ""
    private var fourthUserMail = ""



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CreateNewGameFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewmodel tasks
        observePlayersNameBySharedViewModel()

        lifecycleScope.launchWhenStarted {
            viewModel.checkStatus.collectLatest {
                when(it){
                    is Resource.Loading->{
                        binding.btnFinish.startAnimation()
                    }
                    is Resource.Success->{
                        binding.btnFinish.revertAnimation()
                        findNavController().popBackStack()
                    }
                    is Resource.Error->{
                        binding.btnFinish.revertAnimation()
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    else->Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            sharedViewModel.userAllData.collectLatest {
                println("Userlist in Createnewgame $it")
                secondUserMail = it[0].email
                thirdUserMail = it[1].email
                fourthUserMail = it[2].email

            }
        }


        showDialogFragment()
        initializeAdapter()
        addScore()
        //finishing game
        finishGame()
    }

    private fun finishGame() {
        binding.btnFinish.setOnClickListener {

            val adapterList = adapter.currentList()
            adapterList.forEach{
                firstTeamAllScoresList.add(it.firstTeamScore)
                secondTeamAllScoresList.add(it.secondTeamScore)
            }
            viewModel.uploadGameData(players,firstTeamAllScoresList,firstTeamTotalScore,secondTeamAllScoresList,secondTeamTotalScore,secondUserMail,thirdUserMail,fourthUserMail,scoresList)
        }
    }

    private fun observePlayersNameBySharedViewModel() {
        lifecycleScope.launchWhenStarted {
            sharedViewModel.playersList.collectLatest {
                players = it
                selectPlayersName(it)
            }
        }
    }

    private fun selectPlayersName(it: ArrayList<String>) {
        binding.apply {
            tvPlayerOne.text = it[0]
            tvPlayerTwo.text = it[1]
            tvPlayerThree.text = it[2]
            tvPlayerFour.text = it[3]
        }
    }

    private fun showDialogFragment() {
         PopupFragment().show((activity as AppCompatActivity).supportFragmentManager,"showPopup")
    }

    private fun addScore() {
        binding.btnAddScore.setOnClickListener {
            updateScores()
            calculateTotalScores()

        }
    }

    private fun calculateTotalScores() {
        firstTeamTotalScore = 0
        secondTeamTotalScore = 0
        val scoreListFromAdapter = adapter.currentList()
        scoreListFromAdapter.forEach{
            firstTeamTotalScore += it.firstTeamScore
            secondTeamTotalScore+= it.secondTeamScore
        }
        //Update UI
        binding.apply {
            tvFirstTeamScore.text = firstTeamTotalScore.toString()
            tvSecondTeamScore.text = secondTeamTotalScore.toString()

            //Update Colors
            if(firstTeamTotalScore>secondTeamTotalScore) { makeFirstTeamBlue() }
            else if(firstTeamTotalScore == secondTeamTotalScore){makeFirstAndSecondTeamBlue()}
            else { makeSecondTeamBlue() }
         }


    }

    private fun makeFirstAndSecondTeamBlue() {
        binding.apply {
            tvFirstTeamScore.setTextColor(Color.parseColor("#02DDF0"))
            tvSecondTeamScore.setTextColor(Color.parseColor("#02DDF0"))
        }
    }

    private fun makeFirstTeamBlue(){
        binding.apply {
            tvFirstTeamScore.setTextColor(Color.parseColor("#02DDF0"))
            tvSecondTeamScore.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }
    private fun makeSecondTeamBlue(){
        binding.apply {
            tvSecondTeamScore.setTextColor(Color.parseColor("#02DDF0"))
            tvFirstTeamScore.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

    var itemId = 0
    private fun submitScores() {
        // Calculate single team scores
        val firstPlayerScore = binding.edPlayerOne.text.toString().toInt()
        val secondPlayerScore = binding.edPlayerTwo.text.toString().toInt()
        val thirdPlayerScore = binding.edPlayerThree.text.toString().toInt()
        val fourthPlayerScore = binding.edPlayerFour.text.toString().toInt()

        // Adding To ArrayList
        val firstTeamScore = (firstPlayerScore+secondPlayerScore)
        val secondTeamScore = (thirdPlayerScore+fourthPlayerScore)
        val singleScore = SingleScoreListData(firstTeamScore = firstTeamScore, secondTeamScore = secondTeamScore, itemId = itemId)
        itemId++
        scoresList.add(singleScore)

        //Now I'll send this list to the Adapter
        adapter.submitList(scoresList)

    }

    private fun updateScores() {
        if(validateScores()){
            binding.apply {
                submitScores()
                edPlayerOne.setText("")
                edPlayerTwo.setText("")
                edPlayerThree.setText("")
                edPlayerFour.setText("")
            }
        }else{
            Toast.makeText(requireContext(), "Please fill the blanks.", Toast.LENGTH_LONG).show()

        }
    }

    private fun validateScores(): Boolean {
        binding.apply {
            return !edPlayerOne.text.isNullOrEmpty() and
                    !edPlayerTwo.text.isNullOrEmpty() and
                    !edPlayerThree.text.isNullOrEmpty() and
                    !edPlayerFour.text.isNullOrEmpty()
        }

    }



    private fun initializeAdapter() {
        adapter = ScoresRvAdapter()
        binding.recyclerViewScores.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewScores.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}