package com.example.pain_1

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import codestart.info.kotlinphoto.R.layout.*
import kotlinx.android.synthetic.main.activity_unsarp_masking.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class MaskingActivity : AppCompatActivity() {

    var tempImage : Bitmap? = null
    val pi = 3.1415926535

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_unsarp_masking)
        var cururi = intent.data
        unsharpMaskingImage.setImageURI(cururi)
        button_accept.setOnClickListener {
            cururi=bitmapToFile((unsharpMaskingImage.drawable as BitmapDrawable).bitmap)
            val accept= Intent(this, EditorActivity::class.java)
            accept.data=cururi
            startActivity(accept)
        }
        button_cancel.setOnClickListener {
            val cancel= Intent(this, EditorActivity::class.java)
            cancel.data=cururi
            startActivity(cancel)
        }
    }
    override fun onStart(){
        super.onStart()
        val originalImage = (unsharpMaskingImage.drawable as BitmapDrawable).bitmap
        var tempImage = originalImage.copy(originalImage.getConfig(), true) //Bitmap.createBitmap(originalImage.width, originalImage.height, Bitmap.Config.ARGB_8888)

        var amountUM : Double = amountText.text.toString().toDouble()
        var trash: Double = trashText.text.toString().toDouble()
        var radius: Int = radiusText.text.toString().toInt()



        doUM.setOnClickListener {
            tempImage = gaussianBlur(tempImage!!, radius, trash, amountUM)
            unsharpMaskingImage.setImageBitmap(tempImage)
        }


    }
    fun  gaussianBlur(src: Bitmap, radius: Int,threshold:Double,amount:Double): Bitmap  {

        val SIZE = 2 * radius + 1
        var sum = 0.0
        var pixels = IntArray(src!!.width * src!!.height)
        val weights = Array(SIZE) { DoubleArray(SIZE) } // матрица коэффициентов(весов)
        val width = src.width
        val height = src.height
        src.getPixels(pixels, 0, width, 0, 0, width, height)
        var sumR: Double
        var sumG: Double  // переменные для вычисления суммы цвета
        var sumB: Double
        var x1 = 0
        var y1 = 0
        val result = Bitmap.createBitmap(width, height, src.config)

        for (x in -radius until radius) {
            for (y in -radius until radius) {
                weights[x1][y1] = (Math.pow (Math.E, (-((x * x + y * y) / (2 * radius * radius))).toDouble())) / (2 * pi * radius * radius)
                sum += weights[x1][y1]
                y1++
            }
            y1 = 0
            x1++
        }

        if (sum == 0.0)  // чтобы избежать деления на 0
            sum = 1.0

        for (y in 0 until height - SIZE + 1) {
            for (x in 0 until width - SIZE + 1) {
                sumB = 0.0
                sumG = 0.0
                sumR = 0.0

                for (i in 0 until SIZE) {
                    for (j in 0 until SIZE) {
                        sumR += ((Color.red(pixels[x+i+width*(y+j)]) * weights[i][j]))
                        sumG += ((Color.green(pixels[x+i+width*(y+j)]) * weights[i][j]))  // считаем сумму для цветов
                        sumB += ((Color.blue(pixels[x+i+width*(y+j)]) * weights[i][j]))
                    }
                }

                // получаем итоговые цвета
                var red = Math.max(0, Math.min(255, (sumR / sum).toInt()))
                var green = Math.max(0, Math.min(255, (sumG / sum).toInt()))
                var blue = Math.max(0, Math.min(255, (sumB / sum).toInt()))

                var diff = (red - Color.red(pixels[(x+1)+(y+1)*width]) + green - Color.green(pixels[(x+1)+(y+1)*width]) + blue - Color.blue(pixels[(x+1)+(y+1)*width])) / 3

                if (Math.abs(2 * diff) > threshold) {
                    red = Color.red(pixels[(x + 1) + (y + 1) * width]) + (diff * amount).toInt()
                    green = Color.green(pixels[(x + 1) + (y + 1) * width]) + (diff * amount).toInt()
                    blue = Color.blue(pixels[(x + 1) + (y + 1) * width]) + (diff * amount).toInt()
                    red = Math.max(0, Math.min(255, red))
                    green = Math.max(0, Math.min(255, green))
                    blue = Math.max(0, Math.min(255, blue))
                }
                result.setPixel(x + 1, y + 1, Color.argb(255, red, green, blue))
            }
        }
        return result

    }
    fun bitmapToFile(bitmap:Bitmap): Uri {
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
