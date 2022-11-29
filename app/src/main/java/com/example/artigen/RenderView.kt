package com.example.artigen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class RenderView : View {
    private val painter = Paint()
    private val numLines = 50
    private val lineColors = arrayOf(Color.CYAN, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.BLACK, Color.LTGRAY)
    var pixelArray: ArrayList<ArrayList<ArrayList<Int>>>? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onDraw(canvas: Canvas?) {
        painter.isAntiAlias = false
        painter.style = Paint.Style.FILL

        painter.color = Color.WHITE
        canvas?.drawRect(0F, 0F, width.toFloat(), height.toFloat(), painter)

        val xLowerBound = 0
        val xUpperBound = width
        val yLowerBound = 0
        val yUpperBond = height

        for (i in 0 until numLines) {
            val startX = (xLowerBound..xUpperBound).random()
            val startY = (yLowerBound..yUpperBond).random()
            val stopX = (xLowerBound..xUpperBound).random()
            val stopY = (yLowerBound..yUpperBond).random()

            painter.color = lineColors.random()
            canvas?.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), painter)
        }

        if (pixelArray != null) {
            val startX = 0
            val startY = 0

            pixelArray?.forEachIndexed { idxRow, row ->
                row.forEachIndexed { idxY, y ->
                    painter.color = Color.rgb(y[0], y[1], y[2])
                    canvas?.drawPoint((startX + idxRow).toFloat(), (startY + idxY).toFloat(), painter)
                }
            }
        }
    }
}