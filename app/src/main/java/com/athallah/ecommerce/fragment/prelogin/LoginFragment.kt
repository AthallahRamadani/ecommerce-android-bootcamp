package com.athallah.ecommerce.fragment.prelogin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.databinding.FragmentLoginBinding
import com.athallah.ecommerce.utils.extension.getErrorMessage
import com.athallah.ecommerce.utils.showSnackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModel()
    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        checkNotificationPermission()
        Log.d("lol", "onViewCreated: ${viewModel.prefGetUsername()}")

    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
            val requestPermissionLauncher =
                registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { }
            if (!isPermissionsGranted(notificationPermission))
                requestPermissionLauncher.launch(notificationPermission)
        }
    }

    private fun isPermissionsGranted(permission: String) =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun initViewModel() {
        viewModel.prefGetIsOnboard().observe(viewLifecycleOwner) {
            if (it == false) {
                findNavController().navigate(R.id.action_loginFragment_to_onboardingFragment)
            }
        }

        viewModel.loginLiveData.observe(viewLifecycleOwner) { state ->
            if (state != null) {
                when (state) {
                    is ResultState.Error -> {
                        binding.root.showSnackbar(state.e.getErrorMessage())
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btMasuk.visibility = View.VISIBLE
                    }

                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btMasuk.visibility = View.INVISIBLE
                    }

                    is ResultState.Success -> {
                        viewModel.prefSetAccToken(state.data.accessToken ?: "")
                        viewModel.prefSetRefToken(state.data.refreshToken ?: "")
                        viewModel.prefSetUserName(state.data.userName?:"")
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                            param(FirebaseAnalytics.Param.METHOD, "email")
                        }
                        viewModel.setUserAuthorization(true)
                        findNavController().navigate(R.id.action_global_main_navigation)
                    }
                }
            }
        }
    }

    private fun initViewBtnValid() {
        binding.btMasuk.isEnabled = false
        binding.etEmail.doOnTextChanged { it, _, _, _ ->
            val text = it.toString()
            binding.tilEmail.error =
                if (!isEmailValid(text) && text.isNotEmpty()) getString(R.string.email_invalid) else null
            binding.btMasuk.isEnabled = true
            updateButtonState()
        }

        binding.etPassword.doOnTextChanged { it, _, _, _ ->
            val text = it.toString()
            binding.tilPassword.error =
                if (!isPasswordValid(text) && text.isNotEmpty()) getString(R.string.password_invalid) else null
            binding.btMasuk.isEnabled = true
            updateButtonState()
        }
    }

    private fun initView() {
        initViewBtnValid()
        changeColor()
        binding.btDaftar.setOnClickListener {
            firebaseAnalytics.logEvent("button_Click"){
                param("button_name", "button_register")
            }
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btMasuk.setOnClickListener {
            firebaseAnalytics.logEvent("button_Click"){
                param("button_name", "button_login")
            }
            viewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun updateButtonState() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val isEmailValid = isEmailValid(email)
        val isPasswordValid = isPasswordValid(password)
        binding.btMasuk.isEnabled = isEmailValid && isPasswordValid
    }

    private fun changeColor() {
        val typedValue = TypedValue()
        val theme = requireContext().theme
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        @ColorInt val color = typedValue.data

        val fullText = getString(R.string.agree)

        val spannable = SpannableStringBuilder(fullText)

        val textToHighlight1 = getString(R.string.term_condition)
        val textToHighlight2 = getString(R.string.privacy_policy)

        val start1 = fullText.indexOf(textToHighlight1)
        val start2 = fullText.indexOf(textToHighlight2)

        if (start1 != -1) {
            spannable.setSpan(
                ForegroundColorSpan(color),
                start1,
                start1 + textToHighlight1.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }

        if (start2 != -1) {
            spannable.setSpan(
                ForegroundColorSpan(color),
                start2,
                start2 + textToHighlight2.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }

        binding.tvAgree.text = spannable
    }

}
