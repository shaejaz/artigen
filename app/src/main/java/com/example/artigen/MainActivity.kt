package com.example.artigen

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    companion object {
        init {
            System.loadLibrary("artigen_android")
        }
    }

    private var button: Button? = null

    private external fun invokeCallbackViaJNI(): ArrayList<ArrayList<ArrayList<Int>>>

    private suspend fun invokeCallbackViaJNIAsync(): ArrayList<ArrayList<ArrayList<Int>>> {
        return withContext(Dispatchers.Default) {
            return@withContext invokeCallbackViaJNI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val render = findViewById<RenderView>(R.id.renderView)

        button = findViewById<Button>(R.id.toast_button)
        button?.setOnClickListener {

            GlobalScope.launch {
                val l = invokeCallbackViaJNIAsync()
                render.pixelArray = l
                render.postInvalidate()
            }
        }
    }
}