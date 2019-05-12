package com.example.pain_1

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import codestart.info.kotlinphoto.R
import kotlinx.android.synthetic.main.activity_astar.*

class AStarActivity : AppCompatActivity() {
    var imag = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astar)
        var sw: Int
        var sh: Int
        var mode=0
        var Sset=false
        var sx=-1
        var sy=-1
        var Fset=false
        var fx=-1
        var fy=-1
        clear()
        space.setImageBitmap(imag)
        buttonGenerate.setOnClickListener {
            sw=normal(text_wight.text.toString().toInt())
            sh=normal(text_height.text.toString().toInt())
            imag = Bitmap.createBitmap(sw, sh, Bitmap.Config.ARGB_8888)
            clear()
            space.setImageBitmap(imag)
        }
        this.space.setOnTouchListener { v, MotionEvent ->
            val x=(MotionEvent.x-space.x)*imag.width/space.width
            val y=(MotionEvent.y-space.y)*imag.height/space.height
            var red=0
            var green=0
            var blue=0
            if (mode==1)
            {
                red=255
                green=255
                blue=255
            }
            if (mode==2)
            {
                if (Sset==true)
                    imag.setPixel(sx,sy,Color.rgb(red,green, blue))
                Sset=true
                sx=x.toInt()
                sy=y.toInt()
                red=0
                green=255
                blue=0

            }
            if (mode==3)
            {
                if (Fset==true)
                    imag.setPixel(fx,fy,Color.rgb(red,green, blue))
                Fset=true
                fx=x.toInt()
                fy=y.toInt()
                red=255
                green=0
                blue=0
            }
            imag.setPixel(x.toInt(),y.toInt(),Color.rgb(red,green, blue))
            space.setImageBitmap(imag)
            true
        }

        buttonStart.setOnClickListener {
            mode=2
        }
        buttonWall.setOnClickListener {
            mode=1
        }
        buttonFinish.setOnClickListener {
            mode=3
        }
    }

    fun clear()
    {
        var i=0
        while (i<imag.width)
        {
            var t=0
            while (t<imag.height)
            {
                val newPixel = Color.rgb(
                    0,0,0
                )
                imag.setPixel(i,t,newPixel)
                t++
            }
            i++
        }
    }

    fun normal(i:Int):Int{
        if (i<2)
            return 2
        if (i>1000)
            return 1000
        return i
    }
}
