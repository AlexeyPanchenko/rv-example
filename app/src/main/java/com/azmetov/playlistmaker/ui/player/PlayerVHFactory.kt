package com.azmetov.playlistmaker.ui.player

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.ui.recycler.BaseVHBinder
import com.azmetov.playlistmaker.ui.recycler.ViewHolderFactory

class PlayerVHFactory : ViewHolderFactory<PlayerVH> {

  override fun create(parent: ViewGroup): PlayerVH {
    return PlayerVH(LayoutInflater.from(parent.context).inflate(R.layout.player_player_item, parent, false))
  }
}

class PlayerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val mediaButton = itemView.findViewById<View>(R.id.player_favorites_iv)
  val favButton = itemView.findViewById<View>(R.id.player_like_iv)
}

class PlayerBinder : BaseVHBinder<PlayerVH, Unit> {

  var favoriteClickListener: ((Int) -> Unit)? = null

  override fun onBindViewHolder(holder: PlayerVH, data: Unit, position: Int) {
    holder.favButton.setOnClickListener { favoriteClickListener?.invoke(position) }
  }

}