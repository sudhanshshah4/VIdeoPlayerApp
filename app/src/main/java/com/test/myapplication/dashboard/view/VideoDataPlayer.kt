package com.test.myapplication.dashboard.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.test.myapplication.R
import com.test.myapplication.dashboard.model.MediaFileInfo
import com.test.myapplication.databinding.VideoPlayerBinding

class VideoDataPlayer : AppCompatActivity() {
    private val TAG = "VideoData"

    lateinit var list: MutableList<MediaFileInfo>
    var index: Int = 0
    lateinit var mediainfo: MediaFileInfo
    var simpleExoPlayer: SimpleExoPlayer? = null
    lateinit var binding: VideoPlayerBinding
    private lateinit var mediaDataSourceFactory: DataSource.Factory


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.video_player)
        binding.muteButton.setOnClickListener(View.OnClickListener {
            if (simpleExoPlayer?.volume == 0f) {
                val currentvolume: Float? = simpleExoPlayer?.getVolume()
                if (currentvolume != null) {
                    simpleExoPlayer?.volume = 10f
                }
                binding.muteButton.setImageDrawable(resources.getDrawable(R.drawable.ic_mute, null))

            } else {
                simpleExoPlayer?.setVolume(0f);
                binding.muteButton.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_unmute,
                        null
                    )
                )
            }
        })
        if (intent.hasExtra("data") && intent.hasExtra("list")) {
            list = intent.getSerializableExtra("list") as MutableList<MediaFileInfo>
            mediainfo = intent.getSerializableExtra("data") as MediaFileInfo

        }

        initializePlayer(mediainfo.filePath.toString())
    }

    private fun initializePlayer(url: String) {
        mediaDataSourceFactory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))
        val mediaSourceFactory: MediaSourceFactory =
            DefaultMediaSourceFactory(mediaDataSourceFactory)
        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        var mediaItem: MutableList<MediaItem> = mutableListOf()
        var position = 0
        if (list.size > 0) {
            list.forEach {
                if (position == 0) {
                    mediaItem.add(0, MediaItem.fromUri(mediainfo.filePath.toString()))
                } else {
                    mediaItem.add(MediaItem.fromUri(it.filePath.toString()))
                }
                position++
            }
        } else {
            mediaItem.add(0, MediaItem.fromUri(mediainfo.filePath.toString()))
        }

        simpleExoPlayer?.setMediaItems(mediaItem)

        simpleExoPlayer?.prepare()
        simpleExoPlayer?.playWhenReady = true


        binding?.videoplayer?.setShutterBackgroundColor(Color.BLACK)
        binding?.videoplayer?.player = simpleExoPlayer
        binding?.videoplayer?.requestFocus()


    }

    public override fun onPause() {
        super.onPause()

        if (Util.SDK_INT <= 23) releasePlayer()
    }

    private fun releasePlayer() {
        simpleExoPlayer?.release()
    }

    public override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) releasePlayer()
    }
}
