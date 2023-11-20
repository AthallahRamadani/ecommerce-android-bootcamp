package com.athallah.ecommerce.fragment.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import com.athallah.ecommerce.MainActivity
import com.athallah.ecommerce.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModel()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeLogout()
        makeCrash()
    }

    private fun makeLogout() {
        binding.btnLogout.setOnClickListener {
            viewModel.makeLogout()
            (requireActivity() as MainActivity).logout()
        }
    }

    private fun makeCrash(){
        binding.btCrash.isGone = true
        binding.btCrash.text = "Test Crash"
        binding.btCrash.setOnClickListener {
            throw RuntimeException("Test Crash")
        }
    }
}