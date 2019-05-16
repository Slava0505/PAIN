package com.example.pain_1

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import codestart.info.kotlinphoto.R
import kotlinx.android.synthetic.main.activity_bilinear.*
import kotlin.math.floor

class BilinearActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bilinear)
        var cururi=intent.data
        imageOrig.setImageURI(cururi)
        var opoint1x = 0
        var opoint1y = 0
        var opoint2x = imageOrig.height
        var opoint2y = 0
        var opoint3x = imageOrig.height
        var opoint3y = imageOrig.width
        var rpoint1x = 0
        var rpoint1y = 0
        var rpoint2x = imageOrig.height
        var rpoint2y = 0
        var rpoint3x = imageOrig.height
        var rpoint3y = imageOrig.width
        val canvasbmp = Bitmap.createBitmap(
            imageOrig.width,
            imageOrig.height,
            Bitmap.Config.ARGB_8888
        )
        val origbmp = (imageOrig.drawable as BitmapDrawable).bitmap
        var mode = 1

        buttonPoint1.setOnClickListener {
            mode=1
        }
        buttonPoint2.setOnClickListener {
            mode=2
        }
        buttonPoint3.setOnClickListener {
            mode=3
        }
        this.imageOrig.setOnTouchListener{v, MotionEvent ->
        var x=(floor(MotionEvent.x)).toInt()
        var y=(floor(MotionEvent.y)).toInt()
        if (x<0)
        {
            x=0
        }
        if (y<0)
        {
            y=0
        }
        if (x>=imageOrig.width)
        {
            x=imageOrig.width-1
        }
        if (y>=imageOrig.height)
        {
            y=imageOrig.height-1
        }
        if (mode==1)
        {
            opoint1x=x
            opoint1y=y
        }
        if (mode==2)
        {
            opoint2x=x
            opoint2y=y
        }
        if (mode==3)
        {
            opoint3x=x
            opoint3y=y
        }
            Toast.makeText(this,"You put point on "+x+" "+y,Toast.LENGTH_SHORT)
        true}
        this.imageCanvas.setOnTouchListener{v, MotionEvent ->
            var x=(floor(MotionEvent.x)).toInt()
            var y=(floor(MotionEvent.y)).toInt()
            if (x<0)
            {
                x=0
            }
            if (y<0)
            {
                y=0
            }
            if (x>=imageCanvas.width)
            {
                x=imageCanvas.width-1
            }
            if (y>=imageCanvas.height)
            {
                y=imageCanvas.height-1
            }
            if (mode==1)
            {
                rpoint1x=x
                rpoint1y=y
            }
            if (mode==2)
            {
                rpoint2x=x
                rpoint2y=y
            }
            if (mode==3)
            {
                rpoint3x=x
                rpoint3y=y
            }
            Toast.makeText(this,"You put point on "+x+" "+y,Toast.LENGTH_SHORT)
            true}
            buttonDo.setOnClickListener {
                val a=dis(opoint1x,opoint2x,opoint3x,opoint1y,opoint2y,opoint3y)/dis(rpoint1x,rpoint2x,rpoint3x,opoint1y,opoint2y,opoint3y)
                val b=dis(opoint1x,opoint2x,opoint3x,opoint1y,opoint2y,opoint3y)/dis(opoint1x,opoint2x,opoint3x,rpoint1x,rpoint2x,rpoint3x)
                val c=rpoint1x-a*opoint1x-b*opoint1y
                val d=dis(opoint1x,opoint2x,opoint3x,opoint1y,opoint2y,opoint3y)/dis(rpoint1y,rpoint2y,rpoint3y,opoint1y,opoint2y,opoint3y)
                val e=dis(opoint1x,opoint2x,opoint3x,opoint1y,opoint2y,opoint3y)/dis(opoint1x,opoint2x,opoint3x,rpoint1y,rpoint2y,rpoint3y)
                val f=rpoint1y-d*opoint1x-e*opoint1y
                var i=0
                while (i<origbmp.height)
                {
                    var t=0
                    while (t<origbmp.width)
                    {
                        val pxcolor=origbmp.getPixel(i,t)
                        var ncolor=canvasbmp.getPixel(floor(i*a+t*b+c).toInt(),floor(i*d+t*e+f).toInt())
                        if (ncolor!=0)
                        {
                            ncolor=(ncolor+pxcolor)/2
                        }
                        canvasbmp.setPixel(floor(i*a+t*b+c).toInt(),floor(i*d+t*e+f).toInt(),ncolor)
                        t++
                    }
                    i++
                }
                imageCanvas.setImageBitmap(canvasbmp)
            }
    }

    fun dis(
        x1:Int,
        x2:Int,
        x3:Int,
        y1:Int,
        y2:Int,
        y3:Int
    ):Double{
        return (x1*y2+y1*x3+x2*y3-x3*y2-x1*y3-y1*x2)*1.0
    }
}
