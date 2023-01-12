package com.azmetov.playlistmaker.ui.player

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.ui.recycler.BaseVHBinder
import com.azmetov.playlistmaker.ui.recycler.ViewHolderFactory

class AttributeVHFactory : ViewHolderFactory<AttributeVH> {

  override fun create(parent: ViewGroup): AttributeVH {
    return AttributeVH(LayoutInflater.from(parent.context).inflate(R.layout.player_attribute_item, parent, false))
  }
}

class AttributeVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val nameView = itemView.findViewById<TextView>(R.id.nameView)
  val valueView = itemView.findViewById<TextView>(R.id.valueView)
}

class AttributeBinder : BaseVHBinder<AttributeVH, Pair<String, String>> {

  override fun onBindViewHolder(holder: AttributeVH, data: Pair<String, String>, position: Int) {
    holder.nameView.text = data.first
    holder.valueView.text = data.second
  }
}