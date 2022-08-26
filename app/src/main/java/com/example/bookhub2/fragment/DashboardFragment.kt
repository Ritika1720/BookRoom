package com.example.bookhub2.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub2.adapter.DashboardAdapter
import com.example.bookhub2.R
import com.example.bookhub2.booklist.DataOfBook
import com.example.bookhub2.util.ConnectionManager
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class DashboardFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var dashboardAdapter:DashboardAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var noConnection:RelativeLayout
    var bookInfoList:MutableList<DataOfBook>
            =ArrayList()
    var ratingComparator=Comparator<DataOfBook>{book1,book2 ->

//        if(book1.rating.compareTo(book2.rating,true)==0){
//            book1.author_name.compareTo(book2.author_name,true)
//        }else{
            book1.rating.compareTo(book2.rating,true)
//        }
    }
    var priceComparator=Comparator<DataOfBook>{book1,book2 ->
        book1.book_price.compareTo(book2.book_price,true)
    }
    var nameComparator=Comparator<DataOfBook>{book1,book2 ->
        book1.book_name.compareTo(book2.book_name,true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_dashboard2, container, false)
        recyclerView=view.findViewById(R.id.recyclerView)
        progressLayout=view.findViewById(R.id.progressLayout)
        noConnection=view.findViewById(R.id.noConnection)
        progressLayout.visibility=View.VISIBLE



        val queue= Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnection(activity as Context)){
            val jsonobject=object: JsonObjectRequest(
                Method.GET,
                url,null, Response.Listener
                {
                    try {
                        progressLayout.visibility=View.GONE
                        setHasOptionsMenu(true)
                        val success=it.getBoolean("success")
                        if (success){
                            val data= it.getJSONArray("data")
                            var i=0
                            while (i <data.length()){
                                val dataObject=data.getJSONObject(i)
                                val bookObject=DataOfBook(
                                    dataObject.getString("book_id"),
                                    dataObject.getString("name"),
                                    dataObject.getString("author"),
                                    dataObject.getString("price"),
                                    dataObject.getString("rating"),
                                    dataObject.getString("image")
                                )
                                bookInfoList.add(bookObject)
                                dashboardAdapter= DashboardAdapter(activity as Context,bookInfoList)
                                layoutManager= LinearLayoutManager(activity)
                                /* recyclerView.addItemDecoration(
                                     DividerItemDecoration(recyclerView.context,
                                         (layoutManager as LinearLayoutManager).orientation)
                                 )*/
                                recyclerView.adapter=dashboardAdapter
                                recyclerView.layoutManager=layoutManager
                                i=i+1
                            }
                        }else{
                            Toast.makeText(activity, "some error occurred", Toast.LENGTH_SHORT).show()
                        }

                    }catch (e:Exception) {
                        if (activity != null) {
                            Toast.makeText(activity, "unable to fetch data", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                }, Response.ErrorListener{
                    if(activity!=null){
                        Toast.makeText(activity, "some error occurred", Toast.LENGTH_SHORT).show()
                    }}){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers.put("Content-type","application/json")
                    headers.put("token","a495924c9fd21d")
                    return headers
                }

            }
            queue.add(jsonobject)
        }
        else{
            progressLayout.visibility=View.GONE
             noConnection.visibility=View.VISIBLE

//            val dialogBox= AlertDialog.Builder(activity as Context)
//            dialogBox.setMessage("No Internet Connection")
//            dialogBox.setCancelable(false)
//            dialogBox.setPositiveButton("Connect to the internet"){
//                    text,
//                    listener->
//                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
//                activity?.finish()
//            }
//            dialogBox.setNegativeButton("Cancel") {
//                    text,
//                    listener ->
//                ActivityCompat.finishAffinity(activity as Activity)
//            }
//            dialogBox.create()
//            dialogBox.show()
        }


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sorting_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId

            if (id == R.id.byRating) {
                Collections.sort(bookInfoList, ratingComparator)
                Collections.reverse(bookInfoList)
                dashboardAdapter.notifyDataSetChanged()
            }
        if (id == R.id.byPriceHL) {
                Collections.sort(bookInfoList, priceComparator)
                Collections.reverse(bookInfoList)
            dashboardAdapter.notifyDataSetChanged()
            }
        if (id == R.id.byPriceLH) {
                Collections.sort(bookInfoList, priceComparator)
            dashboardAdapter.notifyDataSetChanged()
            }
        if (id == R.id.byName) {
                Collections.sort(bookInfoList, nameComparator)
            dashboardAdapter.notifyDataSetChanged()
            }



        return super.onOptionsItemSelected(item)
    }
}