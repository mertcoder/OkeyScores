package com.example.okeyscores.fragments.MainActivityFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.okeyscores.databinding.CreateAccountFragmentBinding
import com.example.okeyscores.datamodels.User
import com.example.okeyscores.util.Resource
import com.example.okeyscores.validation.RegisterValidation
import com.example.okeyscores.viewmodels.CreateAccountViewModel
import com.example.okeyscores.viewmodels.FCMViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CreateAccountFragment: Fragment() {
    private var _binding: CreateAccountFragmentBinding?=null
    val binding get() = _binding!!
    private val viewModel by viewModels<CreateAccountViewModel>()
    private val fcmViewModel by activityViewModels<FCMViewModel>()
    private var fcmToken: String?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CreateAccountFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFcmToken()
        binding.btnAlreadyHaveAnAccount.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.apply {
            btnCreateAccount.setOnClickListener{
                val email = edEmail.text.toString()
                val username = edUsername.text.toString()
                val name = edName.text.toString()
                val password = edPassword.text.toString()
                val user = User(email,username,name, token = fcmToken)
                viewModel.validateUser(user,password)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collectLatest { validation->
                if(validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edEmail.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if(validation.name is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edName.apply {
                            requestFocus()
                            error = validation.name.message
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edPassword.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
                if(validation.username is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edUsername.apply {
                            requestFocus()
                            error = validation.username.message
                        }
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.firebaseUser.collectLatest {
                when(it){
                    is Resource.Success->{
                        binding.btnCreateAccount.revertAnimation()
                        Toast.makeText(requireContext(), "Successfully created.", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error->{
                        binding.btnCreateAccount.revertAnimation()
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading->{
                        binding.btnCreateAccount.startAnimation()
                    }
                    else->{}
                }
            }
        }


    }

    private fun getFcmToken() {
        lifecycleScope.launchWhenStarted {
            fcmViewModel.fcmToken.collectLatest {
                fcmToken = it
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}