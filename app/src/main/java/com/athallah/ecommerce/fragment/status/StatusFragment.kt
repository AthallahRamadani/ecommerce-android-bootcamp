package com.athallah.ecommerce.fragment.status

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.databinding.FragmentStatusBinding
import com.athallah.ecommerce.fragment.main.MainFragment
import com.athallah.ecommerce.utils.extension.getErrorMessage
import com.athallah.ecommerce.utils.showSnackbar
import com.athallah.ecommerce.utils.toCurrencyFormat
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatusFragment : Fragment() {

    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatusViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackPresser()
        setupView()
        setupAction()
        observeRating()
    }

    private fun setupAction() {
        binding.btnFinish.setOnClickListener {
            val rating: Int? = with(binding.rbStatus.rating.toInt()) {
                if (this == 0) {
                    null
                } else {
                    this
                }
            }
            val review: String? = with(binding.etLeaveReview.text.toString()) {
                this.ifEmpty { null }
            }
            val invoiceId = viewModel.detailTransaction!!.invoiceId
            viewModel.sendRating(invoiceId, rating, review)
            Log.d("rating", "setupAction: $invoiceId, $review, $rating")
        }
    }

    private fun observeRating() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ratingState.collect { result ->
                    if (result != null) {
                        showLoading(result is ResultState.Loading)
                        when (result) {
                            is ResultState.Loading -> {}
                            is ResultState.Success -> if (result.data) actionToMovePage(true)
                            is ResultState.Error -> {
                                val message = result.e.getErrorMessage()
                                binding.root.showSnackbar(message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupBackPresser() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    actionToMovePage()
                }
            }
        )
    }

    private fun actionToMovePage(isBtnFinishClicked: Boolean = false) {
        val backStackEntryCount: Int = parentFragmentManager.backStackEntryCount
        if (backStackEntryCount == 0) {
            findNavController().navigate(R.id.action_statusFragment_to_mainFragment)
        } else {
            if (isBtnFinishClicked) {
                val bundle = Bundle().apply {
                    putBoolean(MainFragment.MOVE_TRANSACTION_BUNDLE_KEY, true)
                }
                findNavController().navigate(R.id.action_statusFragment_to_mainFragment, bundle)
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnFinish.isInvisible = isLoading
        binding.cpiFinish.isInvisible = !isLoading
    }

    private fun setupView() {
        val data = viewModel.detailTransaction
        with(binding) {
            tvIdResult.text = data?.invoiceId
            tvStatusResult.text =
                if (data?.status == true) {
                    getString(R.string.success)
                } else {
                    getString(R.string.failed)
                }
            tvDateResult.text = data?.date
            tvTimeResult.text = data?.time
            tvPaymentMethodResult.text = data?.payment
            tvPaymentTotalResult.text = data?.total?.toCurrencyFormat()
            rbStatus.rating = viewModel.rating?.toFloat() ?: 0F
            etLeaveReview.setText(viewModel.review)
        }
    }

    companion object {
        const val RATING_BUNDLE_KEY = "rating_bundle_key"
        const val REVIEW_BUNDLE_KEY = "review_bundle_key"
        const val DATA_BUNDLE_KEY = "data_bundle_key"
    }
}
