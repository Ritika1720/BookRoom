package com.example.bookhub2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub2.R
import com.example.bookhub2.activity.Description1
import com.example.bookhub2.localDatabase.Entity
import com.example.bookhub2.localDatabase.Entity1
import com.example.bookhub2.util.ConnectionManager
import com.squareup.picasso.Picasso

class FragmentAdapter(val context: Context, val itemList: List<Entity1>?)
    :RecyclerView.Adapter<FragmentAdapter.Fragmentviewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Fragmentviewholder{

        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_single_row,parent,false)


        return Fragmentviewholder(view)
    }

    override fun onBindViewHolder(holder: Fragmentviewholder, position: Int) {

        val bookNum = itemList!!.get(position)
        holder.bookName.text = bookNum?.book_name
        holder.authorName.text = bookNum?.book_author
        holder.bookPrice.text = bookNum?.book_price
        holder.bookRating.text = bookNum?.book_rating
        //holder.bookImg.setImageResource(itemList[position].bookImage)
        Picasso.get().load(bookNum?.book_img).error(R.drawable.ic_profile).into(holder.bookImg)
        holder.eachItemBlock.setOnClickListener {
            if (ConnectionManager().checkConnection(context)) {
                val intent = Intent(context, Description1::class.java)
                intent.putExtra("book_id", bookNum?.book_id.toString())
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun getItemCount(): Int {
        return itemList!!.size
    }
    class Fragmentviewholder(view: View):RecyclerView.ViewHolder(view){

        val bookName: TextView =view.findViewById(R.id.bookName)
        val bookPrice: TextView =view.findViewById(R.id.bookPrice)
        val authorName: TextView =view.findViewById(R.id.authorName)
        val bookRating: TextView =view.findViewById(R.id.bookRating)
        val bookImg: ImageView =view.findViewById(R.id.book_img)
        val eachItemBlock: LinearLayout =view.findViewById(R.id.eachItemBlock)

    }
}