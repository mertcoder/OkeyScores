package com.example.okeyscores.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.okeyscores.databinding.CreateAccountFragmentBinding
import com.example.okeyscores.datamodels.User
import com.example.okeyscores.validation.RegisterValidation
import com.example.okeyscores.viewmodels.CreateAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CreateAccountFragment: Fragment() {
    private var _binding: CreateAccountFragmentBinding?=null
    val binding get() = _binding!!
    private val viewModel by viewModels<CreateAccountViewModel>()
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
        binding.btnAlreadyHaveAnAccount.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.apply {
            btnCreateAccount.setOnClickListener{
                val email = edEmail.text.toString()
                val username = edUsername.text.toString()
                val name = edName.text.toString()
                val password = edPassword.text.toString()
                val user = User(email,username,name)
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


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}