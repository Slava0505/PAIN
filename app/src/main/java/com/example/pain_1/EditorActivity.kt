package com.example.pain_1

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import codestart.info.kotlinphoto.R
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.activity_main.*

class EditorActivity : AppCompatActivity() {
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
    }


    fun showStartPhoto() {
        // Get the count from the intent extras
        val fileUri = intent.getData()
        imageEditorView.setImageURI(fileUri)
    }
}
