package com.example.pain_1

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_splines.*
import kotlin.math.floor
import java.io.FileOutputStream
import java.io.IOException
import android.os.Environment
import android.view.MotionEvent
import java.io.File
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList


class SplinesActivity : AppCompatActivity() {
    val X = ArrayList<Float>()
    val a = ArrayList<Float>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(codestart.info.kotlinphoto.R.layout.activity_splines)
        val cururi = intent.data
        image1.setImageURI(cururi)




        this.image1.setOnTouchListener() { v, motionEvent ->
            val start_imag = (image1.drawable as BitmapDrawable).bitmap
            val imag = start_imag.copy(start_imag.getConfig(), true)
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                var x = (floor(motionEvent.x) * imag.width / image1.width).toInt()
                var y = (floor(motionEvent.y) * imag.height / image1.height).toInt()
                if (x < 0) {
                    x = 0
                }
                if (y < 0) {
                    y = 0
                }
                if (x >= imag.width) {
                    x = imag.width - 1
                }
                if (y >= imag.height) {
                    y = imag.height - 1
                }
                val red = 255
                val green = 255
                val blue = 255
                X.add(x.toFloat())
                a.add(y.toFloat())
                for (xi in x - imag.width / 100..x + imag.width / 100) {
                    for (yi in y - imag.height / 100..y + imag.height / 100) {
                        var xI = xi
                        var yI = yi
                        if (xi < 0) {
                             xI = 0
                        }
                        if (yi < 0) {
                             yI = 0
                        }
                        if (xi >= imag.width) {
                             xI = imag.width - 1
                        }
                        if (yi >= imag.height) {
                             yI = imag.height - 1
                        }
                        imag.setPixel(xI, yI, Color.rgb(red, green, blue))
                    }
                }
            }

            image1.setImageBitmap(imag)

            if (X.size==5) draw_splines(imag)

            true
        }
    }


    fun bitmapToFile(bitmap:Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir("Images",Context.MODE_PRIVATE)
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


    fun draw_splines(imag: Bitmap)
    {
        val n = X.size-1
        val b =  FloatArray(n){ 0.toFloat() }
        val d = FloatArray(n){ 0.toFloat() }
        val c = FloatArray(n+1){ 0.toFloat() }

        val u = FloatArray(n){ 0.toFloat() }

        val h = FloatArray(n){ 0.toFloat() }
        val q = FloatArray(n){ 0.toFloat() }

        val l = FloatArray(n+1){ 0.toFloat() }
        val z = FloatArray(n+1){ 0.toFloat() }

        for (i in 0..n-1){
            h[i] = (X[i+1]-X[i])
        }
        for (i in 1..n-1){
            q[i] = ((3*(a[i+1]-a[i])/h[i]-3*(a[i]-a[i-1])/h[i-1]))
        }

        l[0] = (1.toFloat())
        u[0] = (0.toFloat())
        z[0] = (0.toFloat())
        for (i in 1..n-1){
            l[i] = (2*(X[i+1]-X[i-1])-h[i-1]*u[i-1])
            u[i] = (h[i]/l[i])
            z[i] = ((q[i]-h[i-1]*z[i-1])/l[i])
        }

        l[n] = (1.toFloat())
        z[n] = (0.toFloat())
        c[n]=0.toFloat()
        for (i in n-1 downTo 0){
            c[i] = z[i] - u[i]*c[i+1]
            b[i] = (a[i+1]-a[i])/h[i] - h[i]*(c[i+1]+2*c[i])/3
            d[i] = (c[i+1]-c[i])/(3*h[i])
        }
        println(a)
        println(b)
        println(c)
        println(d)

        val red = 255
        val green = 255
        val blue = 255
        var prevS = a[0].toInt()
        for (i in 1..n){
            val xi = X[i]
            val xi1 = X[i-1]

            if (xi>xi1){
                for (x in xi1.toInt()..xi.toInt()){
                    val diff = (x-xi1).toFloat()
                    var s = (a[i-1]+b[i-1]*diff+c[i-1]*diff*diff+d[i-1]*diff*diff*diff).toInt()
                    if (s < 0) {
                        continue
                    }
                    if (s >= imag.height) {
                        continue
                    }

                    for (xi in x - imag.width / 300..x + imag.width / 300) {
                        var addDown = 0
                        var addUp = 0
                        if(prevS>s)  addDown =  prevS - s
                        else addUp = s - prevS
                        for (yi in s - addDown - imag.height / 300..s + addUp + imag.height / 300) {
                            var finishY = yi
                            if (xi < 0) {
                                continue
                            }
                            if (yi < 0) {
                                finishY = 0
                            }
                            if (xi >= imag.width) {
                                continue
                            }
                            if (yi >= imag.height) {
                                finishY = imag.height-1
                            }

                            imag.setPixel(xi, finishY, Color.rgb(red, green, blue))
                        }
                    }
                    prevS = s
                    imag.setPixel(x, s, Color.rgb(red, green, blue))

                }
            }
            else{
                for (x in xi1.toInt() downTo xi.toInt()){
                    val diff = -(x-xi).toFloat()
                    var s = (a[i]+b[i-1]*diff+c[i-1]*diff*diff+d[i-1]*diff*diff*diff).toInt()
                    if (s < 0) {
                        continue
                    }
                    if (s >= imag.height) {
                        continue
                    }

                    for (xi in x - imag.width / 300..x + imag.width / 300) {
                        var addDown = 0
                        var addUp = 0
                        if(prevS>s)  addDown =  prevS - s
                        else addUp = s - prevS
                        for (yi in s - addUp - imag.height / 300..s +  addDown+ imag.height / 300) {
                            var finishY = yi
                            if (xi < 0) {
                                continue
                            }
                            if (yi < 0) {
                                finishY = 0
                            }
                            if (xi >= imag.width) {
                                continue
                            }
                            if (yi >= imag.height) {
                                finishY = imag.height-1
                            }

                            imag.setPixel(xi, finishY, Color.rgb(red, green, blue))
                        }
                    }
                    prevS = s
                    imag.setPixel(x, s, Color.rgb(red, green, blue))

                }
            }
        }

        image1.setImageBitmap(imag)
    }
}
