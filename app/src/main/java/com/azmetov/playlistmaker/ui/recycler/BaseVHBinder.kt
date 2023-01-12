package com.azmetov.playlistmaker.ui.recycler

import androidx.recyclerview.widget.RecyclerView

interface BaseVHBinder<in VH : RecyclerView.ViewHolder, in DATA> {
  fun onBindViewHolder(holder: VH, data: DATA, position: Int)
}