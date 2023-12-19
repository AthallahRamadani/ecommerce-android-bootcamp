package com.athallah.ecommerce.fragment.main.profile

import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.databinding.FragmentProfilBinding
import com.athallah.ecommerce.fragment.main.`object`.PhotoLoaderManager
import com.athallah.ecommerce.utils.extension.getErrorMessage
import com.athallah.ecommerce.utils.extension.toMultipartBody
import com.athallah.ecommerce.utils.showSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var tempUri: Uri
    private val viewModel: ProfileViewModel by viewModel()
    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeColor()

        binding.btSelesai.isEnabled = false

        binding.etNama.doOnTextChanged { _, _, _, _ ->
            binding.btSelesai.isEnabled = true
            updateButtonState()
        }

        binding.ivImage.setOnClickListener {
            val items = arrayOf("Kamera", "Galeri")

            MaterialAlertDialogBuilder(
                requireContext(),
                R.drawable.background_dialog
            ).setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.background_dialog
                )
            ).setTitle(resources.getString(R.string.title_dialog)).setItems(items) { _, which ->
                when (which) {
                    0 -> {
                        actionOpenCamera()
                    }

                    1 -> {
                        actionOpenGallery()
                    }
                }
            }.show()
        }
        observeUpload()

        binding.btSelesai.setOnClickListener {
            firebaseAnalytics.logEvent("button_Click") {
                param("button_name", "button_done")
            }
            uploadProfileData()
        }
    }

    private fun uploadProfileData() {
        val imageFile = viewModel.currentImageUri?.let { uri ->
            PhotoLoaderManager.uriToFile(
                uri,
                requireActivity()
            )
        }
        val name = binding.etNama.text.toString()
        val nameRequestBody = name.toRequestBody()
        val imageMultipartBody = imageFile?.toMultipartBody("userImage")
        viewModel.uploadProfile(nameRequestBody, imageMultipartBody)
    }

    private fun actionOpenCamera() {
        tempUri = PhotoLoaderManager.buildNewUri(requireActivity())
        cameraIntentLauncher.launch(tempUri)
    }

    private val cameraIntentLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.currentImageUri = tempUri
            showImage()
        } else {
            val toast = Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private fun showImage() {
        binding.ivImage.setImageURI(viewModel.currentImageUri)
    }

    private fun actionOpenGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.currentImageUri = uri
                showImage()
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private fun updateButtonState() {
        val nama = binding.etNama.text.toString()

        val isNamaValid = isNamaValid(nama)

        binding.btSelesai.isEnabled = isNamaValid
    }

    private fun isNamaValid(nama: String): Boolean {
        return nama.isNotEmpty()
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

    private fun observeUpload() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uploadState.collect { state ->
                    if (state != null) {
                        when (state) {
                            is ResultState.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.btSelesai.visibility = View.INVISIBLE
                            }

                            is ResultState.Error -> {
                                binding.root.showSnackbar(state.e.getErrorMessage())
                                binding.progressBar.visibility = View.INVISIBLE
                                binding.btSelesai.visibility = View.VISIBLE
                            }

                            is ResultState.Success -> {
                                viewModel.prefSetUsername(binding.etNama.text.toString())
                                findNavController().navigate(
                                    R.id.action_profilFragment_to_mainFragment
                                )
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}
