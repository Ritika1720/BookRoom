package com.example.bookhub2.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub2.R
import com.example.bookhub2.fragment.DashboardFragment
import com.example.bookhub2.localDatabase.Database
import com.example.bookhub2.localDatabase.Entity
import com.example.bookhub2.localDatabase.Entity1
import com.example.bookhub2.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class Description1 : AppCompatActivity() {
    lateinit var bookName: TextView
    lateinit var bookAuthor: TextView
    lateinit var bookRating: TextView
    lateinit var bookImg: ImageView
    lateinit var bookPrice: TextView
    lateinit var fav: Button
    lateinit var description: TextView
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    var bookId:String?="100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description1)
        bookAuthor = findViewById(R.id.authorName)
        bookImg = findViewById(R.id.bookImg)
        bookPrice = findViewById(R.id.bookPrice)
        bookName = findViewById(R.id.bookName)
        bookRating = findViewById(R.id.bookRating)
        fav = findViewById(R.id.fav)
        description = findViewById(R.id.description)
        toolbar=findViewById(R.id.toolbar)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            Toast.makeText(this, "some unexpected error occurred", Toast.LENGTH_SHORT).show()
        }
        if (bookId == "100") {
            Toast.makeText(this, "some error occurred", Toast.LENGTH_SHORT).show()
            finish()
        }
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParam = JSONObject()
        jsonParam.put("book_id", bookId)
        if (ConnectionManager().checkConnection(this)) {

            val jsonRequest = object : JsonObjectRequest(
                Method.POST, url, jsonParam,
                Response.Listener {
                    try {

                        val success = it.getBoolean("success")
                        if (success) {
                            progressLayout.visibility = View.GONE
                            fav.visibility= View.VISIBLE
                            val data = it.getJSONObject("book_data")
                            val imgURL=data.getString("image")
                            Picasso.get().load(data.getString("image"))
                                .error(R.drawable.ic_profile).into(bookImg)
                            bookName.text = data.getString("name")
                            bookAuthor.text = data.getString("author")
                            bookPrice.text = data.getString("price")
                            bookRating.text = data.getString("rating")
                            description.text = data.getString("description")

                            val favBookInfo= Entity1(
                                bookId.toString(),
                                bookName.text.toString(),
                                bookAuthor.text.toString(),
                                bookPrice.text.toString(),
                                bookRating.text.toString(),
                                imgURL.toString(),
                                description.text.toString()
                            )
                            val checkfav=DBAsyncTask(applicationContext,1,favBookInfo).execute()
                            val isfav=checkfav.get()
                            if(isfav){
                                fav.text="Remove From Favourties"
                                val favcolor= ContextCompat.getColor(applicationContext,R.color.teal_700)
                                fav.setBackgroundColor(favcolor)

                            }else{
                                fav.text="Add To Favourties"
                                val favcolor= ContextCompat.getColor(applicationContext,R.color.topColor)
                                fav.setBackgroundColor(favcolor)
                            }
                            fav.setOnClickListener{
                                if(!DBAsyncTask(applicationContext,1,favBookInfo).execute().get()){

                                    val async=DBAsyncTask(applicationContext,2,favBookInfo).execute()
                                    val result=async.get()

                                    if(result){
                                        Toast.makeText(this, "successfully added", Toast.LENGTH_SHORT).show()
                                        fav.text="Remove From Favourties"
                                        val favcolor= ContextCompat.getColor(applicationContext,R.color.teal_700)
                                        fav.setBackgroundColor(favcolor)
                                    }else{
                                        Toast.makeText(this, "some error occurred", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                else{
                                    val async=DBAsyncTask(applicationContext,3,favBookInfo).execute()
                                    val result=async.get()
                                    println("removing.......")
                                    if(result){
                                        Toast.makeText(this, "successfully removed", Toast.LENGTH_SHORT).show()
                                        fav.text="Add To Favourites"
                                        val favcolor= ContextCompat.getColor(applicationContext,R.color.topColor)
                                        fav.setBackgroundColor(favcolor)
                                    }else{
                                        Toast.makeText(this, "some error occurred", Toast.LENGTH_SHORT).show()
                                    }

                                }}

                        } else {
                            Toast.makeText(this, "some error occurred", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(this, "some exception occurred $e", Toast.LENGTH_SHORT).show()
                    }
                },

                Response.ErrorListener {
                        Toast.makeText(this, "volley error $it", Toast.LENGTH_SHORT).show()
                   })

            { override fun getHeaders(): MutableMap<String, String> {
                val headers= HashMap<String,String>()
                headers.put("Content-type","application/json")
                headers.put("token","a495924c9fd21d")
                return headers }
            }
            queue.add(jsonRequest)
        } else {
            val dialogBox= AlertDialog.Builder(this)

            dialogBox.setMessage("No Internet Connection")
            dialogBox.setCancelable(false)
            dialogBox.setPositiveButton("Connect to the internet"){
                    text,
                    listener->
                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                this.finish()
            }
            dialogBox.setNegativeButton("Cancel") {
                    text,
                    listener ->
                ActivityCompat.finishAffinity(this)
            }
            dialogBox.create()
            dialogBox.show()
        }
        setSupportActionBar(toolbar)
        supportActionBar?.title="DESCRIPTION"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
    class DBAsyncTask(val context: Context?, val mode:Int, val bookEntity: Entity1)
        : AsyncTask<Void, Void, Boolean>(){
        /*
        mode 1- check DB if the book is favourite or not
        mode 2- save the book into db as favourite
        mode3- remove the favourite book
         */
        val migration_1_2=object: Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE favBookInfo")
            }
        }
        val migration2_3=object :Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS 'profileInfo'('id' INTEGER NOT NULL,'username' TEXT,'imageURL' TEXT,PRIMARY KEY('id') )  ")
            }

        }
        val db= Room.databaseBuilder(context!!, Database::class.java,"books-db")
            .addMigrations(migration_1_2,migration2_3).build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1->{
                    val book: Entity1?=db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book!=null
                }
                2->{
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3->{
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }

            }
            return false
        }

    }

}