package com.athallah.ecommerce.fragment.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.data.datasource.model.Fulfillment
import com.athallah.ecommerce.databinding.FragmentCheckoutBinding
import com.athallah.ecommerce.fragment.detail.DetailFragment
import com.athallah.ecommerce.fragment.payment.PaymentFragment
import com.athallah.ecommerce.fragment.status.StatusFragment
import com.athallah.ecommerce.utils.extension.getErrorMessage
import com.athallah.ecommerce.utils.parcelable
import com.athallah.ecommerce.utils.showSnackbar
import com.athallah.ecommerce.utils.toCurrencyFormat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckoutViewModel by viewModel()

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    private val checkoutAdapter: CheckoutAdapter by lazy {
        CheckoutAdapter(object : CheckoutAdapter.CartCallback {
            override fun itemClickCallback(productId: String) {
                moveToDetailProduct(productId)
            }

            override fun addQuantityCallback(cart: Cart) {
                viewModel.updateDataQuantity(cart, true)
            }

            override fun removeQuantityCallback(cart: Cart) {
                viewModel.updateDataQuantity(cart, false)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCheckoutPrice.text = viewModel.totalPrice.toCurrencyFormat()
        setupAdapter()
        setupAction()
        observeData()
        observePayment()
    }

    private fun observePayment() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.paymentState.collect { result ->
                    if (result != null) {
                        showLoading(result is ResultState.Loading)
                        when (result) {
                            is ResultState.Success -> {
                                sendLogPurchase(result.data)
                                actionToStatus(result.data)
                            }

                            is ResultState.Error -> {
                                val message = result.e.getErrorMessage()
                                binding.root.showSnackbar(message)
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun sendLogPurchase(data: Fulfillment) {
        firebaseAnalytics.logEvent(Event.PURCHASE) {
            val listCart = runBlocking { viewModel.listData.first() }
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
            param(FirebaseAnalytics.Param.CURRENCY, "IDR")
            param(FirebaseAnalytics.Param.VALUE, data.total.toLong())
            param(FirebaseAnalytics.Param.START_DATE, data.date)
            param(FirebaseAnalytics.Param.TRANSACTION_ID, data.invoiceId)
        }
    }

    private fun actionToStatus(data: Fulfillment) {
        val bundle = Bundle().apply {
            putParcelable(StatusFragment.DATA_BUNDLE_KEY, data)
        }
        findNavController().navigate(R.id.action_checkoutFragment_to_statusFragment, bundle)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnBuy.isInvisible = isLoading
        binding.cipBuy.isInvisible = !isLoading
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listData.collect { data ->
                    checkoutAdapter.submitList(data)
                    setPrice(data)
                    setPayment()
                    sendLogBeginCheckout(data)
                }
            }
        }
        setFragmentResultListener(PaymentFragment.REQUEST_KEY) { _, bundle ->
            viewModel.paymentItem =
                bundle.parcelable(PaymentFragment.BUNDLE_PAYMENT_KEY)
            setPayment()
        }
    }

    private fun sendLogBeginCheckout(listCart: List<Cart>) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT) {
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
            param(FirebaseAnalytics.Param.CURRENCY, "IDR")
            param(FirebaseAnalytics.Param.VALUE, viewModel.totalPrice.toLong())
        }
    }

    private fun setPayment() {
        if (viewModel.paymentItem != null) {
            binding.ivCard.load(viewModel.paymentItem!!.image) {
                crossfade(true)
                placeholder(R.drawable.product_image_placeholder)
                error(R.drawable.product_image_placeholder)
            }
            binding.tvChoosePayment.text = viewModel.paymentItem!!.label
            binding.btnBuy.isEnabled = true
        }
    }

    private fun setPrice(data: List<Cart>) {
        viewModel.totalPrice = data.sumOf {
            (it.productPrice + it.variantPrice) * it.quantity!!
        }
        binding.tvCheckoutPrice.text = viewModel.totalPrice.toCurrencyFormat()
    }

    private fun setupAction() {
        binding.cvPayment.setOnClickListener {
            findNavController().navigate(
                R.id.action_checkoutFragment_to_paymentFragment
            )
        }
        binding.btnBuy.setOnClickListener {
            viewModel.makePayment()
        }
        binding.topAppBar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupAdapter() {
        binding.rvCheckout.apply {
            adapter = checkoutAdapter
            layoutManager = object : LinearLayoutManager(requireActivity()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            itemAnimator = null
        }
    }

    private fun moveToDetailProduct(productId: String) {
        findNavController().navigate(
            R.id.action_cartFragment_to_detailFragment,
            bundleOf(DetailFragment.BUNDLE_PRODUCT_ID_KEY to productId)
        )
    }

    companion object {
        const val ARG_DATA = "arg_data"
    }
}
