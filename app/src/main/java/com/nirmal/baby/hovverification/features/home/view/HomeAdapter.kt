package com.nirmal.baby.hovverification.features.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nirmal.baby.hovverification.R
import com.nirmal.baby.hovverification.features.home.entity.HomeEntity
import java.text.SimpleDateFormat
import java.util.Locale

class HomeAdapter(private val context: Context, private val data: List<HomeEntity>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = data[position]

        if (item.faceCount < 2) {
            holder.title.text = "Rejected"
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.red)) // Replace `red` with the name of your red color in colors.xml
        } else {
            holder.title.text = "Approved"
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.green)) // Replace `green` with the name of your green color in colors.xml
        }


        holder.date.text = SimpleDateFormat("EEE, dd MMM yyyy HH:mm", Locale.getDefault())
            .format(item.timestamp)
        holder.passengerCount.text = "Faces Detected: ${item.faceCount}"
    }

    override fun getItemCount() = data.size

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.itemTitle)
        val date: TextView = view.findViewById(R.id.itemDate)
        val passengerCount: TextView = view.findViewById(R.id.passengerCount)
    }
}