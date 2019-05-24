package com.example.pain_1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import codestart.info.kotlinphoto.AppConstants
import codestart.info.kotlinphoto.R
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.activity_main.*

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
        //listen to gallery button click
        gallery_button.setOnClickListener {
            pickPhotoFromGallery()
        }
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
