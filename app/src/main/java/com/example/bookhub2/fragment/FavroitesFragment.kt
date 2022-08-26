package com.example.bookhub2.fragment


import android.content.Context
import android.nfc.Tag
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.bookhub2.localDatabase.Entity
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bookhub2.adapter.FragmentAdapter
import com.example.bookhub2.R
import com.example.bookhub2.localDatabase.Database
import com.example.bookhub2.localDatabase.Entity1
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.google.android.gms.common.util.CollectionUtils.mutableSetOfWithSize
import java.util.Collections.addAll
import kotlin.concurrent.fixedRateTimer

class FavroitesFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var favroitesAdapter: FragmentAdapter
    lateinit var layoutManager2: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout

    lateinit var emptyfav:RelativeLayout
    var favInfoList:List<Entity1>?= listOf<Entity1>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_favroites,
            container, false
        )
        recyclerView = view.findViewById(R.id.recyclerView)
        progressLayout = view.findViewById(R.id.progressLayout)
        emptyfav=view.findViewById(R.id.emptyfav)
        layoutManager2 = GridLayoutManager(activity as Context, 2)
        favInfoList = RetriveFavourites(activity).execute().get()

        if(favInfoList?.isEmpty() == false &&activity!=null) {
        progressLayout.visibility=View.GONE
        favroitesAdapter = FragmentAdapter(activity as Context, favInfoList )
        recyclerView.layoutManager = layoutManager2
        recyclerView.adapter = favroitesAdapter

      }else if(favInfoList?.isEmpty() == true &&activity!=null){
        recyclerView.visibility=View.GONE
         progressLayout.visibility=View.GONE
            emptyfav.visibility=View.VISIBLE
        }else{
            Toast.makeText(activity, "something went wrong", Toast.LENGTH_SHORT).show()}

        return view
    }

    class RetriveFavourites( val context:Context?)
        : AsyncTask<Void, Void, List<Entity1>>() {

//        val migration_1_2=object: Migration(1,2){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("DROP TABLE favBookInfo")
//            }
//        }
        override fun doInBackground(vararg p0: Void?): List<Entity1> {
            val db = Room.databaseBuilder(
                context!!, Database::class.java, "books-db"
            ).build()
            var book:MutableList<Entity1>
            =ArrayList()
            book.addAll(db.bookDao().getAllBooks())
            db.close()
            return book
        }

        override fun onPostExecute(result: List<Entity1>) {
            super.onPostExecute(result)
            //Toast.makeText(context, "showing successfully", Toast.LENGTH_SHORT).show()
        }
    }
}