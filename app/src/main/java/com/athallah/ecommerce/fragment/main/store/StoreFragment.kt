package com.athallah.ecommerce.fragment.main.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.datasource.api.request.ProductsQuery
import com.athallah.ecommerce.data.datasource.model.Product
import com.athallah.ecommerce.databinding.FragmentStoreBinding
import com.athallah.ecommerce.fragment.detail.DetailFragment
import com.athallah.ecommerce.fragment.main.store.search.SearchFragment
import com.athallah.ecommerce.fragment.main.store.storeadapter.LoadingAdapter
import com.athallah.ecommerce.fragment.main.store.storeadapter.ProductAdapter
import com.athallah.ecommerce.utils.toCurrencyFormat
import com.google.android.material.chip.Chip
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class StoreFragment : Fragment() {
    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StoreViewModel by viewModel()
    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    private val productAdapter by lazy {
        ProductAdapter { product ->
            sendLogSelectItem(product)
            Navigation.findNavController(requireActivity(), R.id.fcv_main).navigate(
                R.id.action_mainFragment_to_detailFragment,
                bundleOf(DetailFragment.BUNDLE_PRODUCT_ID_KEY to product.productId)
            )
        }
    }

    private fun sendLogSelectItem(product: Product) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            val bundleProduct = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, product.productId)
                putString(FirebaseAnalytics.Param.ITEM_NAME, product.productName)
                putString(FirebaseAnalytics.Param.ITEM_BRAND, product.brand)
            }
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(bundleProduct))
        }
    }

    private val loadingAdapter = LoadingAdapter()

    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        getSearchFragmentResult()
        setSearchView()
        setImageChange()
        setChipView()
    }

    private fun setSearchView() {
        binding.etSearch.setText(viewModel.productsQuery.value.search ?: "")
    }

    private fun getSearchFragmentResult() {
        childFragmentManager.setFragmentResultListener(
            SearchFragment.FRAGMENT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val query = bundle.getString(SearchFragment.BUNDLE_QUERY_KEY)
            viewModel.getSearchData(query)
            setSearchView()
            binding.layoutEmpty.isInvisible = true
        }
    }

    private fun getAccToken() {
        viewModel.prefGetAccToken()
    }

    private fun setAdapter() {
        gridLayoutManager = GridLayoutManager(requireActivity(), 1)

        binding.includeContent.rvStore.adapter =
            productAdapter.withLoadStateFooter(loadingAdapter)

        binding.includeContent.rvStore.layoutManager = gridLayoutManager

        productAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                binding.includeContent.rvStore.scrollToPosition(0)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                sendLogViewItemList(productAdapter.snapshot().items)
            }
        })

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (viewModel.recyclerViewType == ProductAdapter.MORE_COLUMN_VIEW_TYPE) {
                    if (position == productAdapter.itemCount && loadingAdapter.itemCount > 0) {
                        2
                    } else {
                        1
                    }
                } else {
                    1
                }
            }
        }
    }

    private fun sendLogViewItemList(items: List<Product>) {
        firebaseAnalytics.logEvent(Event.VIEW_ITEM_LIST) {
            val bundle = ArrayList<Bundle>()
            items.map { product ->
                val itemBundle = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_ID, product.productId)
                    putString(FirebaseAnalytics.Param.ITEM_NAME, product.productName)
                    putString(FirebaseAnalytics.Param.ITEM_BRAND, product.brand)
                }
                bundle.add(itemBundle)
            }
            param(FirebaseAnalytics.Param.ITEMS, bundle.toTypedArray())
        }
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
        when (error) {
            is retrofit2.HttpException -> {
                if (error.response()?.code() == 404) {
                    binding.layoutEmpty.isVisible = true
                    binding.includeContent.layoutContent.isVisible = false
                } else {
                    binding.layoutConnection.isVisible = true
                    binding.includeContent.layoutContent.isVisible = false
                    binding.tvTitleConnection.text = error.code().toString()
                    binding.tvSubtitleConnection.text = error.response()?.message().toString()
                }
            }

            is IOException -> {
                binding.layoutConnection.isVisible = true
                binding.includeContent.layoutContent.isVisible = false
            }

            else -> {
                binding.layoutServer.isVisible = true
                binding.includeContent.layoutContent.isVisible = false
            }
        }

        binding.btEmpty.setOnClickListener {
            viewModel.resetData()
            setChipView()
            binding.layoutEmpty.isVisible = false
            setSearchView()
        }

        binding.btServer.setOnClickListener {
            productAdapter.refresh()
            binding.btServer.isVisible = false
        }

        binding.btConnection.setOnClickListener {
            productAdapter.refresh()
            binding.layoutConnection.isVisible = false
        }
    }

    private fun showShimmerLoading(isLoading: Boolean) {
        with(binding) {
            includeLoading.shimmerList.isVisible =
                viewModel.recyclerViewType == ProductAdapter.ONE_COLUMN_VIEW_TYPE
            includeLoading.shimmerGrid.isVisible =
                viewModel.recyclerViewType == ProductAdapter.MORE_COLUMN_VIEW_TYPE

            includeLoading.loadingShimmer.isVisible = isLoading
            includeContent.layoutContent.isVisible = !isLoading

            if (isLoading) {
                includeLoading.loadingShimmer.startShimmer()
            } else if (includeLoading.loadingShimmer.isShimmerStarted) {
                includeLoading.loadingShimmer.stopShimmer()
            }
        }
    }

    private fun actionSetup() {
        binding.includeContent.ivList.setOnClickListener {
            actionChangeToGridView()
        }
        binding.includeContent.chipFilter.setOnClickListener {
            openFilter()
        }
        binding.etSearch.setOnClickListener {
            openSearch()
        }
        binding.swipeRefresh.setOnRefreshListener {
            productAdapter.refresh()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun openSearch() {
        val searchFragment = SearchFragment.newInstance(viewModel.productsQuery.value.search)
        searchFragment.show(childFragmentManager, SearchFragment.TAG)
    }

    private fun openFilter() {
        with(viewModel.productsQuery.value) {
            val filterSheetFragment = FilterSheetFragment.newInstance(
                viewModel.resSortFilterProduct?.let { getString(it) },
                viewModel.resBrandFilterProduct?.let { getString(it) },
                lowest,
                highest
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
            if (viewModel.recyclerViewType == ProductAdapter.ONE_COLUMN_VIEW_TYPE) {
                R.drawable.baseline_grid_view_24
            } else {
                R.drawable.baseline_format_list_bulleted_24
            }
        )
    }

    private fun getFilterFragmentResult() {
        childFragmentManager.setFragmentResultListener(
            FilterSheetFragment.FRAGMENT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val productFilter = ProductsQuery(
                sort = bundle.getString(FilterSheetFragment.BUNDLE_SORT_KEY),
                brand = bundle.getString(FilterSheetFragment.BUNDLE_BRAND_KEY),
                lowest = bundle.getString(FilterSheetFragment.BUNDLE_LOWEST_KEY),
                highest = bundle.getString(FilterSheetFragment.BUNDLE_HIGHEST_KEY)
            )

            viewModel.resSortFilterProduct =
                bundle.getInt(FilterSheetFragment.BUNDLE_SORT_RES_ID_KEY, -1).let {
                    if (it == -1) {
                        null
                    } else {
                        it
                    }
                }
            viewModel.resBrandFilterProduct =
                bundle.getInt(FilterSheetFragment.BUNDLE_BRAND_RES_ID_KEY, -1).let {
                    if (it == -1) {
                        null
                    } else {
                        it
                    }
                }

            viewModel.getFilterData(productFilter)
            setChipView()
        }
    }

    private fun setChipView() {
        binding.includeContent.chipFilterGroup.removeAllViews()

        val filterList = arrayListOf<String>()
        with(viewModel.productsQuery.value) {
            this.sort?.let { filterList.add(getString(viewModel.resSortFilterProduct!!)) }
            this.brand?.let { filterList.add(getString(viewModel.resBrandFilterProduct!!)) }
            this.lowest?.let { filterList.add("> ${it.toInt().toCurrencyFormat()}") }
            this.highest?.let { filterList.add("< ${it.toInt().toCurrencyFormat()}") }
        }

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
