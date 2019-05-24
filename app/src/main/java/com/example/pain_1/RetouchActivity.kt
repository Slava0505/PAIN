package com.example.pain_1

import android.os.Bundle
import android.graphics.Bitmap
import android.view.MotionEvent
import android.media.Image
import android.graphics.Color
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import codestart.info.kotlinphoto.R
import kotlinx.android.synthetic.main.activity_filter.*


import kotlinx.android.synthetic.main.activity_retouch.*
import kotlinx.android.synthetic.main.activity_retouch.view.*
import java.io.File
import java.io.FileOutputStream

class RetouchActivity : AppCompatActivity() {


    var radiusRetouch: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retouch)
        var cururi = intent.data
        retouchImage.setImageURI(cururi)
        fRetouch()
    }


    fun fRetouch(){
        val originalImage = (retouchImage.drawable as BitmapDrawable).bitmap
        var tempImage = originalImage.copy(originalImage.getConfig(), true) //Bitmap.createBitmap(originalImage.width, originalImage.height, Bitmap.Config.ARGB_8888)
        var radiusRetouch1 : Int = radiusRetouchValue.text.toString().toInt()


        this.retouchImage.setOnTouchListener(object: View.OnTouchListener {
            override  fun  onTouch(v:View, event: MotionEvent):Boolean {

                if (event.getAction() === MotionEvent.ACTION_MOVE){
                    var x = event.x
                    var y = event.y
                    val mSize = 2 * radiusRetouch1 + 1
                    val width = tempImage!!.width
                    val height = tempImage!!.height
                    var pixels = IntArray(width * height)
                    tempImage!!.getPixels(pixels, 0, width, 0, 0, width, height)
                    var sumR = 0
                    var sumG = 0
                    var sumB = 0
                    var count = 0
                    val weights = Array<DoubleArray>(mSize, {DoubleArray(mSize)})
                    for (i in -radiusRetouch1 until radiusRetouch1)// считаем коэффициент и количество пикселей в выбранном радиусе
                    {
                        for (j in -radiusRetouch1 until radiusRetouch1)
                        {
                            val dX = i.toDouble()
                            val dY = j.toDouble()
                            val dR = radiusRetouch1.toDouble()
                            weights[i + radiusRetouch1][j + radiusRetouch1] = (Math.pow(Math.E, -((dX * dX + dY * dY) / (2.0 * dR * dR)))) * 0.25
                        }
                    }
                    val s1= Math.max(0, Math.min(width, (x - radiusRetouch1).toInt() - retouchImage.width / 2 + width / 2))
                    val f1= Math.max(0, Math.min(width, (x + radiusRetouch1).toInt() - retouchImage.width / 2 + width / 2))
                    val s2= Math.max(0, Math.min(height, (y - radiusRetouch1).toInt() - retouchImage.height / 2 + height / 2))
                    val f2= Math.max(0, Math.min(height, (y + radiusRetouch1).toInt() - retouchImage.height / 2 + height / 2))
                    for (i in s1 until f1) {

                        var smth = Math.sqrt((radiusRetouch1*radiusRetouch1 - (i - (x - retouchImage.width / 2 + width / 2 ))*(i - (x - retouchImage.width / 2 + width / 2 ))).toDouble())
                        var newY = y  - retouchImage.height / 2 + height / 2
                        for (j in Math.max(s2, Math.min(f2, (newY - smth).toInt()))
                                until Math.max(s2, Math.min(f2, (newY + smth).toInt()))) {
                            sumR += Color.red(pixels[j * width + i])
                            sumG += Color.green(pixels[j * width + i])   // считаем сумму для цветов
                            sumB += Color.blue(pixels[j * width + i])
                            count++
                        }
                    }
                    sumR /= count
                    sumG /= count  //посчитали усредненный цвет
                    sumB /= count
                    for (i in s1 until f1) {
                        var smth = Math.sqrt((radiusRetouch1 * radiusRetouch1 - (i - (x - retouchImage.width / 2 + width / 2 )) * (i - (x - retouchImage.width / 2 + width / 2 ))).toDouble())
                        var newY = y  - retouchImage.height / 2 + height / 2
                        for (j in Math.max(s2, Math.min(f2, (newY - smth).toInt()))
                                until Math.max(s2, Math.min(f2, (newY + smth).toInt()))) {
                            var  r = (sumR * weights[i - s1][j - s2]).toInt() + (Color.red(pixels[j * width + i]) * (1 - weights[i - s1][j - s2]))
                            var  g = (sumG * weights[i - s1][j - s2]).toInt() + (Color.green(pixels[j * width + i]) * (1 - weights[i - s1][j - s2]))
                            var  b = (sumB * weights[i - s1][j - s2]).toInt() + (Color.blue(pixels[j * width + i]) * (1 - weights[i - s1][j - s2]))
                            r = Math.max(0.0, Math.min(255.0, r))
                            g = Math.max(0.0, Math.min(255.0, g))
                            b = Math.max(0.0, Math.min(255.0, b))
                            pixels[j * width + i] = 255 shl 24 or (r.toInt() shl 16) or (g.toInt() shl 8) or b.toInt()
                        }
                    }
                    tempImage = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.RGB_565)
                    retouchImage.setImageBitmap(tempImage)

                }
                return true
    }
            fun imagesave(): Uri
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
                return Uri.fromFile(file)
            }

})}}
