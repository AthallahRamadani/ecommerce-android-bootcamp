package com.athallah.ecommerce.fragment.prelogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.athallah.ecommerce.R
import com.athallah.ecommerce.databinding.FragmentOnboardingBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager2: ViewPager2
    private val viewModel: OnboardingViewModel by viewModel()

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
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
        viewPager2.adapter = OnboardingAdapter(images)
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val indicator = binding.indicator
        indicator.setViewPager(viewPager2)
        val adapter = OnboardingAdapter(images)
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
            firebaseAnalytics.logEvent("button_Click") {
                param("button_name", "button_next")
            }
            val adapter = viewPager2.adapter
            if (adapter != null) {
                if (viewPager2.currentItem < adapter.itemCount - 1) {
                    viewPager2.currentItem = viewPager2.currentItem + 1
                }
            }
        }
        binding.btLewati.setOnClickListener {
            firebaseAnalytics.logEvent("button_Click") {
                param("button_name", "button_skip")
            }
            viewModel.setFirstTimeRunApp(true)
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
        }

        binding.btGabung.setOnClickListener {
            firebaseAnalytics.logEvent("button_Click") {
                param("button_name", "button_join")
            }
            viewModel.setFirstTimeRunApp(true)
            findNavController().navigate(R.id.action_onboardingFragment_to_registerFragment)
        }
    }
}
