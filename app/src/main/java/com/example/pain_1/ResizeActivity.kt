package com.example.pain_1

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import codestart.info.kotlinphoto.R
import kotlinx.android.synthetic.main.activity_resize.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.math.*

class ResizeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resize)
        var cururi=intent.data
        imageForResize.setImageURI(cururi)
        var set=false
        var origbmp = (imageForResize.drawable as BitmapDrawable).bitmap

        buttonStart.setOnClickListener {
            if (!set)
            {
                set=true
                origbmp = (imageForResize.drawable as BitmapDrawable).bitmap
            }
            var coef=coeffText.text.toString().toDouble()*1.0
            if (coef<=0)
            {
                coeffText.setText("1")
                coef=1.0
                Toast.makeText(this,"Too small!",Toast.LENGTH_SHORT).show()
            }
            if (coef>10)
            {
                coeffText.setText("10")
                coef=10.0
                Toast.makeText(this,"Too big!",Toast.LENGTH_SHORT).show()
            }
            val a=dis(0.0, 0.0, coef,0.0,1.0,0.0)/(-1)
            val b=dis(0.0,0.0,1.0,0.0, 0.0, coef)/(-1)
            val c=0.0
            val d=dis(0.0, coef,0.0,0.0,1.0,0.0)/(-1)
            val e=dis(0.0,0.0,1.0,0.0, coef,0.0)/(-1)
            val f=0.0
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
            var ajx= floor((tmaxx-tminx)*origbmp.height/(tmaxy-tminy)).toInt()+1
            if ((ajx/(origbmp.height+1))>3) {
                ajx = origbmp.width + 1
                Toast.makeText(this,"Ratio is NOT FINE at all.Don't use this coordinates ever again", Toast.LENGTH_SHORT).show()
            }
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
                    var ncolor=canvasbmp.getPixel(floor(tx).toInt(), floor(ty).toInt())
                    if ((Color.red(ncolor)!=0)or(Color.blue(ncolor)!=0)or(Color.green(ncolor)!=0))
                    {
                        if (Math.random() <0.5)
                            ncolor=pxcolor

                    }
                    else ncolor=pxcolor
                    canvasbmp.setPixel(floor(tx).toInt(), floor(ty).toInt(),ncolor)
                    t++
                }
                i++
            }
            trinormal(canvasbmp)
            imageForResize.setImageBitmap(canvasbmp)
            Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show()
        }

        buttonAccept.setOnClickListener {
            cururi=bitmapToFile((imageForResize.drawable as BitmapDrawable).bitmap)
            val accept= Intent(this, EditorActivity::class.java)
            accept.data=cururi
            startActivity(accept)
        }

        buttonCancel.setOnClickListener {
            val cancel= Intent(this, EditorActivity::class.java)
            cancel.data=cururi
            startActivity(cancel)
        }
    }

    fun dis(
        x1:Double,
        x2:Double,
        x3:Double,
        y1:Double,
        y2:Double,
        y3:Double
    ):Double{
        return (x1*y2+y1*x3+x2*y3-x3*y2-x1*y3-y1*x2)*1.0
    }

    fun clear(
        bmp: Bitmap
    ) {
        var i=0
        while (i<bmp.width)
        {
            var t=0
            while (t<bmp.height)
            {

                bmp.setPixel(i,t, Color.argb(0,0,0,0))
                t++
            }
            i++
        }
    }

    fun trinormal(
        bmp: Bitmap
    ){
        var i=1
        while (i<bmp.width-1)
        {
            var t=1
            while (t<bmp.height-1)
            {
                val vls= ArrayList<Int>()
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
        val apr=(a1+a2+a3+a4)/4
        return when {
            (abs(a1-apr) < abs(a2-apr))and(abs(a1-apr) < abs(a3-apr))and(abs(a1-apr) < abs(a4-apr)) -> a1
            (abs(a2-apr) < abs(a3-apr))and(abs(a2-apr) < abs(a4-apr)) -> a2
            abs(a3-apr) < abs(a4-apr) -> a3
            else -> a4
        }
    }

    fun bitmapToFile(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

        try{
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }
}
