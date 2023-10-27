package com.athallah.ecommerce.fragment.prelogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.athallah.ecommerce.R
import com.athallah.ecommerce.adapter.ViewPagerAdapter
import com.athallah.ecommerce.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager2: ViewPager2

    private val viewModel: OnboardingViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tampilkanViewPager()
        pindahViewPager()
        pindahFragment()

        viewPager2 = binding.vpOnboard
        viewPager2.adapter = ViewPagerAdapter(images)



    }

    private fun pindahFragment() {
        binding.btLewati.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
            viewModel.setFirstTimeRunApp()
        }

        binding.btGabung.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_registerFragment)
            viewModel.setFirstTimeRunApp()
        }
    }

    private fun pindahViewPager() {
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

        binding.btNext.setOnClickListener {
            if (viewPager2.currentItem < adapter.itemCount - 1) {
                viewPager2.currentItem = viewPager2.currentItem + 1
            }
        }
    }


    private val images = listOf(
        R.drawable.imagerv1,
        R.drawable.imagerv2,
        R.drawable.imagerv3
    )


    private fun tampilkanViewPager() {

        viewPager2 = binding.vpOnboard
        viewPager2.adapter = ViewPagerAdapter(images)
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = binding.indicator
        indicator.setViewPager(viewPager2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}