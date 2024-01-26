package com.example.okeyscores.fragments.ScoresActivityFragments

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.okeyscores.R
import com.example.okeyscores.adapters.MatchesRvAdapter
import com.example.okeyscores.databinding.MatchesFragmentBinding
import com.example.okeyscores.util.LoggedUserDataManager
import com.example.okeyscores.util.Resource
import com.example.okeyscores.viewmodels.CreateNewGameViewModel
import com.example.okeyscores.viewmodels.MatchesViewModel
import com.example.okeyscores.viewmodels.PopupFragmentViewModel
import com.example.okeyscores.viewmodels.PullLoggedUserDataViewModel
import com.example.okeyscores.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MatchesFragment: Fragment(R.layout.matches_fragment){
    private val MARKER = "Fragment: Matches Fragment"
    private var _binding: MatchesFragmentBinding?=null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MatchesViewModel>()
    private val popupViewModel by activityViewModels<PopupFragmentViewModel>()
    private lateinit var adapter: MatchesRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MatchesFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println(MARKER)
        setupPopupMenu()
        setupRv()
        viewModel.getAllGames()
        println("PULLED DATA MATCHESFRAGMENT ${LoggedUserDataManager.getHostUserData()}")
        LoggedUserDataManager.getHostUsername()?.let { adapter.submitUserName(it) }

        lifecycleScope.launchWhenStarted {
            viewModel.usersAllGames.collectLatest {
                when(it){
                    is Resource.Success->{
                        it.data?.let { value->
                            binding.rvMatches.visibility = View.VISIBLE
                            binding.pb.visibility = View.INVISIBLE
                            val sortedValue = it.data.sortedByDescending {s-> s.getDateTime() }.toCollection(ArrayList())
                            adapter.submitList(sortedValue)
                        }
                    }
                    is Resource.Loading->{
                        binding.rvMatches.visibility = View.INVISIBLE
                        binding.pb.visibility = View.VISIBLE
                    }
                    is Resource.Unspecified->{
                        binding.rvMatches.visibility = View.INVISIBLE
                        binding.pb.visibility = View.INVISIBLE
                    }
                    else->Unit
                }
            }
        }






    }

    private fun setupRv() {
        adapter = MatchesRvAdapter()
        binding.rvMatches.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvMatches.adapter = adapter
    }

    private fun setupPopupMenu() {
        binding.btnMenu.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view,0,0,R.style.PopupMenuStyle)
        popupMenu.inflate(R.menu.popup_matches_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.createNewGame -> {
                    createNewGame()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun createNewGame() {
        findNavController().navigate(MatchesFragmentDirections.actionMatchesFragmentToCreateNewGameFragment())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}