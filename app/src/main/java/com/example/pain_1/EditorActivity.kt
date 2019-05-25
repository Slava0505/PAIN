package com.example.pain_1

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import codestart.info.kotlinphoto.R
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.widget.Toast
import codestart.info.kotlinphoto.AppConstants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class EditorActivity : AppCompatActivity() {
    var fileUri: Uri? = null
    companion object {

        const val TOTAL_COUNT = "total_count"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        showStartPhoto()
        filters_button.setOnClickListener {
            val filt= Intent(this, FilterActivity::class.java)
            filt.data= this.intent.data
            startActivity(filt)
        }
        segmintation_button.setOnClickListener {
            val bilin= Intent(this, BilinearActivity::class.java)
            bilin.data= this.intent.data
            startActivity(bilin)
        }
        line_button.setOnClickListener {
            val splines= Intent(this, SplinesActivity::class.java)
            splines.data= this.intent.data
            startActivity(splines)
        }
        rotation_button.setOnClickListener {
            val rot= Intent(this, RotateActivity::class.java)
            rot.data= this.intent.data
            startActivity(rot)
        }
        resize_button.setOnClickListener {
            val resiz= Intent(this, ResizeActivity::class.java)
            resiz.data= this.intent.data
            startActivity(resiz)
        }
        retouch_button.setOnClickListener {
            val retouch= Intent(this, RetouchActivity::class.java)
            retouch.data= this.intent.data
            startActivity(retouch)
        }
        masking_button.setOnClickListener {
            val masking= Intent(this, MaskingActivity::class.java)
            masking.data= this.intent.data
            startActivity(masking)
        }

//        resize_button.setOnClickListener {
//            val resiz= Intent(this, ResizeActivity::class.java)
//            resiz.data= this.intent.data
//            startActivity(resiz)
//        }
        gallery_button.setOnClickListener {
            pickPhotoFromGallery()
        }
        save.setOnClickListener {
            // Save the image in internal storage and get the uri
            val uri:Uri = saveImageToInternalStorage(R.drawable.lenna)
            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()

        }
    }


    // Method to save an image to internal storage
    private fun saveImageToInternalStorage(drawableId:Int):Uri{
        // Get the image from drawable resource as drawable object
        val drawable = ContextCompat.getDrawable(applicationContext,drawableId)

        // Get the bitmap from drawable object
        val bitmap = (drawable as BitmapDrawable).bitmap

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)


        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return Uri.parse(file.absolutePath)
    }

    fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(pickImageIntent, AppConstants.PICK_PHOTO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.TAKE_PHOTO_REQUEST) {
//photo from camera
//display the photo on the imageview
            fileUri = data?.data
            startEditor(fileUri)

        }else if(resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.PICK_PHOTO_REQUEST){
//photo from gallery
            fileUri = data?.data
            startEditor(fileUri)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    fun startEditor (fileUri: Uri?) {
        val editor = Intent(this, EditorActivity::class.java)
        editor.data = fileUri
        startActivity(editor)
    }





    fun showStartPhoto() {
// Get the count from the intent extras
        val fileUri = intent.getData()
        imageEditorView.setImageURI(fileUri)
    }
}