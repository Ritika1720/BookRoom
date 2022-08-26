package com.example.bookhub2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookhub2.R
import com.example.bookhub2.fragment.AboutUsFragment
import com.example.bookhub2.fragment.DashboardFragment
import com.example.bookhub2.fragment.FavroitesFragment
import com.example.bookhub2.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    lateinit var drawer: DrawerLayout
    lateinit var coordinate: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigation: NavigationView
    var previousMenuItem:MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawer=findViewById(R.id.drawerLayout)
        coordinate=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frame)
        navigation=findViewById(R.id.navigation)
        setuptoolbar()
        openDashboard()
        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,
            drawer, R.string.open_drawer, R.string.close_drawer
        )
        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigation.setNavigationItemSelectedListener {
            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when (it.itemId){
               R.id.dashboard ->{
                   openDashboard()
                   drawer.closeDrawers()
               }
                R.id.aboutApp ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutUsFragment())
                        .commit()
                    supportActionBar?.title="ABOUT US"
                    drawer.closeDrawers()
                    }
                R.id.profile ->{

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())
                        .commit()
                    supportActionBar?.title="PROFILE"
                    drawer.closeDrawers()
                    }
                R.id.favorites ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavroitesFragment())
                        .commit()
                    supportActionBar?.title="FAVORITES"
                    drawer.closeDrawers()}
            }
            return@setNavigationItemSelectedListener true
        }

    }
    fun setuptoolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="BOOK ROOM"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if (id==android.R.id.home){
            drawer.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item) }

    fun openDashboard(){
        val fragement= DashboardFragment()
        val transaction= supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragement)
            .commit()
        supportActionBar?.title="DASHBOARD"
        navigation.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag= supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is DashboardFragment ->openDashboard()
            else-> super.onBackPressed()
        }
    }

}