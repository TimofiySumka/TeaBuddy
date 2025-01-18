package com.example.teabuddy.Teas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teabuddy.R

class TeaAdapter(private val teasList: ArrayList<TeaModel>) : RecyclerView.Adapter<TeaAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tea_item_rv, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = teasList[position]
        holder.titleImage.setImageResource(currentItem.image)
        holder.titleText.text = currentItem.name
        holder.titleType.text = currentItem.type
    }


    override fun getItemCount(): Int {
        return teasList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleImage: ImageView = itemView.findViewById(R.id.circle_view2)
        val titleText: TextView = itemView.findViewById(R.id.tea_name_tv)
        val titleType:TextView = itemView.findViewById(R.id.tea_type_tv)
    }
}

