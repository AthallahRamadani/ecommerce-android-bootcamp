package com.athallah.ecommerce.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.athallah.ecommerce.R
import com.athallah.ecommerce.databinding.FragmentMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@androidx.annotation.OptIn(ExperimentalBadgeUtils::class)
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.prefGetUsername().isEmpty()) {
            findNavController().navigate(R.id.action_mainFragment_to_profilFragment)
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        Log.d("lol", "onViewCreated: ${viewModel.prefGetUsername()}")
    }

    private fun initView() {
        binding.topAppBar.title = viewModel.prefGetUsername()
        allNavigationController()
        observeWishlist()
        observeCart()
        observeNotification()
        observeWishlistNavRail()
        setActionAppbar()
    }

    private fun setActionAppbar() {
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_notification -> findNavController().navigate(R.id.action_mainFragment_to_notificationFragment)
                R.id.menu_cart -> findNavController().navigate(R.id.action_mainFragment_to_cartFragment)
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun observeWishlistNavRail() {
        val badge = binding.navRail?.getOrCreateBadge(R.id.wishlistFragment)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.wishlistSize().collect {
                    if (it >= 1) {
                        badge?.isVisible = true
                        badge?.number = it
                    } else {
                        badge?.isVisible = false
                    }
                }
            }
        }
    }

    private fun observeWishlist() {
        val badge = binding.bottomNavView?.getOrCreateBadge(R.id.wishlistFragment)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.wishlistSize().collect { size ->
                    if (size >= 1) {
                        badge?.isVisible = true
                        badge?.number = size
                    } else {
                        badge?.isVisible = false
                    }
                }
            }
        }
    }

    private fun observeCart() {
        val badgeDrawable = BadgeDrawable.create(requireActivity())
        binding.topAppBar.let {
            BadgeUtils.attachBadgeDrawable(
                badgeDrawable,
                it,
                R.id.menu_cart
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartSize().collect { size ->
                    if (size >= 1) {
                        badgeDrawable.isVisible = true
                        badgeDrawable.number = size
                    } else {
                        badgeDrawable.isVisible = false
                    }
                }
            }
        }
    }

    private fun observeNotification() {
        val badgeDrawable = BadgeDrawable.create(requireActivity())
        binding.topAppBar.let {
            BadgeUtils.attachBadgeDrawable(
                badgeDrawable,
                it,
                R.id.menu_notification
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationSize().collect { size ->
                    if (size >= 1) {
                        badgeDrawable.isVisible = true
                        badgeDrawable.number = size
                    } else {
                        badgeDrawable.isVisible = false
                    }
                }
            }
        }
    }

    private fun allNavigationController() {
        setNavRail()
        setNavView()
        setBottomNavigation()
        if (viewModel.shouldMoveToTransaction) {
            binding.bottomNavView?.selectedItemId = R.id.transactionFragment
            binding.navRail?.selectedItemId = R.id.transactionFragment
            binding.navView?.menu?.performIdentifierAction(R.id.transactionFragment, 0)
            viewModel.shouldMoveToTransaction = false
        }
    }

    private fun setBottomNavigation() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomNavView?.setupWithNavController(navController)
    }

    private fun setNavRail() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.navRail?.setupWithNavController(navController)
    }

    private fun setNavView() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.navView?.setupWithNavController(navController)
    }

    companion object {
        const val MOVE_TRANSACTION_BUNDLE_KEY = "move_transaction_bundle_key"
    }
}
