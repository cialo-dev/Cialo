package com.example.cialo.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cialo.databinding.FragmentDashboardBinding
import com.example.cialo.models.RegionEventModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var _regionEventsAdapter: RegionEventsAdapter
    private lateinit var _linearLayoutManager: LinearLayoutManager
    private var _regionEvents = mutableListOf<RegionEventModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        this.initializeBindings(viewModel)

        return binding.root
    }

    private fun initializeBindings(viewModel: DashboardViewModel) {
        _linearLayoutManager = LinearLayoutManager(activity);
        val eventsUiList: RecyclerView = binding.listviewEvents;
        _regionEventsAdapter = RegionEventsAdapter(_regionEvents)
        eventsUiList.layoutManager = _linearLayoutManager
        eventsUiList.adapter = _regionEventsAdapter
        viewModel.regionEvents.observe(viewLifecycleOwner, Observer {
            _regionEventsAdapter.notify(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}