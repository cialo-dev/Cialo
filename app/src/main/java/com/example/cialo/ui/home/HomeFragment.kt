package com.example.cialo.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cialo.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var _chart: PieChart;
    private lateinit var _viewModel: HomeViewModel;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _chart = binding.chart;

        initializeBindings()


        return binding.root
    }

    private fun initializeBindings() {
        val entries = mutableListOf<PieEntry>()

        entries.add(PieEntry(55f, "Token"))
        entries.add(PieEntry(99f, "Nastepny"))

        val dataSet = PieDataSet(entries, "")
        dataSet.setColors(Color.MAGENTA, Color.GRAY)
        val l: Legend = _chart.legend
        l.isEnabled = false;
        val data = PieData(dataSet)

        _chart.data = data;
        _chart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}