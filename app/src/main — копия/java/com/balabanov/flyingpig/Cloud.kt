package com.balabanov.flyingpig

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable

class Cloud(drawable: Drawable, rect: Rect, containerSize:Array<Int> ) {
    var drawable=drawable
    var rect=rect

    fun update(go:Int){
        rect.left-=go
        rect.right-=go}

    fun draw(canvas: Canvas?){
        if(canvas==null)return
        drawable.setBounds(rect)
        drawable.draw(canvas)
    }
}