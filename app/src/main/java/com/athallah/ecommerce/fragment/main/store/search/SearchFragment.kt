package com.athallah.ecommerce.fragment.main.store.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athallah.ecommerce.R
import com.athallah.ecommerce.data.ResultState
import com.athallah.ecommerce.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : DialogFragment() {

    private val viewModel : SearchViewModel by viewModel()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var query: String
    private val searchAdapter: SearchAdapter by lazy{
        SearchAdapter {searchKeyword->
            sendSearchResult(searchKeyword)
        }
    }

    override fun getTheme(): Int {
        return R.style.SearchDialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            query = it.getString(ARG_QUERY) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupEdittext()
        observeSearch()
    }

    private fun setupEdittext() {
        with(binding.etSearch) {
            setText(query)

            requestFocus()
            dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            doOnTextChanged { text, _, _, _ ->
                showSearchLoading(true)
                viewModel.getSearchData(text.toString())
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    sendSearchResult()
                }
                true
            }
        }
    }

    private fun observeSearch() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.searchData.collect{result->
                    showSearchLoading(result is ResultState.Loading)
                    when (result){
                        is ResultState.Loading -> {}
                        is ResultState.Success -> searchAdapter.submitList(result.data)
                        is ResultState.Error -> {}
                    }
                }
            }
        }
    }

    private fun showSearchLoading(isLoading: Boolean) {
        binding.loadingSearch.isVisible = isLoading
    }

    private fun setupRecyclerView() {
        binding.rvSearch.adapter = searchAdapter
        binding.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        searchAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvSearch.scrollToPosition(0)
            }
        })
    }


    private fun sendSearchResult(data: String= binding.etSearch.text.toString()) {
        val bundle = bundleOf(
            BUNDLE_QUERY_KEY to data.ifEmpty { null },
        )
        setFragmentResult(FRAGMENT_REQUEST_KEY,bundle)
        dismiss()
    }


    companion object {
        private const val ARG_QUERY = "arg_query"
        const val TAG = "SearchProductFragment"
        const val FRAGMENT_REQUEST_KEY = "search_fragment_request_key"
        const val BUNDLE_QUERY_KEY = "query"

        @JvmStatic
        fun newInstance(query: String?) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUERY, query)
                }
            }
    }


}