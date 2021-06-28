package com.test.myapplication.dashboard.view

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.myapplication.MyApplication
import com.test.myapplication.R
import com.test.myapplication.dashboard.adapter.VideoListAdapter
import com.test.myapplication.dashboard.model.MediaFileInfo
import com.test.myapplication.dashboard.viewmodel.MainActivityViewModel
import com.test.myapplication.databinding.ActivityMainBinding
import com.test.myapplication.di.ViewModelFactory
import com.test.myapplication.helper.Helper
import com.test.myapplication.listener.DialogButtonClickListener
import java.io.Serializable
import javax.inject.Inject


class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    var model: MainActivityViewModel? = null
    var activityMainBinding: ActivityMainBinding? = null
    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1001
    private var VideList: RecyclerView? = null
    var recycler_orient: String? = "grid"

    @Inject
    lateinit var videoListAdapter: VideoListAdapter


    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(activityMainBinding?.toolbar)
        VideList = activityMainBinding?.VideList
        (application as MyApplication).appComponent?.doMainInjection(this)
        model = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        checkRequiredPermission();

    }

    private fun permissionsGranted() {

        model?.datafromFile()

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            recycler_orient = "3grid"
            VideList =
                setLayout(activityMainBinding!!.root.findViewById(R.id.VideList), recycler_orient)
        } else {
            // In portrait
            recycler_orient = "grid"
            VideList = setLayout(activityMainBinding!!.root.findViewById(R.id.VideList), recycler_orient)
        }



        model?.getAllData()?.observe(this, Observer {
            videoListAdapter.setVideoData(it, object : VideoListAdapter.VideoCallback {
                override fun videoClick(
                    mediaFileInfo: MediaFileInfo,
                    data: MutableList<MediaFileInfo>
                ) {
                    data.remove(mediaFileInfo)
                    startActivity(
                        Intent(this@MainActivity, VideoDataPlayer::class.java)
                            .putExtra("data", mediaFileInfo)
                            .putExtra("list", data as Serializable)
                    )
                }
            })
            activityMainBinding?.VideList?.adapter = videoListAdapter
        })

    }

    private fun consumeResponse(apiResponse: MutableList<MediaFileInfo>?) {
        Log.i("Here", "aa" + apiResponse.toString())

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                permissionsGranted();
            } else {
                // User refused to grant permission.
                checkRequiredPermission()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkRequiredPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("permission", "NOT granted");
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            );
        } else {
            Log.d("permission", "granted");
            permissionsGranted();
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.searchVideo) {
            Helper().showCustomDialog(
                getString(R.string.search_message_dialog),
                getString(R.string.search),
                this,
                object : DialogButtonClickListener {
                    override fun onDialogButtonClicked(query: String?, dialog: Dialog) {
                        dialog.dismiss()
                        model?.searchVideo(query)?.observe(
                            this@MainActivity,
                            Observer<MutableList<MediaFileInfo>> { video ->
                                videoListAdapter.setVideoData(video,
                                    object : VideoListAdapter.VideoCallback {
                                        override fun videoClick(
                                            mediaFileInfo: MediaFileInfo,
                                            data: MutableList<MediaFileInfo>
                                        ) {
                                            data.remove(mediaFileInfo)
                                            startActivity(
                                                Intent(
                                                    this@MainActivity,
                                                    VideoDataPlayer::class.java
                                                )
                                                    .putExtra("data", mediaFileInfo)
                                                    .putExtra("list", data as Serializable)
                                            )
                                        }
                                    })
                            })
                    }
                })
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun setLayout(view: RecyclerView, orientation: String?): RecyclerView {
        view.setHasFixedSize(true)
        view.isNestedScrollingEnabled = false
        view.itemAnimator = DefaultItemAnimator()
        val manager = LinearLayoutManager(this)
        when (orientation) {

            "grid" -> {
                view.layoutManager = GridLayoutManager(this, 2)
                if (view.itemDecorationCount == 0) {
                    view.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(0), true))
                }

            }
            "3grid" -> {
                view.layoutManager = GridLayoutManager(this, 3)
                if (view.itemDecorationCount == 0) {
                    view.addItemDecoration(GridSpacingItemDecoration(3, dpToPx(4), true))
                }

            }

        }
        return view
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }


}