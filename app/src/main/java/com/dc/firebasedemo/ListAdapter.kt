package com.dc.firebasedemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(val mList: ArrayList<ListModel?>, val lister: OnItemDeleteClickListener) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]!!

        holder.tvLastName.text = ItemsViewModel.lastName
        holder.tvFirstName.text = ItemsViewModel.firstName

        holder.ivDelete.setOnClickListener {
            lister.onItemDeleteClick(position)
        }

    }

    interface OnItemDeleteClickListener {
        fun onItemDeleteClick(position: Int)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
        val tvFirstName: TextView = itemView.findViewById(R.id.tvFirstName)
        val tvLastName: TextView = itemView.findViewById(R.id.tvLastName)
    }
}