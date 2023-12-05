package com.athallah.ecommerce.fragment.cart

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.databinding.FragmentCartBinding
import com.athallah.ecommerce.fragment.checkout.CheckoutFragment
import com.athallah.ecommerce.fragment.detail.DetailFragment
import com.athallah.ecommerce.utils.toCurrencyFormat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment() {

    private val viewModel: CartViewModel by viewModel()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    private val cartAdapter: CartAdapter by lazy {
        CartAdapter(object : CartAdapter.CartCallback {
            override fun itemClickCallback(productId: String) {
                moveToDetailProduct(productId)
            }

            override fun deleteItemCallback(cart: Cart) {
                viewModel.deleteCart(cart)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART) {
                    val bundleProduct = Bundle().apply {
                        putString(FirebaseAnalytics.Param.ITEM_ID, cart.productId)
                        putString(FirebaseAnalytics.Param.ITEM_NAME, cart.productName)
                        putString(FirebaseAnalytics.Param.ITEM_BRAND, cart.brand)
                        putString(FirebaseAnalytics.Param.ITEM_VARIANT, cart.variantName)
                    }
                    param(FirebaseAnalytics.Param.ITEMS, arrayOf(bundleProduct))
                }
            }

            override fun addQuantityCallback(cart: Cart) {
                viewModel.updateCartQuantity(cart, true)
            }

            override fun removeQuantityCallback(cart: Cart) {
                viewModel.updateCartQuantity(cart, false)
            }

            override fun checkItemCallback(cart: Cart, isCheck: Boolean) {
                viewModel.updateCartChecked(isCheck, cart)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCartPrice.text = viewModel.totalPrice.toCurrencyFormat()
        setupAdapter()
        setupAction()
        observeCart()
    }

    private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartData().collect { data ->
                    showIsDataEmpty(data.isEmpty())
                    cartAdapter.submitList(data)
                    sendLogCart(data.toTypedArray(), FirebaseAnalytics.Event.VIEW_CART)
                    setView(data)
                }
            }
        }
    }

    private fun sendLogCart(listCart: Array<Cart>, event: String) {
        firebaseAnalytics.logEvent(event) {
            val bundle = ArrayList<Bundle>()
            listCart.map { cart ->
                val itemBundle = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_ID, cart.productId)
                    putString(FirebaseAnalytics.Param.ITEM_NAME, cart.productName)
                    putString(FirebaseAnalytics.Param.ITEM_BRAND, cart.brand)
                    putString(FirebaseAnalytics.Param.ITEM_VARIANT, cart.variantName)
                }
                bundle.add(itemBundle)
            }
            param(FirebaseAnalytics.Param.ITEMS, bundle.toTypedArray())
        }
    }

    private fun setView(listCart: List<Cart>) {
        binding.cbAllCart.isChecked = listCart.all {
            it.isChecked
        }
        binding.btnDelete.isVisible = listCart.any { it.isChecked }
        binding.btnBuy.isEnabled = listCart.any { it.isChecked }
        viewModel.totalPrice = listCart.filter { it.isChecked }.sumOf {
            (it.productPrice + it.variantPrice) * it.quantity!!
        }
        binding.tvCartPrice.text = viewModel.totalPrice.toCurrencyFormat()
    }

    private fun showIsDataEmpty(isEmpty: Boolean) {
        binding.layoutEmpty.isVisible = isEmpty

        val viewsToToggle = listOf(
            binding.cbAllCart,
            binding.tvChooseAll,
            binding.btnDelete,
            binding.divider,
            binding.rvCart,
            binding.divider2,
            binding.tvCartPrice,
            binding.tvTotalPrice,
            binding.btnBuy
        )

        viewsToToggle.forEach { view ->
            view.isVisible = !isEmpty
        }
    }

    private fun setupAction() {
        binding.btnDelete.setOnClickListener { viewModel.deleteCart() }
        binding.cbAllCart.setOnClickListener {
            val isAllChecked = binding.cbAllCart.isChecked
            viewModel.updateCartChecked(isAllChecked)
        }
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnBuy.setOnClickListener {
            val listData = runBlocking { viewModel.cartData().first().filter { it.isChecked } }
            val bundle = Bundle().apply {
                putParcelableArrayList(CheckoutFragment.ARG_DATA, ArrayList<Cart>(listData))
            }
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment, bundle)
        }
    }

    private fun setupAdapter() {
        binding.rvCart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            itemAnimator = null
        }
    }

    private fun moveToDetailProduct(productId: String) {
        findNavController().navigate(
            R.id.action_cartFragment_to_detailFragment,
            bundleOf(DetailFragment.BUNDLE_PRODUCT_ID_KEY to productId)
        )
    }
}
