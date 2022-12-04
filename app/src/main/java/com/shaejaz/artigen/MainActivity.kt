package com.shaejaz.artigen

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.shaejaz.artigen.bottompanel.BottomPanel
import com.shaejaz.artigen.image.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val imageViewModel by viewModels<ImageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<BottomPanel>(R.id.bottom_container)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    imageViewModel.image.collect {
                        imageView.setImageBitmap(it)
                    }
                }

                launch {
                    imageViewModel.imageGenerating.drop(1).collect { generating ->
                        var toast: Toast
                        if (generating) {
                            toast = Toast.makeText(
                                this@MainActivity,
                                "Image has started generating",
                                Toast.LENGTH_LONG
                            )
                        } else {
                            toast = Toast.makeText(
                                this@MainActivity,
                                "Image generation finished",
                                Toast.LENGTH_SHORT
                            )
                        }

                        toast.show()
                    }
                }

                launch {
                    imageViewModel.observeConfig().collect {
                        Log.i("ACTIVITY_MAIN", it.toString())
                    }
                }
            }
        }
    }
}