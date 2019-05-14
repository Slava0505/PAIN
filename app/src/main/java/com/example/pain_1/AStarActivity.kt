package com.example.pain_1

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import codestart.info.kotlinphoto.R
import kotlinx.android.synthetic.main.activity_astar.*
import kotlin.math.floor

class AStarActivity : AppCompatActivity() {
    var imag = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astar)
        imag = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        System.loadLibrary("native-lib")
        var sw: Int
        var sh: Int
        var mode=0
        var Sset=false
        var sx=0
        var sy=0
        var Fset=false
        var fx=99
        var fy=99
        clear()
        space.setImageBitmap(imag)
        buttonGenerate.setOnClickListener {
            sw=normal(text_wight.text.toString().toInt())
            sh=normal(text_height.text.toString().toInt())
            imag = Bitmap.createBitmap(sw, sh, Bitmap.Config.ARGB_8888)
            fx=sw-1
            fy=sh-1
            sx=0
            sy=0
            clear()
            space.setImageBitmap(imag)
        }
        this.space.setOnTouchListener { v, MotionEvent ->
            var x=(floor(MotionEvent.x)*imag.width/space.width).toInt()
            var y=(floor(MotionEvent.y)*imag.height/space.height).toInt()
            if (x<0)
            {
                x=0
            }
            if (y<0)
            {
                y=0
            }
            if (x>=imag.width)
            {
                x=imag.width-1
            }
            if (y>=imag.height)
            {
                y=imag.height-1
            }
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
                if (Sset)
                    imag.setPixel(sx,sy,Color.rgb(red,green, blue))
                Sset=true
                sx=x
                sy=y
                red=0
                green=255
                blue=0

            }
            if (mode==3)
            {
                if (Fset)
                    imag.setPixel(fx,fy,Color.rgb(red,green, blue))
                Fset=true
                fx=x
                fy=y
                red=255
                green=0
                blue=0
            }
            imag.setPixel(x,y,Color.rgb(red,green, blue))
            space.setImageBitmap(Bitmap.createScaledBitmap(imag,1000,1000,false))
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

        buttonGoA.setOnClickListener {
            var arrTmp=IntArray(imag.width*imag.height){i->0}
            val eu=euristicchoice.selectedItemPosition
            val dim=transitionchoice.selectedItemPosition
            var i=0
            while (i<imag.width)
            {
                var t=0
                while (t<imag.height)
                {
                    val pix=imag.getPixel(i,t)
                    val matval=(Color.red(pix)+Color.blue(pix)+Color.green(pix))%2
                    arrTmp[i*imag.height+t]=matval
                    t++
                }
                i++
            }
            Toast.makeText(this,astar(imag.width,imag.height,arrTmp,sx,sy,fx,fy,eu,dim),Toast.LENGTH_SHORT).show()
            i=0
            while (i<imag.width)
            {
                var t=0
                while (t<imag.height)
                {
                    if (arrTmp[i*imag.height+t]==1)
                    {
                        imag.setPixel(i,t,Color.rgb(255,255, 255))
                    }
                    if (arrTmp[i*imag.height+t]==2)
                    {
                        imag.setPixel(i,t,Color.rgb(255,255, 0))
                    }
                    if (arrTmp[i*imag.height+t]==3)
                    {
                        imag.setPixel(i,t,Color.rgb(0,200, 0))
                    }
                    if (arrTmp[i*imag.height+t]==0)
                    {
                        imag.setPixel(i,t,Color.rgb(0,0,0))
                    }
                    if ((i==fx)and(t==fy))
                    {
                        imag.setPixel(i,t,Color.rgb(255,0,0))
                    }

                    if ((i==sx)and(t==sy))
                    {
                        imag.setPixel(i,t,Color.rgb(0,255,0))
                    }
                    t++
                }
                i++
            }
            space.setImageBitmap(Bitmap.createScaledBitmap(imag,1000,1000,false))
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

    external fun astar(
        w:Int,
        h:Int,
        im:IntArray,
        bx:Int,
        by:Int,
        ex:Int,
        ey:Int,
        eu:Int,
        dim:Int
    ):String

    fun normal(i:Int):Int{
        if (i<2)
            return 2
        if (i>1000)
            return 1000
        return i
    }
}
