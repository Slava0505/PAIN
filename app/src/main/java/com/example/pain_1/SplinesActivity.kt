package com.example.pain_1

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.view.MotionEvent
import android.view.View.X
import android.widget.Toast
import codestart.info.kotlinphoto.R
import kotlinx.android.synthetic.main.activity_astar.*
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.activity_splines.*
import kotlin.math.floor

class SplinesActivity : AppCompatActivity() {
    val X = ArrayList<Int>()
    val Y = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splines)
        val cururi=intent.data
        image1.setImageURI(cururi)




        this.image1.setOnTouchListener { v, motionEvent ->
            val orig = (image1.drawable as BitmapDrawable).bitmap
            val imag = Bitmap.createBitmap(orig.width, orig.height, Bitmap.Config.ARGB_8888)
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
            var red = 255
            var green = 255
            var blue = 255
            X.add(x)
            Y.add(y)
            Thread.sleep(1_000)
            println(motionEvent.getAction())
            imag.setPixel(x, y, Color.rgb(red, green, blue))
            image1.setImageBitmap(Bitmap.createScaledBitmap(imag, 1000, 1000, true))
            true
        }
    }


    fun draw_splines()
    {
        for (i in 1..X.size/2){

        }
    }
}
