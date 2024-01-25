package com.example.okeyscores.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.okeyscores.activities.ScoresActivity
import com.example.okeyscores.databinding.WelcomeFragmentBinding
import com.example.okeyscores.util.Resource
import com.example.okeyscores.viewmodels.WelcomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class WelcomeFragment: Fragment() {
    private var _binding : WelcomeFragmentBinding?=null
    private val binding get() = _binding!!
    private val viewModel by viewModels<WelcomeFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= WelcomeFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToCreateAccountFragment())

        }
        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            viewModel.signIn(email,password)
        }
        lifecycleScope.launchWhenStarted {
            viewModel.signedUser.collectLatest {
                when(it){
                    is Resource.Success->{
                        // uygulamaya yönlendir

                        binding.btnLogin.revertAnimation()
                        Toast.makeText(requireContext(), "logged in", Toast.LENGTH_SHORT).show()
                        println("aaaa"+it.data)
                        val intent = Intent(activity, ScoresActivity::class.java)
                        startActivity(intent)
                        activity?.finish()

                    }
                    is Resource.Error->{
                        binding.btnLogin.revertAnimation()
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading->{
                        binding.btnLogin.startAnimation()
                    }
                    else->{}
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}