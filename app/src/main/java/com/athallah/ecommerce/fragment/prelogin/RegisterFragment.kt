package com.athallah.ecommerce.fragment.prelogin

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
import androidx.annotation.ColorInt
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.databinding.FragmentRegisterBinding
import com.athallah.ecommerce.utils.extension.getErrorMessage
import com.athallah.ecommerce.utils.showSnackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModel()
    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("lol", "onViewCreated: ${viewModel.prefGetUsername()}")
        initViewModel()
        initView()
    }

    private fun initViewModel() {
        viewModel.registerLiveData.observe(viewLifecycleOwner) { state ->
            if (state != null) {
                when (state) {
                    is ResultState.Error -> {
                        binding.root.showSnackbar(state.e.getErrorMessage())
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btDaftar.visibility = View.VISIBLE
                    }

                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btDaftar.visibility = View.INVISIBLE
                    }

                    is ResultState.Success -> {
                        viewModel.prefSetAccToken(state.data.accessToken ?: "")
                        viewModel.prefSetRefToken(state.data.refreshToken ?: "")
                        viewModel.setUserAuthorization(true)
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
                            param(FirebaseAnalytics.Param.METHOD, "email")
                        }
                        findNavController().navigate(R.id.action_global_main_navigation)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun initViewBtnValid() {
        binding.btDaftar.isEnabled = false
        binding.etEmail.doOnTextChanged { it, _, _, _ ->
            val text = it.toString()
            binding.tilEmail.error =
                if (!isEmailValid(text) && text.isNotEmpty()) getString(R.string.email_invalid) else null
            binding.btDaftar.isEnabled = true
            updateButtonState()
        }

        binding.etPassword.doOnTextChanged { it, _, _, _ ->
            val text = it.toString()
            binding.tilPassword.error =
                if (!isPasswordValid(text) && text.isNotEmpty()) getString(R.string.password_invalid) else null
            binding.btDaftar.isEnabled = true
            updateButtonState()
        }
    }

    private fun initView() {
        initViewBtnValid()
        changeColor()
        binding.btMasuk.setOnClickListener {
            firebaseAnalytics.logEvent("button_Click"){
                param("button_name", "button_login")
            }
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btDaftar.setOnClickListener {
            firebaseAnalytics.logEvent("button_Click"){
                param("button_name", "button_register")
            }
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            viewModel.register(
                email = email,
                password = pass
            )
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
        binding.btDaftar.isEnabled = isEmailValid && isPasswordValid
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
