package com.athallah.ecommerce.fragment.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.databinding.FragmentReviewBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewFragment : Fragment() {
    private val viewModel: ReviewViewModel by viewModel()
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private val adapter = ReviewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArgs()
        setupAppbarNavigation()
        setupRecyclerView()
        observeReviewProduct()
    }

    private fun observeReviewProduct() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reviewProductState.collect { result ->
                    if (result != null) {
                        showLoading(result is ResultState.Loading)
                        when (result) {
                            is ResultState.Loading -> {}
                            is ResultState.Success -> adapter.submitList(result.data)
                            is ResultState.Error -> showError()
                        }
                    }
                }
            }
        }
        viewModel.getListReview()
    }

    private fun showError() {
        binding.layoutEmpty.isVisible = true
        binding.btEmpty.setOnClickListener { viewModel.getListReview() }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.rvReview.isVisible = !isLoading
        binding.cpiLoading.isVisible = isLoading
        binding.layoutEmpty.isVisible = false
    }

    private fun setupRecyclerView() {
        binding.rvReview.adapter = adapter
        binding.rvReview.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setupAppbarNavigation() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupArgs() {
        viewModel.productId = arguments?.getString(BUNDLE_PRODUCT_ID_KEY)
    }

    companion object {
        const val BUNDLE_PRODUCT_ID_KEY = "product_id"
    }
}
