package com.example.artigen

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Base64

class MainActivity : AppCompatActivity() {
    companion object {
        init {
            System.loadLibrary("artigen_android")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val decoder = Base64.getDecoder()

    private var button: Button? = null

    private external fun invokeCallbackViaJNI(): String

    private suspend fun invokeCallbackViaJNIAsync(): String {
        return withContext(Dispatchers.Default) {
            return@withContext invokeCallbackViaJNI()
        }
    }

    private fun updateImage(bytes: ByteArray) {
        val imageView = findViewById<ImageView>(R.id.imageView)

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        imageView.setImageBitmap(bitmap)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val render = findViewById<RenderView>(R.id.renderView)

        button = findViewById(R.id.toast_button)
        button?.setOnClickListener {

            GlobalScope.launch {
                val code = invokeCallbackViaJNIAsync()
                val bytes = decoder.decode(code)

                this@MainActivity.runOnUiThread {
                    updateImage(bytes)
                }
            }
        }
    }
}