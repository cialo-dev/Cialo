package com.example.cialo.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cialo.R
import com.example.cialo.models.RegionEventModel
import java.text.SimpleDateFormat
import java.util.*

class RegionEventsAdapter(private var _regionEvents: MutableList<RegionEventModel>) :
    RecyclerView.Adapter<RegionEventsAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewDateTime: TextView = itemView.findViewById(R.id.text_dateTime)
        val textViewType: TextView = itemView.findViewById(R.id.text_eventType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_dashboard_events_adapter_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = _regionEvents[position]

        holder.textViewType.text = model.eventType.toString()
        holder.textViewDateTime.text = toDateTime(model.dateTime)
    }

    fun toDateTime(value: Long) : String{
        val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
        val resultdate = Date(value)
        return sdf.format(resultdate);
    }

    fun notify(regions: List<RegionEventModel>) {

        this._regionEvents.clear()
        this._regionEvents = regions.toMutableList().sortedByDescending { it.dateTime }.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return _regionEvents.size
    }
}