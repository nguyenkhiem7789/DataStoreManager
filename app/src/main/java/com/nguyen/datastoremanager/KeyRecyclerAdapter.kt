package com.nguyen.datastoremanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class KeyRecyclerAdapter(val list: List<String>) : RecyclerView.Adapter<RecyclerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.fillData(list[position])
    }

}

class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun fillData(item: String) {
        itemView.keyAlias.text = item
    }
}