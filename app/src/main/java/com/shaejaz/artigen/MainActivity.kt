package com.shaejaz.artigen

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shaejaz.artigen.image.ImageViewModel
import com.shaejaz.artigen.patternconfig.PatternConfigFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val imageViewModel by viewModels<ImageViewModel>()
    private var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)

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
                            toast = Toast.makeText(this@MainActivity, "Image has started generating", Toast.LENGTH_LONG)
                        } else {
                            toast = Toast.makeText(this@MainActivity, "Image generation finished", Toast.LENGTH_SHORT)
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

        button = findViewById(R.id.toast_button)
        button?.setOnClickListener {
            imageViewModel.generateImage()
        }

        val editConfigButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        editConfigButton.setOnClickListener {
            val frag = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
            if (supportFragmentManager.findFragmentById(R.id.fragment_container_view) == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<PatternConfigFragment>(R.id.fragment_container_view)
                }
            } else {
                if (savedInstanceState == null) {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        remove(frag as PatternConfigFragment)
                    }
                }
            }
            val elements = arrayOf(R.id.toast_button, R.id.floatingActionButton, R.id.imageButton, R.id.imageButton2)
            elements.forEach {
                toggleBottomElements(findViewById(it))
            }
        }
    }

    private fun toggleBottomElements(view: View) {
        when (view.visibility) {
            View.INVISIBLE -> view.visibility = View.VISIBLE
            View.VISIBLE -> view.visibility = View.INVISIBLE
        }
    }
}