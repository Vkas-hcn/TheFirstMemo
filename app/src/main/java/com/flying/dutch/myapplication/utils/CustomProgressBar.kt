package com.flying.dutch.myapplication.utils


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.flying.dutch.myapplication.R

class CustomProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ProgressBar(context, attrs, defStyleAttr) {

    private val textPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.white)  // Text color
        textSize = 40f  // Text size
        isAntiAlias = true  // Anti-aliasing for smoother text
        textAlign = Paint.Align.CENTER  // Center align the text
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Calculate the percentage of progress
        val progressPercentage = progress * 100 / max

        // Calculate the position of the text
        val textX = width * progress / (max.toFloat()*2)
        val textY = (height / 2 - (textPaint.descent() + textPaint.ascent()) / 2)

        // Draw the progress text
        canvas.drawText("$progressPercentage%", textX, textY, textPaint)
    }
}
