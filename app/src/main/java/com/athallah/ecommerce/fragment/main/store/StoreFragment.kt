package com.athallah.ecommerce.fragment.main.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.athallah.ecommerce.R
import com.athallah.ecommerce.databinding.FragmentMainBinding
import com.athallah.ecommerce.databinding.FragmentStoreBinding
import com.athallah.ecommerce.fragment.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class StoreFragment : Fragment() {
    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StoreViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}