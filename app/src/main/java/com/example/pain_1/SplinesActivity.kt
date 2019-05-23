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
import java.io.File
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList


class SplinesActivity : AppCompatActivity() {
    val X = ArrayList<Int>()
    val Y = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(codestart.info.kotlinphoto.R.layout.activity_splines)
        val cururi=intent.data
        image1.setImageURI(cururi)




        this.image1.setOnTouchListener { v, motionEvent ->
            val start_imag = (image1.drawable as BitmapDrawable).bitmap
            val imag = start_imag.copy(start_imag.getConfig(), true)
            var x=(floor(motionEvent.x)*imag.width/image1.width).toInt()
            var y=(floor(motionEvent.y)*imag.height/image1.height).toInt()
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
            println(X)
            imag.setPixel(x, y, Color.rgb(red, green, blue))



            image1.setImageBitmap(imag)
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


    fun draw_splines()
    {
        for (i in 1..X.size/2){

        }
    }
}
