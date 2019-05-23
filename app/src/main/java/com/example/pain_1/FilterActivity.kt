package com.example.pain_1

import android.content.Context
import android.content.ContextWrapper
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
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        var cururi=intent.data
        buttonapply.setOnClickListener {
            val c=filterselect.selectedItemPosition
            if (c==0)
            {
                processingBar.visibility= View.VISIBLE
                processingBar.progress=0
                imagecsfilter()
                processingBar.visibility= View.INVISIBLE
            }
            if (c==1)
            {
                processingBar.visibility= View.VISIBLE
                processingBar.progress=0
                imagenegfilter()
                processingBar.visibility= View.INVISIBLE
            }
                if (c==2)
            {
                processingBar.visibility= View.VISIBLE
                processingBar.progress=0
                imagecirclarfilter()
                processingBar.visibility= View.INVISIBLE
            }
        }
        Filt_button_accept.setOnClickListener {
            cururi=bitmapToFile((image_cur_view.drawable as BitmapDrawable).bitmap)
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
        processingBar.max=orig.width*orig.height
        val temp = Bitmap.createBitmap(orig.width, orig.height, Bitmap.Config.ARGB_8888)
        var i=0
        while (i<orig.width)
        {
            var t=0
            while (t<orig.height)
            {
                val pxcolor=orig.getPixel(i,t)
                val alpha= Color.alpha(pxcolor)
                var red= Color.red(pxcolor).div(256-normal(this.text_red.text.toString().toInt()))
                var green= Color.green(pxcolor).div(256-normal(this.text_green.text.toString().toInt()))
                var blue= Color.blue(pxcolor).div(256-normal(this.text_blue.text.toString().toInt()))
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
                processingBar.progress++
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
        processingBar.max=orig.width*orig.height
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

                processingBar.progress++

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
        processingBar.max=orig.width*orig.height
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

                processingBar.progress++
                t++
            }
            i++
        }
        image_cur_view.setImageBitmap(temp)
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

    fun normal(rawread:Int):Int
    {
        var a=rawread
        if (a<1){
            a=1
        }
        if (a>255){
            a=255
        }
        return a
    }
}

