package com.shaejaz.artigen.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


class ColorPicker : View, OnClickListener {
    private var selectedColor: String? = null
    private var paint = Paint()
    private lateinit var selectedColorChangeListener: (color: String) -> Unit

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        setOnClickListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var color = if (selectedColor == null) {
            Color.WHITE
        } else {
            Color.parseColor("#$selectedColor")
        }

        paint.color = color
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.color = Color.GRAY
        paint.strokeWidth = 4f
        paint.style = Paint.Style.STROKE
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    fun setSelectedColorChangedListener(function: (color: String) -> Unit) {
        selectedColorChangeListener = function
    }

    override fun onClick(p0: View?) {
        ColorPickerDialog.Builder(context)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("Select", ColorEnvelopeListener { envelope, _ ->
                selectedColor = envelope.hexCode
                selectedColorChangeListener(selectedColor!!)
            })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialogInterface, _ -> dialogInterface.dismiss() })
            .attachAlphaSlideBar(false)
            .attachBrightnessSlideBar(true) // the default value is true.
            .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
            .show()
    }
}