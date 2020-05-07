package com.healthyfoody.app

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.healthyfoody.app.R
import com.healthyfoody.app.dummy.DummyContent
import com.healthyfoody.app.models.Category
import com.healthyfoody.app.ui.dashboard.DashboardFragment
import com.healthyfoody.app.ui.notifications.NotificationsFragment

class MainActivity : AppCompatActivity() , CategoryFragment.OnListFragmentInteractionListener,CategoryItemsFragment.OnListFragmentInteractionListener{
    private var bottomNav : BottomNavigationView ?=null
    private var categoryFragment : CategoryFragment ?= null
    private var categoryItemsFragment : CategoryItemsFragment ?= null
    //temporales
    private var dashboardFragment : DashboardFragment ?=null
    private var notificationFragment : NotificationsFragment ?=null
    ///////////
    private var mainFrame : FrameLayout ?=null

    private var bundleFragments : Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.nav_view)
        mainFrame = findViewById(R.id.main_frame)

        bundleFragments.putString("title","Categorias")

        categoryFragment = CategoryFragment()
        categoryItemsFragment = CategoryItemsFragment()

        //temporales
        dashboardFragment = DashboardFragment()
        notificationFragment = NotificationsFragment()
        ///////////
        var mainBundle = this.intent.extras

        supportFragmentManager.beginTransaction().replace(R.id.main_frame, categoryFragment!!).commit()
        /*
        * Agregar aqui todos los parametros que se le asignen de la siguiente forma :
        * var id = mainBundle!!.getInt("id")
        * */
        bottomNav?.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment = categoryFragment!!

            when (item.itemId) {
                R.id.navigation_home -> {
                    selectedFragment = categoryFragment!!
                }
                R.id.navigation_dashboard -> {
                    selectedFragment = dashboardFragment!!
                }
                R.id.navigation_notifications -> {
                    selectedFragment = notificationFragment!!
                }
            }

            //supportFragmentManager.beginTransaction().replace(R.id.main_frame, selectedFragment).addToBackStack(txttoolbar!!.text.toString()).commit()
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, selectedFragment).commit()

            true
        }
    }

    override fun onListFragmentInteraction(item: Category?) {
        //Toast.makeText(this,"HOLA COMO ESTAS",Toast.LENGTH_LONG).show()
        /*ejemplo para pasar parametros
        val args : Bundle = Bundle()
        args.putString("categoryId",item!!.name)
        dashboardFragment!!.arguments = args*/
        bundleFragments.putString("title",item!!.name)
        categoryItemsFragment!!.arguments = bundleFragments
        supportFragmentManager.beginTransaction().replace(R.id.main_frame, categoryItemsFragment!!).commit()
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        //Toast.makeText(this,"HOLA COMO ESTAS",Toast.LENGTH_LONG).show()
        //supportFragmentManager.beginTransaction().replace(R.id.main_frame, categoryItemsFragment!!).commit()
    }
}
