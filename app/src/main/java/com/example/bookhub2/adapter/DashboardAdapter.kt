package com.example.bookhub2.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub2.R
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.bookhub2.booklist.DataOfBook
import com.example.bookhub2.activity.Description1
import com.squareup.picasso.Picasso

class DashboardAdapter(val context: Context, val itemList: MutableList<DataOfBook>)
    : RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>(){
    class DashboardViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val bookName:TextView =view.findViewById(R.id.bookName)
        val bookPrice:TextView =view.findViewById(R.id.bookPrice)
        val authorName:TextView =view.findViewById(R.id.authorName)
        val bookRating:TextView=view.findViewById(R.id.bookRating)
        val bookImg:ImageView =view.findViewById(R.id.book_img)
        val eachItemBlock: LinearLayout =view.findViewById(R.id.eachItemBlock)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val bookNum=itemList[position]
        holder.bookName.text= bookNum.book_name
        holder.authorName.text=bookNum.author_name
        holder.bookPrice.text=bookNum.book_price
        holder.bookRating.text=bookNum.rating
        //holder.bookImg.setImageResource(itemList[position].bookImage)
        Picasso.get().load(bookNum.bookImage).error(R.drawable.ic_profile).
        into(holder.bookImg)

        holder.eachItemBlock.setOnClickListener{
            val intent= Intent(context,Description1::class.java)
            intent.putExtra("book_id",bookNum.book_id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}