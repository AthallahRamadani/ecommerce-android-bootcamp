package com.athallah.ecommerce.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.api.model.DetailProduct
import com.athallah.ecommerce.databinding.FragmentDetailBinding
import com.athallah.ecommerce.fragment.review.ReviewFragment
import com.athallah.ecommerce.utils.toCurrencyFormat
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModel()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDetailProduct()
        initAction()
    }

    private fun initAction() {
        binding.ivShare.setOnClickListener {
            actionShare()
        }
        binding.ivFavorite.setOnClickListener {
            actionWishlist()
        }
        binding.btSeeAll.setOnClickListener {
            actionOpenReview()
        }
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun actionOpenReview() {
        findNavController().navigate(
            R.id.action_detailFragment_to_reviewFragment,
            bundleOf(ReviewFragment.BUNDLE_PRODUCT_ID_KEY to viewModel.productId)
        )
    }

    private fun actionWishlist() {

    }

    private fun actionShare() {

    }

    private fun observeDetailProduct() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailProductState.collect {
                    if (it != null) {
                        showLoading(it is ResultState.Loading)
                        when (it) {
                            is ResultState.Loading -> {}
                            is ResultState.Success -> setView(it.data)
                            is ResultState.Error -> showError()
                        }
                    }
                }
            }
        }
    }

    private fun showError() {
        binding.layoutEmpty.isVisible = true
        binding.clDetail.isVisible = false
        binding.llBottom.isVisible = false
        binding.dividerBottom.isVisible = false

        binding.btEmpty.setOnClickListener { viewModel.getDetailProduct() }
    }

    private fun setView(data: DetailProduct) {
        with(binding) {
            setImageView(data.image)
            tvDetailPrice.text = data.productPrice.toCurrencyFormat()
            tvDetailName.text = data.productName
            tvDetailSale.text = getString(R.string.total_sales, data.sale)
            chipRating.text =
                getString(
                    R.string.detail_rating_and_total_rating,
                    data.productRating,
                    data.totalRating
                )
            setChipVariants(data.productVariant, data.productPrice)
            tvDetailDescription.text = data.description
            tvDetailSatisfaction.text =
                getString(R.string.text_satisfaction, data.totalSatisfaction)
            tvDetailRatingReview.text =
                getString(R.string.rating_and_review_total, data.totalRating, data.totalReview)
            tvDetailRating.text = String.format("%.1f", data.productRating)
        }
    }

    private fun setChipVariants(
        productVariant: List<DetailProduct.ProductVariant>,
        price: Int
    ) {
        binding.chipVariantGroup.removeAllViews()
        productVariant.forEach { variant ->
            val chip = Chip(binding.chipVariantGroup.context)
            chip.text = variant.variantName
            chip.isCheckable = true
            chip.isChecked = variant.variantName == productVariant[0].variantName

            chip.setOnClickListener {
                binding.tvDetailPrice.text =
                    (price + variant.variantPrice).toCurrencyFormat()

                viewModel.detailProduct =
                    viewModel.detailProduct?.copy(productPrice = price + variant.variantPrice)
                viewModel.variant = variant.variantName
            }

            binding.chipVariantGroup.addView(chip)
        }
    }

    private fun setImageView(image: List<String>) {
        val adapter = DetailAdapter()
        adapter.submitList(image)
        val pager = binding.pagerDetail
        pager.adapter = adapter

        if (image.size > 1) TabLayoutMediator(
            binding.tabLayout,
            pager
        ) { _, _ -> }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.clDetail.isVisible = !isLoading
        binding.llBottom.isVisible = !isLoading
        binding.cpiLoading.isVisible = isLoading
        binding.dividerBottom.isVisible = !isLoading
        binding.layoutEmpty.isVisible = false
    }


    companion object {
        const val BUNDLE_PRODUCT_ID_KEY = "product_id"
    }
}