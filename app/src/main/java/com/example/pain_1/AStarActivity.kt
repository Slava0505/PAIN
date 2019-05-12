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
        clear()
        space.setImageBitmap(imag)
        buttonGenerate.setOnClickListener {
            sw=normal(text_wight.text.toString().toInt())
            sh=normal(text_height.text.toString().toInt())
            imag = Bitmap.createBitmap(sw, sh, Bitmap.Config.ARGB_8888)
            clear()
            space.setImageBitmap(imag)
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
