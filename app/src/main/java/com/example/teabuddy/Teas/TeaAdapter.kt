package com.example.teabuddy.Teas

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.storage.FirebaseStorage
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teabuddy.R
import com.squareup.picasso.Picasso

class TeaAdapter(private val teasList: ArrayList<TeaModel>) : RecyclerView.Adapter<TeaAdapter.MyViewHolder>() {

    private var mListener: onItemClickListener? = null

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tea_item_rv, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = teasList[position]
        holder.titleText.text = currentItem.name
        holder.titleType.text = currentItem.type

        val imageUrl = currentItem.image
        if (imageUrl.isNotEmpty()) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.timeicon)
                .error(R.drawable.about_icon)
                .into(holder.titleImage)
        } else {
            holder.titleImage.setImageResource(R.drawable.about_icon)
        }
    }


    private var fullTeaList: ArrayList<TeaModel> = ArrayList(teasList)
    fun filter(query: String): Boolean {
        teasList.clear()
        if (query.isEmpty()) {
            teasList.addAll(fullTeaList)
        } else {
            val filteredList = fullTeaList.filter {
                it.name.contains(query, ignoreCase = true)
            }
            teasList.addAll(filteredList)
        }
        notifyDataSetChanged()
        return teasList.isNotEmpty()
    }

    fun updateFullTeaList(newList: ArrayList<TeaModel>) {
        fullTeaList.clear()
        fullTeaList.addAll(newList)
    }

    override fun getItemCount(): Int {
        return teasList.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        val titleImage: ImageView = itemView.findViewById(R.id.circle_view2)
        val titleText: TextView = itemView.findViewById(R.id.tea_name_tv)
        val titleType: TextView = itemView.findViewById(R.id.tea_type_tv)

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }
}
