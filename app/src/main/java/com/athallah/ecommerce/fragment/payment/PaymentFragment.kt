package com.athallah.ecommerce.fragment.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.databinding.FragmentPaymentBinding
import com.athallah.ecommerce.fragment.payment.adapter.PaymentAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class PaymentFragment : Fragment() {
    private val viewModel: PaymentViewModel by viewModel()
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val paymentAdapter: PaymentAdapter by lazy {
        PaymentAdapter { paymentItem ->
            val bundle = Bundle().apply {
                putParcelable(BUNDLE_PAYMENT_KEY, paymentItem)
            }
            setFragmentResult(REQUEST_KEY, bundle)
            findNavController().navigateUp()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observePayment()
        setupAction()
    }

    private fun setupAction() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observePayment() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.paymentState.collect{result->
                    if (result != null) {
                        showLoading(result is ResultState.Loading)
                        when(result) {
                            is ResultState.Success -> paymentAdapter.submitList(result.data)
                            is ResultState.Error -> showError(result.e)
                            else -> {}
                        }
                    }
                }
            }
        }
        viewModel.getPayment()
    }

    private fun showError(error: Throwable) {
        when(error) {
            is retrofit2.HttpException -> {
                if (error.response()?.code() == 404) {
                    binding.layoutEmpty.isVisible = true
                    binding.rvPayment.isVisible = false
                } else {
                    binding.layoutConnection.isVisible = true
                    binding.rvPayment.isVisible = false
                    binding.tvTitleConnection.text = error.code().toString()
                    binding.tvSubtitleConnection.text = error.response()?.message().toString()
                }
            }

            is IOException -> {
                binding.layoutConnection.isVisible = true
                binding.rvPayment.isVisible = false
            }

            else -> {
                binding.layoutConnection.isVisible = true
                binding.rvPayment.isVisible = false
            }
        }

        binding.btEmpty.setOnClickListener {
            viewModel.getPayment()
            binding.layoutEmpty.isVisible = false
        }

        binding.btServer.setOnClickListener {
            viewModel.getPayment()
            binding.btServer.isVisible = false
        }

        binding.btConnection.setOnClickListener {
            viewModel.getPayment()
            binding.layoutConnection.isVisible = false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cipLoading.isVisible = isLoading
        binding.rvPayment.isVisible = !isLoading
    }

    private fun setupRecyclerView() {
        val divider = MaterialDividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL)
        divider.isLastItemDecorated = false
        divider.dividerThickness = 12


        binding.rvPayment.apply {
            adapter = paymentAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            addItemDecoration(divider)
        }
    }

    companion object {
        const val REQUEST_KEY = "payment_request_key"
        const val BUNDLE_PAYMENT_KEY = "payment_bundle_key"
    }
}