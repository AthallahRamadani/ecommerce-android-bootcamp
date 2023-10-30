package com.athallah.ecommerce.fragment.prelogin

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.databinding.FragmentLoginBinding
import com.athallah.ecommerce.utils.showSnackbar
import com.athallah.ecommerce.utils.textTrimmed
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModel()


    private fun initViewModel() {
        viewModel.prefGetIsOnboard().observe(viewLifecycleOwner) {
            if (it == false) {
                findNavController().navigate(R.id.action_loginFragment_to_onboardingFragment)
            }
        }

        viewModel.loginLiveData.observe(viewLifecycleOwner) { state ->
            Log.d("TAG", state::class.java.simpleName)
            if (state != null) {
                when (state) {
                    is ResultState.Error -> {
                        binding.root.showSnackbar(state.message)
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btMasuk.text = "Masuk"
                    }
                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btMasuk.text = ""
                    }
                    is ResultState.Success -> findNavController().navigate(R.id.action_global_main_navigation)
                }
            }
        }
    }


    private fun initViewBtnValid() {
        binding.btMasuk.isEnabled = false
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = p0.toString()
                binding.tilEmail.error =
                    if (!isEmailValid(text) && text.isNotEmpty()) getString(R.string.email_invalid) else null
                binding.btDaftar.isEnabled = true
            }

            override fun afterTextChanged(p0: Editable?) {
                updateButtonState()
            }
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = p0.toString()
                binding.tilPassword.error =
                    if (!isPasswordValid(text) && text.isNotEmpty()) getString(R.string.password_invalid) else null
                binding.btDaftar.isEnabled = true
            }

            override fun afterTextChanged(p0: Editable?) {
                updateButtonState()
            }
        })
    }

    private fun initView() {
        initViewBtnValid()
        ubahWarna()
        binding.btDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btMasuk.setOnClickListener {
            viewModel.login(binding.etEmail.textTrimmed, binding.etPassword.textTrimmed)
        }

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

    private fun ubahWarna() {
        val typedValue = TypedValue()
        val theme = requireContext().theme
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        @ColorInt val color = typedValue.data

        val fullText =
            "Dengan masuk disini, kamu menyetujui Syarat & Ketentuan serta Kebijakan Privasi TokoPhincon"
        val spannable = SpannableStringBuilder(fullText)

        val textToHighlight1 = "Syarat & Ketentuan"
        val textToHighlight2 = "Kebijakan Privasi"

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