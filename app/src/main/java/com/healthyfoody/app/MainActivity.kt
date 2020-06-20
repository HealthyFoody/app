package com.healthyfoody.app

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.healthyfoody.app.address.AddressFragment
import com.healthyfoody.app.cart.CartFragment
import com.healthyfoody.app.catalog.CategoryFragment
import com.healthyfoody.app.catalog.CategoryItemsFragment
import com.healthyfoody.app.common.CONSTANTS
import com.healthyfoody.app.common.SharedPreferences
import com.healthyfoody.app.dummy.DummyContent
import com.healthyfoody.app.models.Address
import com.healthyfoody.app.models.Cart
import com.healthyfoody.app.models.Category
import com.healthyfoody.app.models.MainUserValues
import com.healthyfoody.app.ui.notifications.NotificationsFragment

class MainActivity : AppCompatActivity() , CategoryFragment.OnListFragmentInteractionListener,
    CategoryItemsFragment.OnListFragmentInteractionListener,
    AddressFragment.OnListFragmentInteractionListener,CartFragment.OnListFragmentInteractionListener{
    private var bottomNav : BottomNavigationView ?=null
    private var categoryFragment : CategoryFragment?= null
    private var categoryItemsFragment : CategoryItemsFragment?= null
    private var cartFragment : CartFragment ?= null
    private var selectedFragment: Fragment ?= null
    private lateinit var sharedPreferences : SharedPreferences
    //temporales
    private var addressFragment : AddressFragment?=null
    private var notificationFragment : NotificationsFragment ?=null
    ///////////
    private var mainFrame : FrameLayout ?=null

    private var bundleFragments : Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.nav_view)
        mainFrame = findViewById(R.id.main_frame)
        sharedPreferences = SharedPreferences(this)
        bundleFragments.putString("title", "Categorias")

        categoryFragment = CategoryFragment()
        categoryItemsFragment = CategoryItemsFragment()
        cartFragment = CartFragment()
        addressFragment = AddressFragment()
        var mainBundle = this.intent.extras
        if (mainBundle != null){
            val token = mainBundle.getString("token")
            if (token != null && !token.isEmpty()){
                Log.d("TOKEN",token)
                val payload = token.split('.')[1]
                Log.d("TOKEN",payload)
                val userInfo = String(Base64.decode(payload,Base64.URL_SAFE),Charsets.UTF_8)
                Log.d("TOKEN 2",userInfo)
                //Toast.makeText(this,"TOKEN",Toast.LENGTH_LONG).show()
                val gson = Gson()
                val json = gson.fromJson(userInfo,MainUserValues::class.java)
                json!!.token = "Bearer " + token
                Log.d("TOKEN ",gson.toJson(json))
                sharedPreferences.save(CONSTANTS.MAIN_INFO,gson.toJson(json))
            }
        }
        if(savedInstanceState != null){
            Toast.makeText(this,savedInstanceState.getString("userId"),Toast.LENGTH_LONG).show()
        }

        if(selectedFragment == null)
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, categoryFragment!!).commit() // fragment default
        else
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, selectedFragment!!).commit()
        /*
        * Agregar aqui todos los parametros que se le asignen de la siguiente forma :
        * var id = mainBundle!!.getInt("id")
        * */
        bottomNav?.setOnNavigationItemSelectedListener { item ->
            selectedFragment = categoryFragment!!

            when (item.itemId) {
                R.id.navigation_catalog -> {
                    selectedFragment = categoryFragment!!
                }
                R.id.navigation_address -> {
                    selectedFragment = addressFragment!!
                }
                R.id.navigation_cart -> {
                    selectedFragment = cartFragment!!
                }
            }

            //supportFragmentManager.beginTransaction().replace(R.id.main_frame, selectedFragment).addToBackStack(txttoolbar!!.text.toString()).commit()
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, selectedFragment!!).commit()

            true
        }
    }

    override fun onListFragmentInteraction(item: Category?) {
        /*ejemplo para pasar parametros
        val args : Bundle = Bundle()
        args.putString("categoryId",item!!.name)
        dashboardFragment!!.arguments = args*/
        bundleFragments.putString("title",item!!.name)
        bundleFragments.putString("categoryId",item.id)
        categoryItemsFragment!!.arguments = bundleFragments
        supportFragmentManager.beginTransaction().replace(R.id.main_frame, categoryItemsFragment!!).commit()
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {

    }

    override fun onListFragmentInteraction(item: Address?) {
        addressFragment!!.deleteAddress(item!!)
    }

    override fun onListFragmentInteraction(item: Cart?) {
        Toast.makeText(this,"Carrito ",Toast.LENGTH_LONG).show()
    }
}
