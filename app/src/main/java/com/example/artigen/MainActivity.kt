package com.example.artigen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    companion object {
        init {
            System.loadLibrary("artigen_android")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rustBridge = RustBridge()
        val sampleText: String = rustBridge.invokeFunctionBridge("test text")

        val toast = Toast.makeText(this@MainActivity, sampleText, Toast.LENGTH_LONG)
        toast.show()
    }
}