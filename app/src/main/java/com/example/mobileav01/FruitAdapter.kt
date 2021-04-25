package com.example.mobileav01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fruit_card.view.*

class FruitAdapter(private var fruitList: List<Fruit>, private var listener: OnItemClickListener) : RecyclerView.Adapter<FruitAdapter.FruitHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fruit_card, parent,
            false)

        return FruitHolder(itemView)
    }

    override fun onBindViewHolder(holder: FruitHolder, position: Int) {
        val currentItem = fruitList[position]

        if(currentItem.intImg != null){
            holder.imageView.setImageResource(currentItem.intImg!!)
        }
        holder.imageView.setImageURI(currentItem.imageResource)
        holder.fruitTextView.text = currentItem.fruitName
        holder.benefitsTextView.text = currentItem.fruitBenefits
    }

    override fun getItemCount() = fruitList.size

    inner class FruitHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        val imageView: ImageView = itemView.image_view
        val fruitTextView: TextView = itemView.text_view_fruit_name
        val benefitsTextView: TextView = itemView.text_view_fruit_benefits

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}