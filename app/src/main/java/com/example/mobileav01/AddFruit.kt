package com.example.mobileav01

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_add_fruit.*

class AddFruit : AppCompatActivity() {
    companion object{
        const val MAIN_ACTIVITY_IMAGE_URI_KEY = "MAIN_ACTIVITY_IMAGE_URI_KEY"
    }
    private val pickImage = 100
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fruit)
        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(MAIN_ACTIVITY_IMAGE_URI_KEY)
            imageView2.setImageURI(imageUri)
        }

        uploadBtn.setOnClickListener{
            val gallery = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        concluirBtn.setOnClickListener{
            val i = Intent()
            val fruitName = editTextFruitName.text.toString()
            val benefits = editTextTextMultiLine.text.toString()
            val newFruit = Fruit(imageUri,fruitName, benefits)
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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putParcelable(MAIN_ACTIVITY_IMAGE_URI_KEY, imageUri)
    }
}