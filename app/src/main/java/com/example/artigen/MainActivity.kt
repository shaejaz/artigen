package com.example.artigen

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    companion object {
        init {
            System.loadLibrary("artigen_android")
        }
    }

    private var button: Button? = null

    private external fun invokeCallbackViaJNI(): ArrayList<ArrayList<ArrayList<Integer>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<Button>(R.id.toast_button)
        button?.setOnClickListener {
            val l = invokeCallbackViaJNI()

            for (i in l) {
                for (j in i) {
                    Log.i("TESTING", "Item Row $i: $j")
                }
            }
        }
    }
}