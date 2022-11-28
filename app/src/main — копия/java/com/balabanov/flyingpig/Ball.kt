package com.balabanov.flyingpig

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.graphics.toRect

class Ball(drawable: Drawable, rect: Rect,i:Int, containerSize:Array<Int>) {
    var drawable=drawable
    var rect=rect

    var i=i

    var width=rect.right-rect.left
    var height=rect.bottom-rect.top

    fun update(go:Int){
        rect.left-=go
        rect.right-=go

        i++
        if (i==360)i=0
    }
    fun draw(canvas: Canvas?){
        if(canvas==null)return

        drawable.setBounds(rect)
        canvas.save()
        canvas.rotate(i*1f,rect.left+width/2f,rect.top+height/2f)
        drawable.draw(canvas)
        canvas.restore()
    }
}