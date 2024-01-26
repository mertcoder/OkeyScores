package com.example.okeyscores.fragments.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.okeyscores.R
import com.example.okeyscores.databinding.PopupFragmentBinding
import com.example.okeyscores.datamodels.User
import com.example.okeyscores.util.LoggedUserDataManager
import com.example.okeyscores.util.Resource
import com.example.okeyscores.viewmodels.PopupFragmentViewModel
import com.example.okeyscores.viewmodels.SharedViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

@AndroidEntryPoint
class PopupFragment: DialogFragment(R.layout.popup_fragment) {
    private val playerList = ArrayList<User>()
    private var isReadyToSearch = false
    private var firstPlayerName = ""
    private var _binding: PopupFragmentBinding?=null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PopupFragmentViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var players = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PopupFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        binding.btnSave.setOnClickListener{
            setPlayers()
        }
        binding.btnCancel.setOnClickListener{
            findNavController().popBackStack()
            dismiss()
        }


        collectUserDocumentFromViewModel()
        collectHostUsername()
        collectValidationFromViewModel()


    }


    private fun collectValidationFromViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collectLatest {
                when(it){
                    is Resource.Success->{isReadyToSearch = true}
                    is Resource.Error->{ Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show() }
                    else->Unit } }
        }
    }

    private fun collectHostUsername() {
        firstPlayerName = LoggedUserDataManager.getHostUsername().toString()
        binding.tvFirstPlayer.text = firstPlayerName
    }

    private fun collectUserDocumentFromViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.userDocument.collect{
                when(it){
                    is Resource.Success->{
                        if(!playerList.contains(it.data)){
                            playerList.add(it.data!!)
                            println(it.data)
                            println("debug $playerList")
                            if(playerList.size==3){
                                sharedViewModel.passPlayerUsersAllData(playerList)
                                sharedViewModel.commitPlayers(players)
                                dismiss()
                            }else{
                                println("All users are not correct $playerList")
                            }
                        }
                    }
                    is Resource.Error->{
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()}
                    else->Unit
                }
            }
        }
    }

    private fun setPlayers() {
        players.clear()
        binding.apply {
            val secondPlayerName = edSecondPlayer.text.toString()
            val thirdPlayerName = edThirdPlayer.text.toString()
            val fourthPlayerName = edFourthPlayer.text.toString()
            players.add(firstPlayerName)
            players.add(secondPlayerName)
            players.add(thirdPlayerName)
            players.add(fourthPlayerName)
            if(!secondPlayerName.trim().isNullOrEmpty() and !thirdPlayerName.trim().isNullOrEmpty() and !fourthPlayerName.trim().isNullOrEmpty()){
                viewModel.validatePlayers(players)
            }else{
                Toast.makeText(requireContext(), "Please fill the blanks.", Toast.LENGTH_SHORT).show()
            }

            if(isReadyToSearch){
                lifecycleScope.launch{
                    async {viewModel.findUserWithNickname(secondPlayerName) }.await()
                    async {viewModel.findUserWithNickname(thirdPlayerName) }.await()
                    async {viewModel.findUserWithNickname(fourthPlayerName) }.await()


                }
            }
        }
    }

}