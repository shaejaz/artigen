package com.shaejaz.artigen.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class ColorPicker(context: Context, attrs: AttributeSet) : View(context, attrs), OnClickListener {
    var selectedColor: String? = null
        set(value) {
            field = value
            this.postInvalidate()
        }

    private var paint = Paint()
    private lateinit var selectedColorChangeListener: (color: String) -> Unit

    init {
        setOnClickListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val color = if (selectedColor == null) {
            Color.WHITE
        } else {
            if (!selectedColor!!.startsWith("#")) {
                selectedColor = "#$selectedColor"
            }
            Color.parseColor(selectedColor)
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
        val view = ColorPickerDialog.Builder(context)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("Select", ColorEnvelopeListener { envelope, _ ->
                selectedColor = "#${envelope.hexCode}"
                selectedColorChangeListener(selectedColor!!)
            })
            .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
            .attachAlphaSlideBar(false)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)

        if (selectedColor != null) {
            view.colorPickerView.setInitialColor(Color.parseColor(selectedColor))
        }
        view.show()
    }
}