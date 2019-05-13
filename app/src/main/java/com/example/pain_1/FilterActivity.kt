package com.example.pain_1

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import codestart.info.kotlinphoto.R
import kotlinx.android.synthetic.main.activity_filter.*
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.Color
import android.net.Uri
import android.net.Uri.*
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar.getInstance
import kotlin.math.floor


class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        var cururi=intent.data
        Neg_button.setOnClickListener {
            imagenegfilter()
        }
        CS_button.setOnClickListener {
            imagecsfilter()
        }
        Circlar_button.setOnClickListener {
            imagecirclarfilter()
        }
        Filt_button_accept.setOnClickListener {
            cururi=imagesave()
            val accept= Intent(this, EditorActivity::class.java)
            accept.data=cururi
            startActivity(accept)
        }
        Filt_button_cancel.setOnClickListener {
            val cancel= Intent(this, EditorActivity::class.java)
            cancel.data=cururi
            startActivity(cancel)
        }
        val imageUri = cururi
        this.image_cur_view.setImageURI(imageUri)
    }

    fun imagecsfilter()
    {
        val orig = (image_cur_view.drawable as BitmapDrawable).bitmap
        val temp = Bitmap.createBitmap(orig.width, orig.height, Bitmap.Config.ARGB_8888)
        var i=0
        while (i<orig.width)
        {
            var t=0
            while (t<orig.height)
            {
                val pxcolor=orig.getPixel(i,t)
                val alpha= Color.alpha(pxcolor)
                var red=floor(Color.red(pxcolor)*255.0/normal(this.text_red.text.toString().toInt())).toInt()
                var green=floor(Color.green(pxcolor)*255.0/normal(this.text_green.text.toString().toInt())).toInt()
                var blue=floor(Color.blue(pxcolor)*255.0/normal(this.text_blue.text.toString().toInt())).toInt()
                if (red>255){
                    red=255
                }
                if (blue>255){
                    blue=255
                }
                if (green>255){
                    green=255
                }
                val newPixel = Color.argb(
                    alpha, red, green,blue
                )
                temp.setPixel(i,t,newPixel)
                t++
            }
            i++
        }
        image_cur_view.setImageBitmap(temp)
    }

    fun imagenegfilter()
    {
        val orig = (image_cur_view.drawable as BitmapDrawable).bitmap
        val temp = Bitmap.createBitmap(orig.width, orig.height, Bitmap.Config.ARGB_8888)
        var i=0
        while (i<orig.width)
        {
            var t=0
            while (t<orig.height)
            {
                val pxcolor=orig.getPixel(i,t)
                val alpha= Color.alpha(pxcolor)
                val red=255-Color.red(pxcolor)
                val green=255-Color.green(pxcolor)
                val blue=255-Color.blue(pxcolor)
                val newPixel = Color.argb(
                    alpha, red, green,blue
                )
                temp.setPixel(i,t,newPixel)
                t++
            }
            i++
        }
        image_cur_view.setImageBitmap(temp)
    }

    fun imagecirclarfilter()
    {
        val orig = (image_cur_view.drawable as BitmapDrawable).bitmap
        val temp = Bitmap.createBitmap(orig.width, orig.height, Bitmap.Config.ARGB_8888)
        var i=0
        while (i<orig.width)
        {
            var t=0
            while (t<orig.height)
            {
                val pxcolor=orig.getPixel(i,t)
                val alpha= Color.alpha(pxcolor)
                val red=Color.red(pxcolor)
                val green=Color.green(pxcolor)
                val blue=Color.blue(pxcolor)
                val newPixel = Color.argb(
                    alpha, blue, red,green
                )
                temp.setPixel(i,t,newPixel)
                t++
            }
            i++
        }
        image_cur_view.setImageBitmap(temp)
    }

    fun imagesave():Uri
    {
        val orig = (image_cur_view.drawable as BitmapDrawable).bitmap
        val filepath = Environment.getExternalStorageDirectory().absolutePath + "/PAINImages"
        val dir = File(filepath)
        if (!dir.exists())
            dir.mkdirs()
        val file = File(dir, "PainImage" + "lol" + ".png")
        val fOut = FileOutputStream(file)
        orig.compress(Bitmap.CompressFormat.PNG, 85, fOut)
        fOut.flush()
        fOut.close()
        return fromFile(file)
    }

    fun normal(rawread:Int):Int
    {
        var a=rawread
        if (a<1){
            a=1
        }/*
        if (a>255){
            a=255
        }*/
        return a
    }
}

