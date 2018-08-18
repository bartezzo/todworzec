package com.tobzzo.todworzec.dworzeckrakowski.tools

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Paint.Style
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.util.AttributeSet
import android.widget.RadioButton

class SegmentedControlButton : RadioButton {

    private var mX: Float = 0.toFloat()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    public override fun onDraw(canvas: Canvas) {

        val text = this.text.toString()
        val textPaint = Paint()
        textPaint.isAntiAlias = true
        val currentWidth = textPaint.measureText(text)
        val currentHeight = textPaint.measureText("x")

        // final float scale =
        // getContext().getResources().getDisplayMetrics().density;
        // float textSize = (int) (TEXT_SIZE * scale + 0.5f);
        textPaint.textSize = this.textSize
        textPaint.textAlign = Paint.Align.CENTER

        val canvasWidth = canvas.width.toFloat()
        val textWidth = textPaint.measureText(text)

        if (isChecked) {
            //GradientDrawable grad = new GradientDrawable(Orientation.TOP_BOTTOM, new int[] { 0xff7ed03d, 0xff4c9434 });
            val grad = GradientDrawable(Orientation.TOP_BOTTOM, intArrayOf(0xff4b8bbb.toInt(), 0xff236295.toInt()))
            grad.setBounds(0, 0, this.width, this.height)
            grad.draw(canvas)
            textPaint.color = Color.WHITE
        } else {
            val grad = GradientDrawable(Orientation.TOP_BOTTOM, intArrayOf(0xffb5b5b5.toInt(), 0xffa5a5a5.toInt()))
            grad.setBounds(0, 0, this.width, this.height)
            grad.draw(canvas)
            textPaint.color = 0xffcccccc.toInt()
        }

        val w = this.width / 2 - currentWidth
        val h = this.height / 2 + currentHeight
        canvas.drawText(text, mX, h, textPaint)

        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Style.STROKE
        val rect = Rect(0, 0, this.width, this.height)
        canvas.drawRect(rect, paint)

    }

    override fun onSizeChanged(w: Int, h: Int, ow: Int, oh: Int) {
        super.onSizeChanged(w, h, ow, oh)
        mX = w * 0.5f // remember the center of the screen
    }

    companion object {

        private val TEXT_SIZE = 16.0f
    }

}