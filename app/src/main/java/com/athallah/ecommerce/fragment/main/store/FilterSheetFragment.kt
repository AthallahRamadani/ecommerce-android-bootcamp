package com.athallah.ecommerce.fragment.main.store

import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.fragment.app.setFragmentResult
import com.athallah.ecommerce.R
import com.athallah.ecommerce.databinding.FragmentFilterSheetBinding
import com.athallah.ecommerce.databinding.FragmentStoreBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class FilterSheetFragment : BottomSheetDialogFragment() {

    private var sort: String? = null
    private var sortResId: Int? = null
    private var brand: String? = null
    private var brandResId: Int? = null
    private var lowest: String? = null
    private var highest: String? = null

    private var _binding: FragmentFilterSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sort = it.getString(ARG_SORT)
            brand = it.getString(ARG_BRAND)
            lowest = it.getString(ARG_LOWEST)
            highest = it.getString(ARG_HIGHEST)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val contextThemeWrapper = ContextThemeWrapper(requireActivity(), R.style.Theme_Ecommerce)
        _binding = FragmentFilterSheetBinding.inflate(
            inflater.cloneInContext(contextThemeWrapper),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val modalBottomSheetBehavior = (dialog as BottomSheetDialog).behavior
        modalBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        initView()
        initAction()
    }

    private fun initView() {
        binding.etLowest.setText(lowest ?: "")
        binding.etHighest.setText(highest ?: "")
        setSortChipView()
        setBrandChipView()
    }

    private fun setSortChipView() {
        if (!sort.isNullOrEmpty() && sort == getString(R.string.review)) {
            binding.chipReview.isChecked = true
            brandResId = R.string.review
        }

        if (!sort.isNullOrEmpty() && sort == getString(R.string.sale)) {
            binding.chipSale.isChecked = true
            brandResId = R.string.asus
        }

        if (!sort.isNullOrEmpty() && sort == getString(R.string.lowest)) {
            binding.chipLow.isChecked = true
            brandResId = R.string.lowest
        }

        if (!sort.isNullOrEmpty() && sort == getString(R.string.highest)) {
            binding.chipHigh.isChecked = true
            brandResId = R.string.highest
        }
    }

    private fun setBrandChipView() {
        Log.d(TAG, "setBrandChipView: ${brand.isNullOrEmpty()}")
        Log.d(TAG, "setBrandChipView: ${brand == getString(R.string.apple)}")
        if (!brand.isNullOrEmpty() && brand == getString(R.string.apple)) {
            binding.chipApple.isChecked = true
            brandResId = R.string.apple
        }

        if (!brand.isNullOrEmpty() && brand == getString(R.string.asus)) {
            binding.chipAsus.isChecked = true
            brandResId = R.string.asus
        }

        if (!brand.isNullOrEmpty() && brand == getString(R.string.dell)) {
            binding.chipDell.isChecked = true
            brandResId = R.string.dell
        }

        if (!brand.isNullOrEmpty() && brand == getString(R.string.lenovo)) {
            binding.chipLenovo.isChecked = true
            brandResId = R.string.lenovo
        }

    }


    private fun initAction() {
        binding.btReset.setOnClickListener {
            actionReset()
        }
        binding.btShow.setOnClickListener {
            actionReturnFilterData()
        }
    }

    private fun actionReturnFilterData() {
        lowest = binding.etLowest.text.toString().let {
            it.ifEmpty { null }
        }
        highest = binding.etHighest.text.toString().let {
            it.ifEmpty { null }
        }

        sort = when (binding.cgSort.checkedChipId) {
            R.id.chip_review -> {
                resources.getString(R.string.review)
            }

            R.id.chip_sale -> {
                resources.getString(R.string.sale)
            }

            R.id.chip_low -> {
                resources.getString(R.string.lowest)
            }

            R.id.chip_high -> {
                resources.getString(R.string.highest)
            }

            else -> {
                null
            }
        }

        sortResId = when (binding.cgSort.checkedChipId) {
            R.id.chip_review -> {
                R.string.review
            }

            R.id.chip_sale -> {
                R.string.sale
            }

            R.id.chip_low -> {
                R.string.lowest
            }

            R.id.chip_high -> {
                R.string.highest
            }

            else -> {
                null
            }
        }

        brand = when (binding.cgCategory.checkedChipId) {
            R.id.chip_apple -> {
                resources.getString(R.string.apple)
            }

            R.id.chip_asus -> {
                resources.getString(R.string.asus)
            }

            R.id.chip_dell -> {
                resources.getString(R.string.dell)
            }

            R.id.chip_lenovo -> {
                resources.getString(R.string.lenovo)
            }

            else -> {
                null
            }
        }

        brandResId = when (binding.cgCategory.checkedChipId) {
            R.id.chip_apple -> {
                R.string.apple
            }

            R.id.chip_asus -> {
                R.string.asus
            }

            R.id.chip_dell -> {
                R.string.dell
            }

            R.id.chip_lenovo -> {
                R.string.lenovo
            }

            else -> {
                null
            }
        }


        val bundle = bundleOf(
            BUNDLE_SORT_KEY to sort,
            BUNDLE_SORT_RES_ID_KEY to sortResId,
            BUNDLE_BRAND_KEY to brand,
            BUNDLE_BRAND_RES_ID_KEY to brandResId,
            BUNDLE_LOWEST_KEY to lowest,
            BUNDLE_HIGHEST_KEY to highest
        )
        setFragmentResult(FRAGMENT_REQUEST_KEY, bundle)
        dismiss()
    }

    private fun actionReset() {
        sort = null
        brand = null
        lowest = null
        highest = null
        sortResId = null
        brandResId = null
        binding.cgSort.clearCheck()
        binding.cgCategory.clearCheck()
        binding.etLowest.setText("")
        binding.etHighest.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val ARG_SORT = "arg_sort"
        private const val ARG_BRAND = "arg_brand"
        private const val ARG_LOWEST = "arg_lowest"
        private const val ARG_HIGHEST = "arg_highest"

        const val TAG = "FilterSheetFragment"
        const val FRAGMENT_REQUEST_KEY = "filter_sheet_fragment_request_key"

        const val BUNDLE_SORT_KEY = "sort"
        const val BUNDLE_SORT_RES_ID_KEY = "sort_res_id"
        const val BUNDLE_BRAND_KEY = "brand"
        const val BUNDLE_BRAND_RES_ID_KEY = "brand_res_id"
        const val BUNDLE_LOWEST_KEY = "lowest"
        const val BUNDLE_HIGHEST_KEY = "highest"

        @JvmStatic
        fun newInstance(sort: String?, brand: String?, lowest: String?, highest: String?) =
            FilterSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SORT, sort)
                    putString(ARG_BRAND, brand)
                    putString(ARG_LOWEST, lowest)
                    putString(ARG_HIGHEST, highest)
                }
            }
    }
}