package com.example.mobileav01

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FruitAdapter.OnItemClickListener {

    companion object{

        const val MAIN_ACTIVITY_FRUIT_EXTRA_CODE = 1
        const val MAIN_ACTIVITY_FRUIT_DETAIL_CODE = 2
        const val MAIN_ACTIVITY_FRUIT_EXTRA_PARCELABLE = "MAIN_ACTIVITY_FRUIT_EXTRA_PARCELABLE"
        const val MAIN_ACTIVITY_FRUIT_DETAIL_PARCELABLE = "MAIN_ACTIVITY_FRUIT_DETAIL_PARCELABLE"
        const val MAIN_ACTIVITY_FRUIT_DETAIL_POSITION = "MAIN_ACTIVITY_FRUIT_DETAIL_POSITION"
        const val MAIN_ACTIVITY_FRUIT_LIST_KEY = "MAIN_ACTIVITY_FRUIT_LIST_KEY"

    }

    private var fruitList = generateList(3)
    private var adapter = FruitAdapter(fruitList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            fruitList = savedInstanceState.getParcelableArrayList<Fruit>(MAIN_ACTIVITY_FRUIT_LIST_KEY) as ArrayList<Fruit>
            adapter = FruitAdapter(fruitList, this)
        }
        fruit_recycler_view.adapter = adapter
        fruit_recycler_view.layoutManager = LinearLayoutManager(this)
        fruit_recycler_view.setHasFixedSize(true)

        fab.setOnClickListener(){
            val fruitIntent = Intent(this, AddFruit::class.java)
            startActivityForResult(fruitIntent, MAIN_ACTIVITY_FRUIT_EXTRA_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK) {
            return
        }

        if(requestCode == MAIN_ACTIVITY_FRUIT_EXTRA_CODE) {
            val fruitResult :Fruit? = data?.getParcelableExtra(MAIN_ACTIVITY_FRUIT_EXTRA_PARCELABLE)
            if(fruitResult != null){
                insertItem(null, fruitResult)
            }
        }

        if(requestCode == MAIN_ACTIVITY_FRUIT_DETAIL_CODE){
            val position: Int? = data?.getIntExtra(MAIN_ACTIVITY_FRUIT_DETAIL_POSITION, 0)
            if(position != null){
                deleteItem(null, position)
            }
        }

    }


    private fun insertItem(view: View?, fruit: Fruit){
        val index = 0
        fruitList.add(index, fruit)
        adapter.notifyItemInserted(index)
    }

    private fun deleteItem(view: View?, position: Int){
        fruitList.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    override fun onItemClick(position: Int) {
        var fruitTarget = fruitList[position]
        val detailIntent = Intent(this, DetalhesActivity::class.java)
        detailIntent.putExtra(MAIN_ACTIVITY_FRUIT_DETAIL_POSITION, position)
        detailIntent.putExtra(MAIN_ACTIVITY_FRUIT_DETAIL_PARCELABLE, fruitTarget)
        startActivityForResult(detailIntent, MAIN_ACTIVITY_FRUIT_DETAIL_CODE)
    }

    private fun generateList(size: Int): ArrayList<Fruit> {
        val list = ArrayList<Fruit>()
        for (i in 0 until size) {
            val item = Fruit(null, "Exemplo $i", "Benefícios")
            list += item
        }
        return list
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putParcelableArrayList(MAIN_ACTIVITY_FRUIT_LIST_KEY, fruitList)
    }
}