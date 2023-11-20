package com.athallah.ecommerce.fragment.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.athallah.ecommerce.R
import com.athallah.ecommerce.databinding.FragmentDetailBinding
import com.athallah.ecommerce.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupAction() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_notificationFragment_to_mainFragment)
        }
    }

    companion object {

    }
}