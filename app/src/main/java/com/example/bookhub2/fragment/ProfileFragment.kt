package com.example.bookhub2.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.service.autofill.SaveInfo
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.bookhub2.R
import com.example.bookhub2.activity.loginActivity
import com.example.bookhub2.localDatabase.Database
import com.example.bookhub2.localDatabase.Entity1
import com.example.bookhub2.localDatabase.Entity3
import com.example.bookhub2.util.ConnectionManager
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    lateinit var userNo:TextView
    lateinit var imageView:ShapeableImageView
    lateinit var floatButton:FloatingActionButton
    lateinit var save:Button
    lateinit var editProfile:androidx.constraintlayout.widget.ConstraintLayout
    lateinit var profile:androidx.constraintlayout.widget.ConstraintLayout
    lateinit var userText:EditText
    lateinit var userName:TextView
    lateinit var editPicture:Button
    lateinit var imageDP:ShapeableImageView
    var imageUri: Uri?=null
    lateinit var activity1: Context
    var saveInfo:MutableList<Entity3> =ArrayList()
    var launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode==Activity.RESULT_OK){
            imageUri=it.data?.data!!
            imageView.setImageURI(imageUri)
            imageDP.setImageURI(imageUri)
            Toast.makeText(activity as Context, "image set successfully", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(activity as Context, "ERROR:try again", Toast.LENGTH_SHORT).show()
        }
    }
    var mAuth:FirebaseAuth?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_profile, container, false)
        activity1=container!!.context
        setHasOptionsMenu(true)
        userNo=view.findViewById(R.id.userNumber)
        imageView=view.findViewById(R.id.imageView)
        floatButton=view.findViewById(R.id.floatingActionButton)
        save=view.findViewById(R.id.save)
        editPicture=view.findViewById(R.id.editPicture)
        editProfile=view.findViewById(R.id.editProfile)
        profile=view.findViewById(R.id.profile)
        userText=view.findViewById(R.id.userText)
        userName=view.findViewById(R.id.userName)
        imageDP=view.findViewById(R.id.imageDP)
        mAuth= FirebaseAuth.getInstance()
        userNo.text=mAuth?.currentUser?.phoneNumber.toString()

        val retrive =Retrive(activity as Context).execute().get()
        if (retrive!=null){
            userName.setText(retrive.username)
            userText.setText(retrive.username)
            Picasso.get().load(retrive.imageURL).error(R.drawable.gradient_main).into(imageDP)
            Picasso.get().load(retrive.imageURL).error(R.drawable.gradient_main).into(imageView)
        }

        floatButton.setOnClickListener {
            profile.visibility=View.GONE
            editProfile.visibility=View.VISIBLE
        }
        editPicture.setOnClickListener {

            ImagePicker.with(activity1 as Activity)
                .cropSquare()
                .createIntentFromDialog {launcher.launch(it)  }

        }
        save.setOnClickListener {
            if(ConnectionManager().checkConnection(activity as Context)==true) {
                //showProgress
                if (userText.editableText.isEmpty() == false) {
                    var dailog = Dialog(activity as Context)
                    dailog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    val view1 = inflater.inflate(R.layout.wait, container, false)
                    dailog.setContentView(view1)
                    dailog.setCanceledOnTouchOutside(false)
                    dailog.show()
                    //middle work

                    userName.setText(userText.editableText.toString())
//                        userText.setText(userName.text.toString())
                    var condition:Boolean
                    if (imageUri!=null){
                    val saveInfo1 = Entity3(1,userName.text.toString(),imageUri.toString())
                     condition= SaveInfo(activity as Context,saveInfo1).execute().get()}
                    else{
                        condition= SaveInfo(activity as Context,Entity3(1,userName.text.toString(),imageView.toString())).execute().get()}
                    // after ending of work
                    if (condition){
                        Handler().postDelayed({
                            dailog.dismiss()
                            Toast.makeText(activity, "Successfully Saved", Toast.LENGTH_SHORT)
                                .show()
                            editProfile.visibility = View.GONE
                            profile.visibility = View.VISIBLE
                        }, 2000)
                }}else{
                    Toast.makeText(activity, "BLOCK is empty", Toast.LENGTH_SHORT).show()}
            }
            else{
                Toast.makeText(activity, "CONNECT To Your Internet", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode==Activity.RESULT_OK){
//            imageUri=data?.data!!
//            imageView.setImageURI(imageUri)
//            imageDP.setImageURI(imageUri)
//            Toast.makeText(activity as Context, "image set successfully", Toast.LENGTH_SHORT).show()
//        }else{
//            Toast.makeText(activity as Context, "ERROR:try again", Toast.LENGTH_SHORT).show()
//        }
//    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item.itemId

        if (id==R.id.signOut){
            if(ConnectionManager().checkConnection(activity as Context)){
            mAuth?.signOut()
            val intent=Intent(activity,loginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            activity?.finish()}
            else{
                Toast.makeText(activity, "CONNECT To Your Internet", Toast.LENGTH_SHORT).show()
            }
        }
        if (id==R.id.deleteAccount){
            if(ConnectionManager().checkConnection(activity as Context)){
                val flag=DeleteFavourites(activity).execute().get()
                val flag1=DeleteSaved(activity).execute().get()
            mAuth?.currentUser?.delete()
            mAuth?.signOut()
            val intent=Intent(activity,loginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            activity?.finish()}
            else{
                Toast.makeText(activity, "CONNECT To Your Internet", Toast.LENGTH_SHORT).show()
            }


        }
        return super.onOptionsItemSelected(item)
    }
    class DeleteFavourites( val context:Context?)
        : AsyncTask<Void, Void,Boolean>() {
        val db = Room.databaseBuilder(context!!, Database::class.java, "books-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            val favBooks: MutableList<Entity1> = ArrayList()
            favBooks.addAll(db.bookDao().getAllBooks())
            var i = 0
            while (i < favBooks.size) {
                val book: Entity1? = db.bookDao().getBookById(favBooks[i].book_id.toString())
                    db.bookDao().deleteBook(book!!)
                i++
            }
            db.close()
            return true
        }
    }
    class SaveInfo(val context:Context?,val entity3: Entity3):AsyncTask<Void,Void,Boolean>(){
        val db = Room.databaseBuilder(context!!, Database::class.java, "books-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            val id=db.proDao().getInfoById(entity3.id)
            if(id==null){
                db.proDao().insertProfile(entity3)
                db.close()
            }else{db.proDao().updateProfile(entity3)
            db.close()}
          return true
        }
    }
    class Retrive( val context:Context?)
        : AsyncTask<Void, Void, Entity3?>() {
        override fun doInBackground(vararg p0: Void?): Entity3? {
            val db = Room.databaseBuilder(
                context!!, Database::class.java, "books-db"
            ).build()
            
            val savedData :Entity3? = db.proDao().getProfile()
            db.close()
            return savedData
        }
    
    }
    class DeleteSaved( val context:Context?)
        : AsyncTask<Void, Void,Boolean>() {
        val db = Room.databaseBuilder(context!!, Database::class.java, "books-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
         val toBeDeleted=db.proDao().getProfile()
         db.proDao().deleteProfile(toBeDeleted!!)
         db.close()
         return true
        }
        }

}

