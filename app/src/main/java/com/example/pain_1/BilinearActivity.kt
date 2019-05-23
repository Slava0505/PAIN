package com.example.pain_1

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import codestart.info.kotlinphoto.R
import kotlinx.android.synthetic.main.activity_bilinear.*
import java.lang.Math.abs
import java.lang.Math.random
import kotlin.math.floor

class BilinearActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bilinear)
        var cururi=intent.data
        imageOrig.setImageURI(cururi)
        imageCanvas.setImageURI(cururi)
        var opoint1x = 0
        var opoint1y = 0
        var opoint2x = imageOrig.height-1
        var opoint2y = 0
        var opoint3x = imageOrig.height-1
        var opoint3y = imageOrig.width-1
        var rpoint1x = 0
        var rpoint1y = 0
        var rpoint2x = imageOrig.height-1
        var rpoint2y = 0
        var rpoint3x = imageOrig.height-1
        var rpoint3y = imageOrig.width-1
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
            Toast.makeText(this,"You put point on "+x+" "+y,Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this,"You put point on "+x+" "+y,Toast.LENGTH_SHORT).show()
            true}
            buttonDo.setOnClickListener {
                val origbmp = (imageOrig.drawable as BitmapDrawable).bitmap
                val a=dis(rpoint1x,rpoint2x,rpoint3x,opoint1y,opoint2y,opoint3y)/dis(opoint1x,opoint2x,opoint3x,opoint1y,opoint2y,opoint3y)
                val b=dis(opoint1x,opoint2x,opoint3x,rpoint1x,rpoint2x,rpoint3x)/dis(opoint1x,opoint2x,opoint3x,opoint1y,opoint2y,opoint3y)
                val c=rpoint1x-a*opoint1x-b*opoint1y
                val d=dis(rpoint1y,rpoint2y,rpoint3y,opoint1y,opoint2y,opoint3y)/dis(opoint1x,opoint2x,opoint3x,opoint1y,opoint2y,opoint3y)
                val e=dis(opoint1x,opoint2x,opoint3x,rpoint1y,rpoint2y,rpoint3y)/dis(opoint1x,opoint2x,opoint3x,opoint1y,opoint2y,opoint3y)
                val f=rpoint1y-d*opoint1x-e*opoint1y
                var i=0
                var tmaxy=f
                var tmaxx=c
                var tminy=f
                var tminx=c
                while (i<origbmp.width-1) {
                    var t = 0
                    while (t < origbmp.height - 1) {
                        if ((i*d+t*e+f)>tmaxy)
                            tmaxy=i*d+t*e+f
                        if ((i*d+t*e+f)<tminy)
                            tminy=i*d+t*e+f
                        if ((i*a+t*b+c)<tminx)
                            tminx=i*a+t*b+c
                        if ((i*a+t*b+c)>tmaxx)
                            tmaxx=i*a+t*b+c
                        t++
                    }
                    i++
                }
                var ajx=floor((tmaxx-tminx)*origbmp.height/(tmaxy-tminy)).toInt()+1
                if ((ajx/(origbmp.height+1))>3)
                    ajx=origbmp.height+1
                val canvasbmp = Bitmap.createBitmap(
                    ajx,
                    origbmp.height+1,
                    Bitmap.Config.ARGB_8888
                )
                clear(canvasbmp)
                i=0
                while (i<origbmp.width-1)
                {
                    var t=0
                    while (t<origbmp.height-1)
                    {
                        val pxcolor=mid(origbmp.getPixel(i,t),origbmp.getPixel(i+1,t),origbmp.getPixel(i,t+1),origbmp.getPixel(i+1,t+1))
                        var tx= (i*a+t*b+c-tminx)*origbmp.height/(tmaxy-tminy)
                        var ty= (i*d+t*e+f-tminy)*origbmp.height/(tmaxy-tminy)
                        if (tx<0)
                        {
                            tx=0.0
                        }
                        if (tx>=canvasbmp.width)
                        {
                            tx=canvasbmp.width-1.0
                        }
                        if (ty<0)
                        {
                            ty=0.0
                        }
                        if (ty>=canvasbmp.height)
                        {
                            ty=canvasbmp.height-1.0
                        }
                        var ncolor=canvasbmp.getPixel(floor(tx).toInt(),floor(ty).toInt())
                        if ((Color.red(ncolor)!=0)or(Color.blue(ncolor)!=0)or(Color.green(ncolor)!=0))
                        {
                            if (random()<0.5)
                            ncolor=pxcolor

                        }
                        else ncolor=pxcolor
                        canvasbmp.setPixel(floor(tx).toInt(),floor(ty).toInt(),ncolor)
                        t++
                    }
                    i++
                }
                trinormal(canvasbmp)
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

    fun clear(
        bmp:Bitmap
    ) {
        var i=0
        while (i<bmp.width)
        {
            var t=0
            while (t<bmp.height)
            {

                bmp.setPixel(i,t,Color.argb(0,0,0,0))
                t++
            }
            i++
        }
    }

    fun trinormal(
        bmp:Bitmap
    ){
        var i=1
        while (i<bmp.width-1)
        {
            var t=1
            while (t<bmp.height-1)
            {
                var vls=ArrayList<Int>()
                var col=bmp.getPixel(i,t)
                if (Color.alpha(col)==0)
                {
                    var apx=-1
                    while (apx<2)
                    {
                        var apy=-1
                        while (apy<2)
                        {
                            val tm=bmp.getPixel(i+apx,t+apy)
                            if (Color.alpha(tm)!=0)
                            {
                                vls.add(tm)
                            }
                            apy++
                        }
                        apx++
                    }
                    vls.sort()
                    if (vls.isNotEmpty()) {
                        col = vls[vls.size / 2]
                        bmp.setPixel(i, t, col)
                    }
                }
                t++
            }
            i++
        }
    }

    fun mid(
        a1:Int,
        a2:Int,
        a3:Int,
        a4:Int
    ):Int{
        var apr=(a1+a2+a3+a4)/4
        return when {
            (abs(a1-apr)<abs(a2-apr))and(abs(a1-apr)<abs(a3-apr))and(abs(a1-apr)<abs(a4-apr)) -> a1
            (abs(a2-apr)<abs(a3-apr))and(abs(a2-apr)<abs(a4-apr)) -> a2
            abs(a3-apr)<abs(a4-apr) -> a3
            else -> a4
        }
    }
}

