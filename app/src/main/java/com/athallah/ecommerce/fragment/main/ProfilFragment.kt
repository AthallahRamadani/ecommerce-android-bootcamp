package com.athallah.ecommerce.fragment.main

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.athallah.ecommerce.R
import com.athallah.ecommerce.databinding.FragmentLoginBinding
import com.athallah.ecommerce.databinding.FragmentProfilBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var tempUri: Uri
    private val viewModel: ProfilViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ubahWarna()

        binding.btSelesai.isEnabled = false

        binding.etNama.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                updateButtonState()
            }

        })


        binding.ivImage.setOnClickListener {
            val items = arrayOf("Kamera", "Galeri")

            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.ThemeOverlay_App_MaterialAlertDialog
            )
                .setTitle(resources.getString(R.string.title_dialog))
                .setItems(items) { dialog, which ->
                    when (which) {
                        0 -> {

                            actionOpenCamera()
                        }

                        1 -> {
                            actionOpenGallery()
                        }
                    }
                }
                .show()
        }
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
            val toast = Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG) // in Activity
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
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                viewModel.currentImageUri = uri
                showImage()
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private fun uploadProfileData() {
        Log.d("UplodProfil", "uploadprofil")
    }


    private fun dispatchTakePictureIntent() {
        val REQUEST_IMAGE_CAPTURE = 1
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
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