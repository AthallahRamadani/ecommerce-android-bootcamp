package com.athallah.ecommerce.fragment.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.di.dataStore
import com.athallah.ecommerce.databinding.FragmentLoginBinding
import com.athallah.ecommerce.databinding.FragmentMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.prefGetAccToken().isEmpty()) {
            findNavController().navigate(R.id.action_global_prelogin_navigation)
        } else if (viewModel.prefGetUsername().isEmpty()) {
            findNavController().navigate(R.id.action_mainFragment_to_profilFragment)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        Log.d("lol", "onViewCreated: ${viewModel.prefGetUsername()}")
    }

    private fun initView() {
        binding.topAppBar.title = viewModel.prefGetUsername()
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomNavView.setupWithNavController(navController)
    }
}