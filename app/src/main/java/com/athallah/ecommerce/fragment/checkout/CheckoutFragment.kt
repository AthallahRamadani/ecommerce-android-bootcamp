package com.athallah.ecommerce.fragment.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.databinding.FragmentCartBinding
import com.athallah.ecommerce.databinding.FragmentCheckoutBinding
import com.athallah.ecommerce.fragment.detail.DetailFragment
import com.athallah.ecommerce.fragment.payment.PaymentFragment
import com.athallah.ecommerce.utils.parcelable
import com.athallah.ecommerce.utils.parcelableArrayList
import com.athallah.ecommerce.utils.toCurrencyFormat
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckoutViewModel by viewModel()

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            viewModel.setData(it.parcelableArrayList(ARG_DATA) ?: ArrayList())
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listData.collect {data->
                    checkoutAdapter.submitList(data)
                    setPrice(data)
                    setPayment()
                }
            }
        }
        setFragmentResultListener(PaymentFragment.REQUEST_KEY) {_, bundle ->
            viewModel.paymentItem =
                bundle.parcelable(PaymentFragment.BUNDLE_PAYMENT_KEY)
            setPayment()
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
        viewModel.totalPrice = data.sumOf{
            (it.productPrice + it.variantPrice) * it.quantity!!
        }
        binding.tvCheckoutPrice.text = viewModel.totalPrice.toCurrencyFormat()
    }

    private fun setupAction() {
        binding.cvPayment.setOnClickListener { findNavController().navigate(R.id.action_checkoutFragment_to_paymentFragment)}
        binding.btnBuy.setOnClickListener {  }
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