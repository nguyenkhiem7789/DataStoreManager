package com.nguyen.datastoremanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class KeyRecyclerAdapter(val listAlias: List<String>, val listener: OnClickItemListener) : RecyclerView.Adapter<RecyclerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listAlias.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.fillData(listAlias[position], listener)
    }

}

class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun fillData(alias: String, listener: OnClickItemListener) {
        itemView.keyAlias.text = alias
        itemView.deleteButton.setOnClickListener {
            listener.delete(alias)
        }
        itemView.encryptButton.setOnClickListener {
            listener.encrypt(alias)
        }
        itemView.decryptButton.setOnClickListener {
            listener.decrypt(alias)
        }
    }
}

interface OnClickItemListener {
    fun delete(alias: String)
    fun encrypt(alias: String)
    fun decrypt(alias: String)
}