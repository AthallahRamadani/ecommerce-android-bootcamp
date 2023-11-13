package com.athallah.ecommerce.fragment.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Cart
import com.athallah.ecommerce.databinding.FragmentCartBinding
import com.athallah.ecommerce.databinding.FragmentCheckoutBinding
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
        arguments?.let {
            viewModel.setData(it.parcelableArrayList(ARG_DATA) ?: ArrayList())
        }
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
                }
            }
        }
    }

    private fun setPrice(data: List<Cart>) {
        viewModel.totalPrice = data.sumOf{
            (it.productPrice + it.variantPrice) * it.quantity!!
        }
        binding.tvCheckoutPrice.text = viewModel.totalPrice.toCurrencyFormat()
    }

    private fun setupAction() {
        binding.cvPayment.setOnClickListener {  }
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


    companion object {
        const val ARG_DATA = "arg_data"
    }

}