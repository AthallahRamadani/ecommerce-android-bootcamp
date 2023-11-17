package com.athallah.ecommerce.fragment.main.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.databinding.FragmentStoreBinding
import com.athallah.ecommerce.databinding.FragmentTransactionBinding
import com.athallah.ecommerce.fragment.main.store.StoreViewModel
import com.athallah.ecommerce.fragment.status.StatusFragment
import com.athallah.ecommerce.utils.extension.toFulfillment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModel()

    private val transactionAdapter: TransactionAdapter by lazy{
        TransactionAdapter { data ->
            val bundle = Bundle().apply {
                putParcelable(StatusFragment.DATA_BUNDLE_KEY, data.toFulfillment())
                putInt(StatusFragment.RATING_BUNDLE_KEY, data.rating)
                putString(StatusFragment.REVIEW_BUNDLE_KEY, data.review)
            }
            Navigation.findNavController(requireActivity(), R.id.fcv_main).navigate(
                R.id.action_mainFragment_to_statusFragment,
                bundle
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeTransaction()
    }

    private fun observeTransaction() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transactionState.collect { result ->
                    if (result != null) {
                        showLoading(result is ResultState.Loading)
                        when (result) {
                            is ResultState.Loading -> {}
                            is ResultState.Success -> {
                                transactionAdapter.submitList(result.data)
                                if(result.data.isEmpty()) {
                                    binding.layoutEmpty.isVisible = true
                                    binding.rvTransaction.isVisible = false
                                    binding.btEmpty.setOnClickListener {
                                        viewModel.getListTransaction()
                                        binding.layoutEmpty.isVisible = false
                                    }
                                }
                            }
                            is ResultState.Error -> showErrorView(result.e)
                        }
                    }
                }
            }
        }
    }

    private fun showErrorView(error: Throwable) {
        when(error) {
            is retrofit2.HttpException -> {
                if (error.response()?.code() == 404) {
                    binding.layoutEmpty.isVisible = true
                    binding.rvTransaction.isVisible = false
                } else {
                    binding.layoutConnection.isVisible = true
                    binding.rvTransaction.isVisible = false
                    binding.tvTitleConnection.text = error.code().toString()
                    binding.tvSubtitleConnection.text = error.response()?.message().toString()
                }
            }

            is IOException -> {
                binding.layoutConnection.isVisible = true
                binding.rvTransaction.isVisible = false
            }

            else -> {
                binding.layoutServer.isVisible = true
                binding.rvTransaction.isVisible = false
            }
        }

        binding.btEmpty.setOnClickListener {
            viewModel.getListTransaction()
            binding.layoutEmpty.isVisible = false

        }

        binding.btServer.setOnClickListener {
            viewModel.getListTransaction()
            binding.btServer.isVisible = false
        }

        binding.btConnection.setOnClickListener {
            viewModel.getListTransaction()
            binding.layoutConnection.isVisible = false
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.cpiTransaction.isVisible =isLoading
        binding.rvTransaction.isVisible = !isLoading
    }

    private fun setupRecyclerView() {
        binding.rvTransaction.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

}