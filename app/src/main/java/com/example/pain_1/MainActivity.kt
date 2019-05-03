package codestart.info.kotlinphoto

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.pain_1.AStarActivity
import com.example.pain_1.EditorActivity
import kotlinx.android.synthetic.main.activity_editor.imageEditorView
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //listen to gallery button click
        gallery.setOnClickListener {
            pickPhotoFromGallery()
        }

        //listen to take photo button click
        takePhoto.setOnClickListener {
            askCameraPermission()
        }
        //listen to A* button click
        aStar.setOnClickListener {
            val a= Intent(this, AStarActivity::class.java)
            startActivity(a)
        }
        //listen to Editor button click
        editor.setOnClickListener {
            val editor = Intent(this, EditorActivity::class.java)
            startActivity(editor)
        }
    }
    //pick a photo from gallery
    private fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(pickImageIntent, AppConstants.PICK_PHOTO_REQUEST)
    }




    private fun askCameraPermission() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent,  AppConstants.TAKE_PHOTO_REQUEST)
            }
        }
    }


    //override function that is called once the photo has been taken
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.TAKE_PHOTO_REQUEST) {
            //photo from camera
            //display the photo on the imageview
            imageView.setImageURI(fileUri)
        }else if(resultCode == Activity.RESULT_OK
            && requestCode == AppConstants.PICK_PHOTO_REQUEST){
            //photo from gallery
            fileUri = data?.data
            startEditor(fileUri)
            imageView.setImageURI(fileUri)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    fun startEditor (fileUri: Uri?) {
        val editor = Intent(this, EditorActivity::class.java)
        editor.setData(fileUri)
        startActivity(editor)
    }

}