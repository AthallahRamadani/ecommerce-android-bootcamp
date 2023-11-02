package com.athallah.ecommerce.fragment.prelogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.athallah.ecommerce.R
import com.athallah.ecommerce.adapter.ViewPagerAdapter
import com.athallah.ecommerce.databinding.FragmentOnboardingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager2: ViewPager2
    private val viewModel: OnboardingViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewPager() {
        val images = listOf(
            R.drawable.imagerv1,
            R.drawable.imagerv2,
            R.drawable.imagerv3
        )
        viewPager2 = binding.vpOnboard
        viewPager2.adapter = ViewPagerAdapter(images)
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val indicator = binding.indicator
        indicator.setViewPager(viewPager2)
        val adapter = ViewPagerAdapter(images)
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position < adapter.itemCount - 1) {
                    binding.btNext.visibility = View.VISIBLE
                } else {
                    binding.btNext.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun initView() {
        initViewPager()
        binding.btNext.setOnClickListener {
            val adapter = viewPager2.adapter
            if (adapter != null) {
                if (viewPager2.currentItem < adapter.itemCount - 1) {
                    viewPager2.currentItem = viewPager2.currentItem + 1
                }
            }
        }
        binding.btLewati.setOnClickListener { viewModel.setAlreadyOnboard(ActionOnboard.LOGIN) }
        binding.btGabung.setOnClickListener { viewModel.setAlreadyOnboard(ActionOnboard.REGISTER) }
    }

    private fun initViewModel() {
        viewModel.prefSetIsOnBoard.observe(viewLifecycleOwner) { nextNav ->
            if (nextNav != null) {
                when (nextNav) {
                    ActionOnboard.LOGIN -> findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
                    ActionOnboard.REGISTER -> findNavController().navigate(R.id.action_onboardingFragment_to_registerFragment)
                }
            }
        }
    }

}