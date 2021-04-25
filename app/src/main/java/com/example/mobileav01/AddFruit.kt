package com.example.mobileav01

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_add_fruit.*
import kotlinx.android.synthetic.main.new_toolbar.*

class AddFruit : AppCompatActivity() {
    companion object{
        const val MAIN_ACTIVITY_IMAGE_URI_KEY = "MAIN_ACTIVITY_IMAGE_URI_KEY"
    }
    private val pickImage = 100
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fruit)

        new_toolbar.title = getString(R.string.add_activity_title)

        setSupportActionBar(findViewById(R.id.new_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        uploadBtn.setOnClickListener{
            val gallery = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        concluirBtn.setOnClickListener{
            val i = Intent()
            val fruitName = editTextFruitName.text.toString()
            var benefits = editTextTextMultiLine.text.toString()

            if(benefits.isEmpty()){
                benefits = "No Description"
            }

            val newFruit = Fruit(null,imageUri,fruitName, benefits)
            i.putExtra(MainActivity.MAIN_ACTIVITY_FRUIT_EXTRA_PARCELABLE, newFruit)
            setResult(Activity.RESULT_OK, i)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            val contentResolver = applicationContext.contentResolver
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            imageUri?.let { contentResolver.takePersistableUriPermission(it, takeFlags) }
            imageView2.setImageURI(imageUri)
        }
    }
}