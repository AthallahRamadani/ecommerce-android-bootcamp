package com.athallah.ecommerce.fragment.main.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.api.request.ProductsQuery
import com.athallah.ecommerce.databinding.FragmentStoreBinding
import com.athallah.ecommerce.fragment.main.store.storeadapter.LoadingAdapter
import com.athallah.ecommerce.fragment.main.store.storeadapter.ProductAdapter
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class StoreFragment : Fragment() {
    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StoreViewModel by viewModel()
    private val productAdapter = ProductAdapter()
    private val gridLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(requireActivity(), 1)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setAdapter()
        setRecyclerView()
        observeProducts()
        getAccToken()
        actionSetup()
        getFilterFragmentResult()
    }

    private fun getAccToken() {
        viewModel.prefGetAccToken()
    }

    private fun setAdapter() {
        binding.includeContent.rvStore.adapter =
            productAdapter.withLoadStateFooter(LoadingAdapter())
        productAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                binding.includeContent.rvStore.scrollToPosition(0)

            }
        })

        binding.includeContent.rvStore.layoutManager = gridLayoutManager
    }

    private fun setRecyclerView() {
        if (viewModel.recyclerViewType == ProductAdapter.ONE_COLUMN_VIEW_TYPE) {
            productAdapter.viewType = ProductAdapter.ONE_COLUMN_VIEW_TYPE
            gridLayoutManager.spanCount = 1

        } else {
            productAdapter.viewType = ProductAdapter.MORE_COLUMN_VIEW_TYPE
            gridLayoutManager.spanCount = 2

        }
    }

    private fun observeProducts() {
        productAdapter.addLoadStateListener { loadState ->
            val state = loadState.refresh
            showShimmerLoading(state is LoadState.Loading)
            if (state is LoadState.Error) showErrorView(state.error)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productsData.collect { pagingData ->
                    productAdapter.submitData(pagingData)
                    setRecyclerView()
                }
            }
        }
    }

    private fun showErrorView(error: Throwable) {
        TODO("Not yet implemented")
    }

    private fun showShimmerLoading(isLoading: Boolean) {
        with(binding) {
            includeLoading.shimmerList.isVisible =
                viewModel.recyclerViewType == ProductAdapter.ONE_COLUMN_VIEW_TYPE
            includeLoading.shimmerGrid.isVisible =
                viewModel.recyclerViewType == ProductAdapter.MORE_COLUMN_VIEW_TYPE

            includeLoading.loadingShimmer.isVisible = isLoading
            includeContent.layoutContent.isVisible = !isLoading

            if(isLoading){
                includeLoading.loadingShimmer.startShimmer()
            } else if (includeLoading.loadingShimmer.isShimmerStarted)
                includeLoading.loadingShimmer.stopShimmer()

        }
    }

    private fun actionSetup() {
        binding.includeContent.ivList.setOnClickListener {
            actionChangeToGridView()
        }
        binding.includeContent.chipFilter.setOnClickListener {
            openFilter()
        }
        binding.swipeRefresh.setOnRefreshListener {
            productAdapter.refresh()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun openFilter() {
        with(viewModel.productsQuery.value) {
            val filterSheetFragment = FilterSheetFragment.newInstance(
                sort, brand, lowest, highest
            )
            filterSheetFragment.show(childFragmentManager, FilterSheetFragment.TAG)
        }
    }

    private fun actionChangeToGridView() {
        viewModel.recyclerViewType =
            if (viewModel.recyclerViewType == ProductAdapter.ONE_COLUMN_VIEW_TYPE) {
                ProductAdapter.MORE_COLUMN_VIEW_TYPE
            } else {
                ProductAdapter.ONE_COLUMN_VIEW_TYPE
            }

        setRecyclerView()
        setImageChange()
    }

    private fun setImageChange() {
        binding.includeContent.ivList.setImageResource(
            if (viewModel.recyclerViewType == ProductAdapter.ONE_COLUMN_VIEW_TYPE)
                R.drawable.baseline_grid_view_24
            else
                R.drawable.baseline_format_list_bulleted_24
        )
    }

    private fun getFilterFragmentResult() {
        childFragmentManager.setFragmentResultListener(
            FilterSheetFragment.FRAGMENT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            Log.d(
                "TAG",
                "getFilterFragmentResult: ${bundle.getString(FilterSheetFragment.BUNDLE_BRAND_KEY)}"
            )
            Log.d(
                "TAG",
                "getFilterFragmentResult: ${bundle.getString(FilterSheetFragment.BUNDLE_SORT_KEY)}"
            )
            val productFilter = ProductsQuery(
                sort = bundle.getString(FilterSheetFragment.BUNDLE_SORT_KEY),
                brand = bundle.getString(FilterSheetFragment.BUNDLE_BRAND_KEY),
                lowest = bundle.getString(FilterSheetFragment.BUNDLE_LOWEST_KEY),
                highest = bundle.getString(FilterSheetFragment.BUNDLE_HIGHEST_KEY)
            )

            val sort = bundle.getString(FilterSheetFragment.BUNDLE_SORT_KEY)
            val brand = bundle.getString(FilterSheetFragment.BUNDLE_BRAND_KEY)


            if (sort != null) {
                val chip = Chip(requireActivity())
                chip.text = sort
                chip.setChipBackgroundColorResource(R.color.white)
                chip.isCloseIconVisible = false
                chip.setTextColor(resources.getColor(android.R.color.black, requireContext().theme))
                binding.includeContent.chipFilterGroup.addView(chip)
            }

            if (brand != null) {
                val chip = Chip(requireActivity())
                chip.text = brand
                chip.setChipBackgroundColorResource(R.color.white)
                chip.isCloseIconVisible = false
                chip.setTextColor(resources.getColor(android.R.color.black, requireContext().theme))
                binding.includeContent.chipFilterGroup.addView(chip)
            }

            viewModel.resSortFilterProduct =
                bundle.getInt(FilterSheetFragment.BUNDLE_SORT_RES_ID_KEY)
            viewModel.resBrandFilterProduct =
                bundle.getInt(FilterSheetFragment.BUNDLE_BRAND_RES_ID_KEY)


            viewModel.getFilterData(productFilter)
            setChipView()

        }
    }

    private fun setChipView() {
        binding.includeContent.chipFilterGroup.removeAllViews()

        val filterList = arrayListOf<String>()
        Log.d("TAG", "setChipView: ${viewModel.resSortFilterProduct}")
        with(viewModel.productsQuery.value) {
            this.sort?.let { filterList.add(resources.getString(viewModel.resSortFilterProduct!!)) }
            this.brand?.let { filterList.add(resources.getString(viewModel.resBrandFilterProduct!!)) }
            this.lowest?.let { filterList.add("> $it") }
            this.highest?.let { filterList.add("< $it") }
        }
//        filterList.add(bundle)

        for (filter in filterList) {
            val chip = Chip(binding.includeContent.chipFilterGroup.context)
            chip.setTextAppearanceResource(R.style.TextAppearance_Medium)
            chip.text = filter
            chip.isClickable = false
            chip.isCheckable = false
            binding.includeContent.chipFilterGroup.addView(chip)
        }
    }
}
