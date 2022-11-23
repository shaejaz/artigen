package com.example.artigen

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), JNICallback {
    companion object {
        init {
            System.loadLibrary("artigen_android")
        }
    }

    private var button: Button? = null

    private external fun invokeCallbackViaJNI(callback: JNICallback)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<Button>(R.id.toast_button)
        button?.setOnClickListener {
            invokeCallbackViaJNI(this)
        }
    }

    override fun callback(string: String?) {
        val toast = Toast.makeText(this@MainActivity, "From JNI: $string", Toast.LENGTH_LONG)
        toast.show()
    }
}