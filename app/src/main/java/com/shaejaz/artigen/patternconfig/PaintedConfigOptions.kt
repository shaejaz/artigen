package com.shaejaz.artigen.patternconfig

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shaejaz.artigen.R
import com.shaejaz.artigen.utils.ColorPicker

class PaintedConfigOptions : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_painted_config_options, container, false)

        val colorPicker = view.findViewById<ColorPicker>(R.id.colorPickerView)
        colorPicker.setSelectedColorChangedListener {
            Log.i("MAIN", it)
            colorPicker.postInvalidate()
        }

        return view
    }
}