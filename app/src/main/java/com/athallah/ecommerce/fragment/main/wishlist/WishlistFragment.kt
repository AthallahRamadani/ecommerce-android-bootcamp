package com.athallah.ecommerce.fragment.main.wishlist

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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.model.Wishlist
import com.athallah.ecommerce.databinding.FragmentWishlistBinding
import com.athallah.ecommerce.fragment.detail.DetailFragment
import com.athallah.ecommerce.utils.showSnackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WishlistViewModel by viewModel()

    private val wishlistAdapter: WishlistAdapter by lazy {
        WishlistAdapter(object : WishlistAdapter.WishlistCallback {
            override fun itemClickCallback(productId: String) {
                moveToDetailProduct(productId)
            }

            override fun deleteCallback(wishlist: Wishlist) {
                viewModel.deleteWishlist(wishlist)
                binding.root.showSnackbar(getString(R.string.wishlist_remove_successful))
            }

            override fun addCartCallback(wishlist: Wishlist) {
                actionAddCart(wishlist)
            }
        })
    }

    private fun moveToDetailProduct(productId: String) {
        Navigation.findNavController(requireActivity(), R.id.fcv_main).navigate(
            R.id.action_mainFragment_to_detailFragment,
            bundleOf(DetailFragment.BUNDLE_PRODUCT_ID_KEY to productId)
        )
    }

    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupAction()
        observeWishlist()
    }

    private fun observeWishlist() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.wishlistData.collect{data->
                    showIsDataEmpty(data.isEmpty())
                    binding.tvWishlistTotalItem.text =
                        getString(R.string.total_wishlist_item,data.size)
                    wishlistAdapter.submitList(data)
                }
            }
        }
    }

    private fun showIsDataEmpty(isEmpty: Boolean) {
        binding.tvWishlistTotalItem.isVisible = !isEmpty
        binding.ivWishlist.isVisible = !isEmpty
        binding.rvWishlistItem.isVisible = !isEmpty
        binding.view.isVisible = !isEmpty
        binding.layoutEmpty.isVisible = isEmpty
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun setupAction() {
        binding.ivWishlist.setOnClickListener {
            viewModel.recyclerViewType =
                if (viewModel.recyclerViewType == WishlistAdapter.ONE_COLUMN_VIEW_TYPE) {
                    WishlistAdapter.MORE_COLUMN_VIEW_TYPE
                } else {
                    WishlistAdapter.ONE_COLUMN_VIEW_TYPE
                }
            changeRecyclerViewType()
            setImageBtnChangeRecyclerView()
        }
    }

    private fun initView() {
        setAdapter()
        setImageBtnChangeRecyclerView()
        changeRecyclerViewType()
    }

    private fun changeRecyclerViewType() {
        if (viewModel.recyclerViewType == WishlistAdapter.ONE_COLUMN_VIEW_TYPE) {
            wishlistAdapter.viewType = WishlistAdapter.ONE_COLUMN_VIEW_TYPE
            gridLayoutManager.spanCount = 1
        } else {
            wishlistAdapter.viewType = WishlistAdapter.MORE_COLUMN_VIEW_TYPE
            gridLayoutManager.spanCount = 2
        }
    }

    private fun setImageBtnChangeRecyclerView() {
        binding.ivWishlist.setImageResource(
            if (viewModel.recyclerViewType == WishlistAdapter.ONE_COLUMN_VIEW_TYPE)
                R.drawable.baseline_grid_view_24
            else
                R.drawable.baseline_format_list_bulleted_24
        )
    }

    private fun setAdapter() {
        gridLayoutManager = GridLayoutManager(requireActivity(), 1)

        binding.rvWishlistItem.adapter = wishlistAdapter
        binding.rvWishlistItem.layoutManager = gridLayoutManager

        wishlistAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvWishlistItem.scrollToPosition(0)
            }
        })
    }

    private fun actionAddCart(wishlist: Wishlist) {
        if (viewModel.insertCart(wishlist))
            binding.root.showSnackbar(getString(R.string.cart_insert_successful))
        else
            binding.root.showSnackbar(getString(R.string.stock_is_unavailable))
    }


}