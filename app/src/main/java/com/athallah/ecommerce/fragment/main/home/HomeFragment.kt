package com.athallah.ecommerce.fragment.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.athallah.ecommerce.MainActivity
import com.athallah.ecommerce.databinding.FragmentHomeBinding
import com.athallah.ecommerce.utils.Helper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeCrash()
        initSwitchButton()
        initAction()
    }

    private fun initSwitchButton() {
        binding.switchTheme.isChecked = runBlocking {
            viewModel.getAppTheme().first()
        }
        binding.switchLanguage.isChecked = runBlocking {
            viewModel.getAppLanguage().first() == "id"
        }
    }

    private fun initAction() {
        binding.btnLogout.setOnClickListener {
            viewModel.makeLogout()
            (requireActivity() as MainActivity).logout()
        }
        binding.switchTheme.setOnCheckedChangeListener { _, isDarkTheme ->
            viewModel.setAppTheme(isDarkTheme)
            Helper.setAppTheme(requireActivity(), isDarkTheme)
        }
        binding.switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setAppLanguage(isChecked)
            val appLanguage = if (isChecked) "id" else "en"
            Helper.setAppLanguage(requireActivity(), appLanguage)
            requireActivity().recreate()
        }
    }

    private fun makeCrash() {
        binding.btCrash.isGone = true
        binding.btCrash.text = "Test Crash"
        binding.btCrash.setOnClickListener {
            throw RuntimeException("Test Crash")
        }
    }
}
