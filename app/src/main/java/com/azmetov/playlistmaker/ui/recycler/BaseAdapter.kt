package com.azmetov.playlistmaker.ui.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private val items = mutableListOf<ListItem<RecyclerView.ViewHolder, Any>>()

  fun <VH : RecyclerView.ViewHolder, DATA> addItem(item: ListItem<VH, DATA>) {
    items.add(item as ListItem<RecyclerView.ViewHolder, Any>)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return items.find { it.factory.hashCode() == viewType }?.factory!!.create(parent)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    item.binder.onBindViewHolder(holder, item.data, position)
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun getItemViewType(position: Int): Int {
    return items[position].factory.hashCode()
  }

}

class ListItem<VH : RecyclerView.ViewHolder, DATA>(
  val factory: ViewHolderFactory<VH>,
  val binder: BaseVHBinder<VH, DATA>,
  val data: DATA
)
