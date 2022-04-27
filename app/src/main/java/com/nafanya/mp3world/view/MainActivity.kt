package com.nafanya.mp3world.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_TITLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.ActivityMainBinding
import com.nafanya.mp3world.model.foregroundService.ForegroundService
import com.nafanya.mp3world.model.foregroundService.PlayerLiveDataProvider
import com.nafanya.mp3world.model.foregroundService.ServiceInitializer
import com.nafanya.mp3world.model.listManagers.AlbumListManager
import com.nafanya.mp3world.model.listManagers.ArtistListManager
import com.nafanya.mp3world.model.listManagers.FavouriteListManager
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.wrappers.Album
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.listActivities.albums.AlbumListActivity
import com.nafanya.mp3world.view.listActivities.artists.ArtistListActivity
import com.nafanya.mp3world.view.listActivities.favourite.FavouriteListActivity
import com.nafanya.mp3world.view.listActivities.playlists.PlaylistListActivity
import com.nafanya.mp3world.view.listActivities.search.SearchSongListActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity
import com.nafanya.mp3world.view.playerViews.GenericPlayerControlView
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var playerView: GenericPlayerControlView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.displayOptions = DISPLAY_SHOW_TITLE
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initInitializer()
        subscribeToPlayerState()
        initMainMenu()
    }

    private fun initInitializer() {
        val intent = Intent(applicationContext, ServiceInitializer::class.java)
        startService(intent)
    }

    private fun subscribeToPlayerState() {
        // observe player state
        val observerPlayer = Observer<Boolean> {
            if (it) {
                playerView = GenericPlayerControlView(this, R.id.player_control_view)
                playerView!!.setSongObserver()
                playerView!!.playerControlView.visibility = View.VISIBLE
            }
        }
        PlayerLiveDataProvider.isPlayerInitialized.observe(
            this,
            observerPlayer
        )
    }

    @Suppress("LongMethod")
    private fun initMainMenu() {
        // all songs
        val songListObserver = Observer<MutableList<Song>> { songList ->
            binding.songCount = songList.size
            binding.allSongs.item.setOnClickListener {
                ActivityCreator()
                    .with(this)
                    .createSongListActivity()
                    .start()
            }
        }
        binding.allSongs.menuItemIcon.setImageResource(R.drawable.song_menu_icon)
        SongListManager.songList.observe(this, songListObserver)

        // playlists
        val playlistsObserver = Observer<MutableList<Playlist>> {
            binding.playlistCount = it.size
            binding.playlists.item.setOnClickListener {
                ActivityCreator()
                    .with(this)
                    .createPlaylistListActivity()
                    .start()
            }
        }
        binding.playlists.menuItemIcon.setImageResource(R.drawable.playlist_play)
        PlaylistListManager.playlists.observe(this, playlistsObserver)

        // artists
        val artistObserver = Observer<MutableList<Artist>> {
            binding.artistCount = it.size
            binding.artists.item.setOnClickListener {
                ActivityCreator()
                    .with(this)
                    .createArtistListActivity()
                    .start()
            }
        }
        binding.artists.menuItemIcon.setImageResource(R.drawable.artist)
        ArtistListManager.artists.observe(this, artistObserver)

        // albums
        val albumsObserver = Observer<MutableList<Album>> {
            binding.albumCount = it.size
            binding.albums.item.setOnClickListener {
                ActivityCreator()
                    .with(this)
                    .createAlbumListActivity()
                    .start()
            }
        }
        binding.albums.menuItemIcon.setImageResource(R.drawable.album)
        AlbumListManager.albums.observe(this, albumsObserver)

        // favourites
        val favouriteObserver = Observer<Playlist> { playlist ->
            binding.favouriteCount = playlist.songList.size
            binding.favourite.item.setOnClickListener {
                ActivityCreator()
                    .with(this)
                    .createFavouriteListActivity()
                    .start()
            }
        }
        binding.favourite.menuItemIcon.setImageResource(R.drawable.favorite)
        FavouriteListManager.favorites.observe(this, favouriteObserver)

        // statistic
//        val statisticObserver = Observer<MutableList<SongStatisticEntity>> {
//            binding.statistics.item.setOnClickListener {
//                val statisticIntent = Intent(this, StatisticActivity::class.java)
//                startActivity(statisticIntent)
//            }
//        }
//        StatisticInfoManager.info.observe(this, statisticObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // setting appBar search view
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        // setting search dispatcher
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    ActivityCreator()
                        .with(this@MainActivity)
                        .createSearchSongListActivity(query)
                        .start()
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            }
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.stopService(Intent(this, ForegroundService::class.java))
    }
}
