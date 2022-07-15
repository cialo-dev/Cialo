package com.example.cialo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cialo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initializeBindings(viewModel)

        return binding.root
    }

    private fun initializeBindings(viewModel: HomeViewModel) {
        val button: Button = binding.buttonCleanDb;

        button.setOnClickListener(View.OnClickListener {
            viewModel.cleanDatabase()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}