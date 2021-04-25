package com.example.mobileav01

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.new_toolbar.*

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
        new_toolbar.title = getString(R.string.main_activity_title)


        fruit_recycler_view.adapter = adapter
        fruit_recycler_view.layoutManager = LinearLayoutManager(this)
        fruit_recycler_view.setHasFixedSize(true)

        fab.setOnClickListener(){
            val fruitIntent = Intent(this, AddFruit::class.java)
            startActivityForResult(fruitIntent, MAIN_ACTIVITY_FRUIT_EXTRA_CODE)
        }

        val optionsCode = booleanArrayOf(false,false,false)
        val options = arrayOf("Remover frutas duplicadas","Ordenar por inserção","Ordenar por ordem alfabética")
        val fruitDialog = AlertDialog.Builder(this)
                .setTitle("Escolha os filtros abaixo")
                .setMultiChoiceItems(options, optionsCode){_: DialogInterface?, i: Int, isChecked: Boolean ->
                    if(isChecked){
                        optionsCode[i] = true
                        Toast.makeText(this, "Opção ${options[i]} marcada", Toast.LENGTH_SHORT).show()
                    }else{
                        optionsCode[i] = false
                        Toast.makeText(this, "Opção ${options[i]} desmarcada", Toast.LENGTH_SHORT).show()
                    }
                }
                .setPositiveButton("Ok"){_, _ ->
                    filterList(optionsCode)
                    Toast.makeText(this, "As alterações foram aplicadas", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Voltar") { _, _ ->
                    Toast.makeText(this, "Não houve nenhuma alteração", Toast.LENGTH_SHORT).show()
                }.create()


        filter_fruit.setOnClickListener {
            fruitDialog.show()
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

//    private fun openDialog(){
//        val choice = booleanArrayOf(false,false,false)
//        val options = arrayOf("Frutas com mesmo nome","Ordem de inserção","Ordem alfabética")
//        val fruitDialog = AlertDialog.Builder(this)
//                .setTitle("Escolha os filtros abaixo")
//                .setMultiChoiceItems(options, choice){_: DialogInterface?, i: Int, isChecked: Boolean ->
//                    if(isChecked){
//                        choice[i] = true
//                        Toast.makeText(this, "Opção ${options[i]} marcada", Toast.LENGTH_SHORT).show()
//                    }else{
//                        choice[i] = false
//                        Toast.makeText(this, "Opção ${options[i]} desmarcada", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                .setPositiveButton("Ok"){_, _ ->
//                    filterList(choice)
//                    Toast.makeText(this, "As alterações foram aplicadas", Toast.LENGTH_SHORT).show()
//                }
//                .setNegativeButton("Voltar") { _, _ ->
//                    Toast.makeText(this, "Não houve nenhuma alteração", Toast.LENGTH_SHORT).show()
//                }.create()
//
//        fruitDialog.show()
//    }

    private fun filterList(optionsCode: BooleanArray){
        if(optionsCode[0]){
            val newFruitList = fruitList.distinctBy { it.fruitName } as ArrayList<Fruit>
//            Log.i("INFO", newFruitList.toString())
            adapter = FruitAdapter(newFruitList, this)
            fruit_recycler_view.swapAdapter(adapter, true)
        }

        else if(optionsCode[1])
        {
            val invertFruitList = fruitList.reversed() as ArrayList<Fruit>
            adapter = FruitAdapter(invertFruitList, this)
            fruit_recycler_view.swapAdapter(adapter, true)
        }
        else if(optionsCode[2])
        {

            var sortedFruitList = fruitList
            sortedFruitList.sortBy { it.fruitName }
            adapter = FruitAdapter(sortedFruitList, this)
            fruit_recycler_view.swapAdapter(adapter, true)
        }
        else
        {
            Log.i("INFO", fruitList.toString())
            adapter = FruitAdapter(fruitList, this)
            fruit_recycler_view.swapAdapter(adapter, true)
        }

    }

    private fun insertItem(view: View?, fruit: Fruit){
        val index = 0
        fruitList.add(index, fruit)
        adapter.notifyItemInserted(index)
        fruit_recycler_view.scrollToPosition(0)
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
        for (i in (size-1) downTo  0) {
            val item = Fruit(R.drawable.ic_bananas, null, "Banana $i", "Boa antes de praticar esportes")
            val item1 = Fruit(R.drawable.ic_bananas, null, "Banana $i", "Boa antes de praticar esportes")
            list += item
            list += item1
        }
        return list
    }
}