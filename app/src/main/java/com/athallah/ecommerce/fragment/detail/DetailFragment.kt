package com.athallah.ecommerce.fragment.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.athallah.ecommerce.data.datasource.model.DetailProduct
import com.athallah.ecommerce.databinding.FragmentDetailBinding
import com.athallah.ecommerce.fragment.checkout.CheckoutFragment
import com.athallah.ecommerce.fragment.review.ReviewFragmentCompose
import com.athallah.ecommerce.utils.extension.toCart
import com.athallah.ecommerce.utils.showSnackbar
import com.athallah.ecommerce.utils.toCurrencyFormat
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModel()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDetailProduct()
        initAction()
        setupWishlist()
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
        binding.btCart.setOnClickListener {
            actionAddCart()
            Log.d("TAG", "initAction: ${viewModel.detailProduct}")
        }

        binding.btBuyNow.setOnClickListener {
            if (viewModel.detailProduct != null && viewModel.productVariant != null) {
                val data = viewModel.detailProduct?.toCart(viewModel.productVariant!!)
                val bundle = Bundle().apply {
                    putParcelableArrayList(CheckoutFragment.ARG_DATA, arrayListOf(data))
                }
                findNavController().navigate(R.id.action_detailFragment_to_checkoutFragment, bundle)
            }
        }
    }

    private fun actionAddCart() {
        if (viewModel.insertCart()) {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
                val bundleProduct = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_ID, viewModel.detailProduct!!.productId)
                    putString(
                        FirebaseAnalytics.Param.ITEM_NAME,
                        viewModel.detailProduct!!.productName
                    )
                    putString(FirebaseAnalytics.Param.ITEM_BRAND, viewModel.detailProduct!!.brand)
                    putString(
                        FirebaseAnalytics.Param.ITEM_VARIANT,
                        viewModel.productVariant!!.variantName
                    )
                }
                param(FirebaseAnalytics.Param.ITEMS, arrayOf(bundleProduct))
            }
            binding.root.showSnackbar(getString(R.string.cart_insert_successful))
        } else {
            binding.root.showSnackbar(getString(R.string.stock_is_unavailable))
        }
    }

    private fun actionOpenReview() {
        findNavController().navigate(
            R.id.action_detailFragment_to_reviewFragmentCompose,
            bundleOf(ReviewFragmentCompose.BUNDLE_PRODUCT_ID_KEY to viewModel.productId)
        )
    }

    private fun actionWishlist() {
        if (viewModel.isWishlist) {
            viewModel.deleteWishlist()
            binding.root.showSnackbar("Berhasil menghapus wishlist")
        } else {
            viewModel.insertWishlist()
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST) {
                val bundleProduct = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_ID, viewModel.detailProduct!!.productId)
                    putString(
                        FirebaseAnalytics.Param.ITEM_NAME,
                        viewModel.detailProduct!!.productName
                    )
                    putString(FirebaseAnalytics.Param.ITEM_BRAND, viewModel.detailProduct!!.brand)
                    putString(
                        FirebaseAnalytics.Param.ITEM_VARIANT,
                        viewModel.productVariant!!.variantName
                    )
                }
                param(FirebaseAnalytics.Param.ITEMS, arrayOf(bundleProduct))
            }
            binding.root.showSnackbar("Berhasil menambahkan wishlist")
        }
        setWishlistIcon()
    }

    private fun setupWishlist() {
        viewModel.isWishlist = viewModel.checkExistWishlist()
        setWishlistIcon()
    }

    private fun setWishlistIcon() {
        binding.ivFavorite.setImageResource(
            if (viewModel.isWishlist) {
                R.drawable.baseline_favorite_purple_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
        )
    }

    private fun actionShare() {
        val sendIntent: Intent = Intent().apply {
            val text = """
                Name : ${viewModel.detailProduct?.productName}
                Price : ${viewModel.detailProduct?.productPrice?.toCurrencyFormat()}
                Link : https://ecommerce.athallah.com/products/${viewModel.productId}
            """.trimIndent()
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun observeDetailProduct() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailProductState.collect {
                    if (it != null) {
                        showLoading(it is ResultState.Loading)
                        when (it) {
                            is ResultState.Loading -> {}
                            is ResultState.Success -> setData(it.data)
                            is ResultState.Error -> showError()
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun setData(data: DetailProduct) {
        viewModel.detailProduct = data
        if (viewModel.productVariant == null) {
            viewModel.productVariant = data.productVariant[0]
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
            val bundleProduct = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, data.productId)
                putString(FirebaseAnalytics.Param.ITEM_NAME, data.productName)
                putString(FirebaseAnalytics.Param.ITEM_BRAND, data.brand)
            }
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(bundleProduct))
        }
        setView(data)
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
            tvDetailPrice.text =
                data.productPrice.plus(viewModel.productVariant!!.variantPrice).toCurrencyFormat()
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
            chip.isChecked = variant.variantName == viewModel.productVariant!!.variantName

            chip.setOnClickListener {
                binding.tvDetailPrice.text =
                    (price + variant.variantPrice).toCurrencyFormat()

                viewModel.detailProduct =
                    viewModel.detailProduct?.copy(productPrice = price)
                viewModel.productVariant = variant
            }

            binding.chipVariantGroup.addView(chip)
        }
    }

    private fun setImageView(image: List<String>) {
        val adapter = DetailAdapter()
        adapter.submitList(image)
        val pager = binding.pagerDetail
        pager.adapter = adapter

        if (image.size > 1) {
            TabLayoutMediator(
                binding.tabLayout,
                pager
            ) { _, _ -> }.attach()
        }
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
