package com.athallah.ecommerce.fragment.notification

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.athallah.ecommerce.R
import com.athallah.ecommerce.databinding.FragmentNotificationBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : Fragment() {

    private val viewModel: NotificationViewModel by viewModel()

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val notificationAdapter: NotificationAdapter by lazy {
        NotificationAdapter { data ->
            viewModel.readNotification(data)
            Log.d("jojo", "hai: $data")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupRecyclerView()
        observeNotification()
    }

    private fun observeNotification() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationData().collect { data ->
                    showIsDataEmpty(data.isEmpty())
                    notificationAdapter.submitList(data)
                }
            }
        }
    }

    private fun showIsDataEmpty(isEmpty: Boolean) {
        binding.layoutEmpty.isVisible = isEmpty
        binding.rvNotification.isVisible = !isEmpty
    }

    private fun setupRecyclerView() {
        binding.rvNotification.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            itemAnimator = null
        }
    }

    private fun setupAction() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_notificationFragment_to_mainFragment)
        }
    }
}
