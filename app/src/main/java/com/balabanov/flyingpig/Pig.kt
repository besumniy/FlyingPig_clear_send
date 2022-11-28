package com.balabanov.flyingpig

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable

class Pig(drawable: Drawable, rect: Rect, containerSize:Array<Int> ) {
    var drawable=drawable
    var rect=rect
    var smaller=containerSize[1]/80
    var rectC=Rect(rect.left+smaller,rect.top+smaller,rect.right-smaller,rect.bottom-smaller)
    var a=containerSize[1]/1600.0//200
    var vect=0.0
    fun update(){
        rect.top-=vect.toInt()
        rectC.top-=vect.toInt()
        rect.bottom-=vect.toInt()
        rectC.bottom-=vect.toInt()
        vect-=a
    }
    fun draw(canvas: Canvas?){
        if(canvas==null)return
        drawable.setBounds(rect)
        drawable.draw(canvas)
    }
}