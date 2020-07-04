package com.healthyfoody.app.cart

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.healthyfoody.app.MainActivity
import com.healthyfoody.app.R
import com.healthyfoody.app.address.GoogleMapsActivity

import com.healthyfoody.app.cart.dummy.DummyContent
import com.healthyfoody.app.cart.dummy.DummyContent.DummyItem
import com.healthyfoody.app.catalog.ProductDetailActivity
import com.healthyfoody.app.common.CONSTANTS
import com.healthyfoody.app.common.SharedPreferences
import com.healthyfoody.app.models.*
import com.healthyfoody.app.services.CartService
import com.healthyfoody.app.services.CategoryService
import com.healthyfoody.app.services.ProductService
import kotlinx.android.synthetic.main.fragment_address_list.view.*
import kotlinx.android.synthetic.main.fragment_cart_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [CartFragment.OnListFragmentInteractionListener] interface.
 */
class CartFragment : Fragment() {

    // TODO: Customize parameters
    private var REQUEST_EDIT_ITEM : Int = 10
    private var columnCount = 1
    private lateinit var cartService : CartService
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var cart : Cart
    private var listCartMeals: List<CartMealItem> = listOf()
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var mainInfo : MainUserValues
    private var cartId = ""
    private lateinit var viewGroup: ViewGroup
    private val gson = Gson()


    private lateinit var recyclerView: RecyclerView
    private lateinit var txtTitle : TextView
    private lateinit var btnContinue : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart_list, container, false)
        recyclerView = view.findViewById(R.id.rv_items_cart)
        txtTitle = view.txt_title_cart
        txtTitle.text = "MI CARRITO"
        btnContinue = view.btn_continue_cart

        viewGroup = container!!
        sharedPreferences = SharedPreferences(viewGroup.context)
        mainInfo = gson.fromJson(sharedPreferences.getValue(CONSTANTS.MAIN_INFO),MainUserValues::class.java)
        cartId = sharedPreferences.getValue(CONSTANTS.CART_ID).toString()

        initServices()

        // Set the adapter
        with(recyclerView) {
            layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = CartRecyclerViewAdapter(listCartMeals, listener)
        }
        updateListCart()
        return view
    }
    fun deleteItem(item:CartMealItem){

        val cartMealRequest = CartMealRequest(item.id,item.quantity,"meal")
        cartService.deleteItemFromCart("application/json",mainInfo.token!!,cart.id,cartMealRequest).enqueue(object :
            Callback<Cart> {
            override fun onFailure(call: Call<Cart>, t: Throwable) {
                Toast.makeText(viewGroup.context,"Error", Toast.LENGTH_LONG).show()
                Log.d("cartFragmentDeleteItem", t.toString())
            }

            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                if (response.isSuccessful){
                    Toast.makeText(viewGroup.context,"Eliminado satisfactoriamente", Toast.LENGTH_LONG).show()
                    updateListCart()

                }else{
                    Log.e("DELETE ITEM FROM CART",response.toString())
                }
            }
        })
    }
    fun initServices(){
        val retrofit = Retrofit.Builder()
            .baseUrl(CONSTANTS.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        cartService = retrofit.create(CartService::class.java)
    }
    fun updateListCart(){
        cartService.getCart("application/json",mainInfo.token!!,mainInfo.customerId!!).enqueue(object :
            Callback<Cart> {
            override fun onFailure(call: Call<Cart>, t: Throwable) {
                Toast.makeText(viewGroup.context,"Error", Toast.LENGTH_LONG).show()
                Log.d("cartFragmentGetCart", t.toString())
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                if (response.isSuccessful){
                   cart = response.body()!!
                   if(cart.totalPrice != null){
                       btnContinue.text = "Continuar S/${cart.totalPrice}"
                   }
                   listCartMeals = cart.meals

                   if(listCartMeals != null){
                       listCartMeals = listCartMeals.sortedBy { it.id }
                   }else{
                       listCartMeals = listOf()

                   }
                    recyclerView.adapter = CartRecyclerViewAdapter(listCartMeals, listener)
                    recyclerView.adapter!!.notifyDataSetChanged()

                }else{
                    Log.e("GET CART ERROR",response.toString())
                }
            }
        })
    }

    fun editCartItem(item:CartMealItem){
        val editCartItemActivity = Intent(
            viewGroup.context,
            ProductDetailActivity::class.java)
        editCartItemActivity.putExtra("productId",item.id)
        editCartItemActivity.putExtra("quantity",item.quantity.toString())
        editCartItemActivity.putExtra("requestCode",REQUEST_EDIT_ITEM.toString())
        startActivityForResult(editCartItemActivity,REQUEST_EDIT_ITEM)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_EDIT_ITEM){
            if(resultCode == Activity.RESULT_OK){
                //Toast.makeText(viewGroup.context,"LLEGUE JEJE",Toast.LENGTH_LONG).show()
                updateListCart()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: CartMealItem?,type:Number)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
