package com.example.gyrodatasakhalin.battery.helper

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.gyrodatasakhalin.battery.listener.BatteryClickListener

class SwipeButton(private val context: Context, private val text: String,
                  private val textSize: Int,
                  private val imageResIdL: Int,
                  private val color: Int,
                  private val listener: BatteryClickListener) {

    private val position: Int = 0
    private val clickRegion: RectF? = null
    private val resoources: Resources

    init {
        resoources = context.resources
    }

    fun onClick(x: Float, y: Float) : Boolean {
        if (clickRegion != null && clickRegion!!.contains(x, y)){
            listener.onClick(position)
            return true
        }
        return false
    }

    fun onDraw(c: Canvas, rectF: RectF, position: Int){
        val p = Paint()
        p.color = color
        c.drawRect(rectF, p)

        //Text
        p.color = Color.WHITE
        p.textSize = textSize.toFloat()

        val r = Rect()
        val cHeight = rectF.height()
        val cWidth = rectF.width()
        p.textAlign = Paint.Align.LEFT
        p.getTextBounds(text, 0, text.length, r)
        var x = 0f
        var y = 0f
        if (imageResIdL == 0) {
            x = cWidth / 2f - r.width() / 2f - r.left.toFloat()
            y = cHeight / 2f + r.height() / 2f - r.bottom.toFloat()
            c.drawText(text, rectF.left + x, rectF.top + y, p)
        }else{
            val d = ContextCompat.getDrawable(context, imageResIdL)
            val bitmap = drawableToBitmap(d)
            c.drawBitmap(bitmap, (rectF.left + rectF.right) / 2, (rectF.top + rectF.bottom) / 2, p)
        }
    }

    private fun drawableToBitmap(d: Drawable?) : Bitmap {
        if (d is BitmapDrawable) return d.bitmap
        val bitmap = Bitmap.createBitmap(d!!.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        d.setBounds(0, 0, canvas.width, canvas.height)
        d.draw(canvas)
        return bitmap
    }
}