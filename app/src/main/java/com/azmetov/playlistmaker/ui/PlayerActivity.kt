package com.azmetov.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.azmetov.playlistmaker.App
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.data.entities.Track
import com.azmetov.playlistmaker.other.Constants.PLAYER_SHARED_PREFS
import com.azmetov.playlistmaker.other.Converter.convertTime
import com.azmetov.playlistmaker.data.shared.SingleTrackSharedStore
import com.azmetov.playlistmaker.ui.player.*
import com.azmetov.playlistmaker.ui.recycler.BaseAdapter
import com.azmetov.playlistmaker.ui.recycler.ListItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.w3c.dom.Attr

class PlayerActivity : AppCompatActivity() {

    private val artworkView by lazy { findViewById<ImageView>(R.id.player_artwork_iv) }
    private val trackView by lazy { findViewById<TextView>(R.id.player_track_name_tv) }
    private val bandView by lazy { findViewById<TextView>(R.id.player_artist_name_tv) }
    private val durationView by lazy { findViewById<TextView>(R.id.player_duration_tv) }
    private val collectionView by lazy { findViewById<TextView>(R.id.player_album_tv) }
    private val releaseDateView by lazy { findViewById<TextView>(R.id.player_year_tv) }
    private val genreView by lazy { findViewById<TextView>(R.id.player_genre_tv) }
    private val countryView by lazy { findViewById<TextView>(R.id.player_country_tv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player2)

        val rv = findViewById<RecyclerView>(R.id.list)
        val adapter = BaseAdapter()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val arrowBack = findViewById<ImageView>(R.id.iv_player_arrow_back)
        arrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val singleTrackSharedStore = App.instance.singleTrackSharedStore

        @Suppress("DEPRECATION")
        val track =
            if (intent.extras != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.extras!!.getSerializable(EXTRA_TRACK, Track::class.java)?.apply {
                        singleTrackSharedStore.saveToSharedPrefs(this)
                    }
                } else {
                    (intent.extras!!.getSerializable(EXTRA_TRACK) as Track).apply {
                        singleTrackSharedStore.saveToSharedPrefs(this)
                    }
                }
                    ?: throw RuntimeException("Extra serializable in PlayerActivity can not be null!")
            } else {
                singleTrackSharedStore.loadFromSharedPrefs()
                    ?: throw RuntimeException("Extra serializable in PlayerActivity can not be null!")
            }

        adapter.addItem(ListItem(ImageVHFactory(), ImageBinder(), track.getCoverArtwork()))
        adapter.addItem(ListItem(TitleVHFactory(), TitleBinder(), Pair(track.trackName, track.artistName)))
        val playerBinder = PlayerBinder()
        playerBinder.favoriteClickListener = { position -> Toast.makeText(this, "Click $position", Toast.LENGTH_SHORT).show() }
        adapter.addItem(ListItem(PlayerVHFactory(), playerBinder, Unit))
        val attributeFactory = AttributeVHFactory()
        adapter.addItem(ListItem(attributeFactory, AttributeBinder(), Pair("Длительность", convertTime(track.trackTime))))
        adapter.addItem(ListItem(attributeFactory, AttributeBinder(), Pair("Альбом", track.collectionName!!)))
        adapter.addItem(ListItem(attributeFactory, AttributeBinder(), Pair("Год", track.releaseDate!!)))
        adapter.addItem(ListItem(attributeFactory, AttributeBinder(), Pair("Жанр", track.genre!!)))
        adapter.addItem(ListItem(attributeFactory, AttributeBinder(), Pair("Страна", track.country!!)))
        adapter.notifyDataSetChanged()
    }


    private fun bindTrack(track: Track) {
        with(track) {
            Glide.with(this@PlayerActivity)
                .load(this.getCoverArtwork())
                .centerCrop()
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.album_placeholder_with_padding)
                .into(artworkView)
            trackView.text = trackName
            bandView.text = artistName
            durationView.text = convertTime(trackTime)
            collectionView.text = collectionName
            releaseDateView.text = releaseDate
            genreView.text = genre
            countryView.text = country
        }
    }

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK"

        fun getIntent(context: Context, track: Track) =
            Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_TRACK, track)
            }
    }
}