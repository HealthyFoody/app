package com.healthyfoody.app

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.healthyfoody.app.address.AddressFragment
import com.healthyfoody.app.cart.CartFragment
import com.healthyfoody.app.catalog.CategoryFragment
import com.healthyfoody.app.catalog.CategoryItemsFragment
import com.healthyfoody.app.dummy.DummyContent
import com.healthyfoody.app.models.Address
import com.healthyfoody.app.models.Cart
import com.healthyfoody.app.models.Category
import com.healthyfoody.app.ui.notifications.NotificationsFragment

class MainActivity : AppCompatActivity() , CategoryFragment.OnListFragmentInteractionListener,
    CategoryItemsFragment.OnListFragmentInteractionListener,
    AddressFragment.OnListFragmentInteractionListener,CartFragment.OnListFragmentInteractionListener{
    private var bottomNav : BottomNavigationView ?=null
    private var categoryFragment : CategoryFragment?= null
    private var categoryItemsFragment : CategoryItemsFragment?= null
    private var cartFragment : CartFragment ?= null
    private var selectedFragment: Fragment ?= null
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

        bundleFragments.putString("title", "Categorias")

        categoryFragment = CategoryFragment()
        categoryItemsFragment = CategoryItemsFragment()
        cartFragment = CartFragment()
        addressFragment = AddressFragment()

        var mainBundle = this.intent.extras
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
        categoryItemsFragment!!.arguments = bundleFragments
        supportFragmentManager.beginTransaction().replace(R.id.main_frame, categoryItemsFragment!!).commit()
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {

    }

    override fun onListFragmentInteraction(item: Address?) {
        Toast.makeText(this,"Direccion",Toast.LENGTH_LONG).show()
    }

    override fun onListFragmentInteraction(item: Cart?) {
        Toast.makeText(this,"Carrito ",Toast.LENGTH_LONG).show()
    }
}
