package com.example.mobileav01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detalhes.*

class DetalhesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes)

        val position = intent.getIntExtra(MainActivity.MAIN_ACTIVITY_FRUIT_DETAIL_POSITION,0)
        val fruit = intent.getParcelableExtra<Fruit?>(MainActivity.MAIN_ACTIVITY_FRUIT_DETAIL_PARCELABLE)

        textView.text = fruit?.fruitName
        textView3.text = fruit?.fruitBenefits
        if(fruit?.imageResource != null){
            imageView.setImageURI(fruit?.imageResource)
        }else{
            imageView.setImageResource(R.drawable.ic_image)
        }



        deleteBtn.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(MainActivity.MAIN_ACTIVITY_FRUIT_DETAIL_POSITION, position)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}