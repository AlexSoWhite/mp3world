package com.nafanya.mp3world.features.metadataEditing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.core.os.bundleOf
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.view.BaseActivity
import com.nafanya.mp3world.databinding.ActivityEditMetadaLayoutBinding

class MetadataEditActivity : BaseActivity<ActivityEditMetadaLayoutBinding>() {

    companion object {
        const val SONG_URI_KEY = "uri"
    }

    override fun inflate(layoutInflater: LayoutInflater): ActivityEditMetadaLayoutBinding {
        return ActivityEditMetadaLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val uri = intent.getStringExtra(SONG_URI_KEY)
        val bundle = bundleOf(SONG_URI_KEY to uri)
        val fragment = MetadataEditFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.metadata_edit_fragment_container, fragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
