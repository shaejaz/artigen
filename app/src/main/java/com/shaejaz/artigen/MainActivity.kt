package com.shaejaz.artigen

import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shaejaz.artigen.bottompanel.BottomPanel
import com.shaejaz.artigen.bottompanel.BottomPanelViewModel
import com.shaejaz.artigen.data.BlocksConfig
import com.shaejaz.artigen.image.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val imageViewModel by viewModels<ImageViewModel>()
    private val bottomPanelViewModel by viewModels<BottomPanelViewModel>()
    private var progressDialog: AlertDialog? = null

    private fun createLoadingDialog() {
        val b = AlertDialog.Builder(this@MainActivity)
        b.setMessage("Generating...")
        b.setCancelable(false)
        b.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            imageViewModel.cancelGenerateImage()
        }
        progressDialog = b.create()
    }

    private fun setInitialConfigData() {
        val size = Point()
        windowManager.defaultDisplay.getRealSize(size)
        imageViewModel.setDeviceXY(size.x, size.y)

        imageViewModel.setConfig(
            BlocksConfig(
                x = size.x,
                y = size.y,
                color1 = "5e062b",
                color2 = "171585",
                color3 = "b8cf38",
                bgColor = "ffffff",
                blockSize = 2,
                lineSize = 2,
                density = 1.0f,
            ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<BottomPanel>(R.id.bottom_container)
        }

        createLoadingDialog()
        setInitialConfigData()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    imageViewModel.image.collect {
                        imageView.setImageBitmap(it)
                        val config = imageViewModel.observeConfig().value
                        if (config != null) {
                            imageView.layoutParams.height = config.y
                            imageView.layoutParams.width = config.x
                        }
                    }
                }

                launch {
                    imageViewModel.imageGenerating.collect { generating ->
                        if (generating) {
                            if (!progressDialog!!.isShowing) {
                                progressDialog!!.show()
                            }
                        } else {
                            if (progressDialog != null && progressDialog!!.isShowing) {
                                progressDialog!!.dismiss()
                            }
                        }
                    }
                }

                launch {
                    bottomPanelViewModel.shareButtonClick.collect {
                        val image = imageViewModel.image.value

                        if (image != null) {
                            val d = Calendar.getInstance().time
                            val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                            val filename = formatter.format(d)

                            val uri = bottomPanelViewModel.saveBitmap(
                                this@MainActivity,
                                image,
                                Bitmap.CompressFormat.PNG,
                                "image/png",
                                filename
                            )

                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_STREAM, uri)
                                type = "image/png"
                            }
                            startActivity(Intent.createChooser(shareIntent, null))
                        }
                    }
                }
            }
        }
    }
}