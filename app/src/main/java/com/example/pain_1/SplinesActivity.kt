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
                        if (xi < 0) {
                            val xi = 0
                        }
                        if (yi < 0) {
                            val yi = 0
                        }
                        if (xi >= imag.width) {
                            val xi = imag.width - 1
                        }
                        if (yi >= imag.height) {
                            val yi = imag.height - 1
                        }
                        imag.setPixel(xi, yi, Color.rgb(red, green, blue))
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

        val u = ArrayList<Float>()

        val h = ArrayList<Float>()
        val q = ArrayList<Float>()

        val l = ArrayList<Float>()
        val z = ArrayList<Float>()

        for (i in 0..n-1){
            h.add(X[i+1]-X[i])
        }
        q.add(0.toFloat())
        for (i in 1..n-1){
            q.add(3*(a[i+1]-a[i])/h[i]-3*(a[i]-a[i-1])/h[i-1])
        }

        l.add(1.toFloat())
        u.add(0.toFloat())
        z.add(0.toFloat())
        for (i in 1..n-1){
            l.add(2*(X[i+1]-X[i-1])-h[i-1]*u[i-1])
            u.add(h[i]/l[i])
            z.add((q[i]-h[i-1]*z[i-1])/l[i])
        }

        l.add(1.toFloat())
        z.add(0.toFloat())
        c[n]=0.toFloat()
        for (i in n-1 downTo 0){
            c[i] = z[i] - u[i]*c[i+1]
            b[i] = (a[i+1]-a[i])/h[i] - h[i]*(c[i+1]+2*c[i])/3
            d[i] = (c[i+1]-c[i])/(3*h[i])
        }
        println(b)
        val red = 255
        val green = 255
        val blue = 255
        for (i in 1..n){
            val xi = X[i]
            for (x in X[i-1].toInt()..xi.toInt()){
                val diff = (x-xi).toFloat()
                var s = (a[i]+b[i-1]*diff+c[i-1]*diff*diff+d[i-1]*diff*diff*diff).toInt()
                if (s < 0) {
                    continue
                }
                if (s >= imag.height) {
                    continue
                }

                for (xi in x - imag.width / 300..x + imag.width / 300) {
                    for (yi in s - imag.height / 300..s + imag.height / 300) {
                        if (xi < 0) {
                            continue
                        }
                        if (yi < 0) {
                            continue
                        }
                        if (xi >= imag.width) {
                            continue
                        }
                        if (yi >= imag.height) {
                            continue
                        }
                        imag.setPixel(xi, yi, Color.rgb(red, green, blue))
                    }
                }
                imag.setPixel(x, s, Color.rgb(red, green, blue))

            }
        }

        image1.setImageBitmap(imag)
    }
}
