package com.athallah.ecommerce.fragment.prelogin

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.source.local.preference.SharedPref
import com.athallah.ecommerce.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isFirstLaunch()){
            findNavController().navigate(R.id.action_loginFragment_to_onboardingFragment)
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

        pindahFragment()
        ubahWarna()

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

    private fun isPasswordValid(password: String): Boolean {
        return password.length >=8
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

    private fun pindahFragment() {
        binding.btDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btMasuk.setOnClickListener {
            findNavController().navigate(R.id.action_global_main_navigation)
        }
    }

    private fun ubahWarna() {
        val typedValue = TypedValue()
        val theme = requireContext().theme
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        @ColorInt val color = typedValue.data

        val fullText = "Dengan masuk disini, kamu menyetujui Syarat & Ketentuan serta Kebijakan Privasi TokoPhincon"
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