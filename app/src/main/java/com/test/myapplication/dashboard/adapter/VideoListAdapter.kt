package com.test.myapplication.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.myapplication.R
import com.test.myapplication.dashboard.model.MediaFileInfo
import com.test.myapplication.databinding.MVideoitemBinding
import javax.inject.Inject

class VideoListAdapter @Inject constructor() : RecyclerView.Adapter<VideoItem>() {

    private var layoutInflater: LayoutInflater? = null
    lateinit var data: MutableList<MediaFileInfo>
    private var videoCallback: VideoCallback? = null

    var context: Context? = null


    fun setVideoData(Listdata: MutableList<MediaFileInfo>, videoCallback: VideoCallback) {
        data = Listdata
        this.videoCallback = videoCallback
    }

    interface VideoCallback {
        fun videoClick(mediaFileInfo: MediaFileInfo, data: MutableList<MediaFileInfo>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItem {

        val binding = DataBindingUtil.inflate<MVideoitemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.m_videoitem,
            parent,
            false
        )
        return VideoItem(binding)
    }

    override fun onBindViewHolder(holder: VideoItem, position: Int) {
        holder.binding.container.setOnClickListener(View.OnClickListener {
            videoCallback?.videoClick(
                data.get(position),
                data
            )
        })
        holder.binding.mediaInfo = data.get(position)

        Glide.with(holder.binding.imageVideo.context)
            .asBitmap()
            .load(data.get(position).filePath) // or URI/path
            .into(holder.binding.imageVideo);


    }

    override fun getItemCount(): Int {
        return data.size
    }


}