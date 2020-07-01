package com.healthyfoody.app.catalog


import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import com.healthyfoody.app.MainActivity
import com.healthyfoody.app.R
import com.healthyfoody.app.common.CONSTANTS
import com.healthyfoody.app.common.SharedPreferences
import com.healthyfoody.app.models.CartMealRequest
import com.healthyfoody.app.models.MainUserValues
import com.healthyfoody.app.models.Product
import com.healthyfoody.app.models.Token
import com.healthyfoody.app.services.CartService
import com.healthyfoody.app.services.ProductService
import com.healthyfoody.app.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductDetailActivity : AppCompatActivity() {
    private lateinit var myBundle : Bundle
    private lateinit var sharedPreferences : SharedPreferences
    private var productId = "NO_CODE"
    private val gson = Gson()
    private lateinit var mainInfo :MainUserValues
    private var cartId  = ""
    private lateinit var productService : ProductService
    private lateinit var cartService: CartService

    private lateinit var imgProduct :ImageView
    private lateinit var txtNameProduct : TextView
    private lateinit var txtDescriptionProduct : TextView
    private lateinit var txtPriceProduct : TextView
    private lateinit var txtIngredientsTitle : TextView
    private lateinit var listViewIngredients : ListView
    private var product : Product? = null

    private lateinit var imgPlusButton : ImageView
    private lateinit var imgMinusButton : ImageView
    private lateinit var txtQuantity : TextView
    private lateinit var btnAddProduct : Button

    private var quantity = 1
    private var requestCode: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        myBundle = intent.extras!!
        sharedPreferences = SharedPreferences(this)

        myBundle.let {
            productId = it.getString("productId")!!
            if(it.containsKey("quantity")){
                quantity = it.getString("quantity")!!.toInt()
            }
            if(it.containsKey("requestCode")){
                requestCode = it.getString("requestCode")!!.toInt()
            }
            Toast.makeText(this,productId, Toast.LENGTH_LONG).show()
        }
        mainInfo = gson.fromJson(sharedPreferences.getValue(CONSTANTS.MAIN_INFO),MainUserValues::class.java)
        cartId = sharedPreferences.getValue(CONSTANTS.CART_ID)!!
        
        imgProduct = findViewById(R.id.img_product)
        txtNameProduct = findViewById(R.id.txt_name_product)
        txtDescriptionProduct = findViewById(R.id.txt_description_product)
        txtPriceProduct = findViewById(R.id.txt_price_product)
        txtIngredientsTitle = findViewById(R.id.txt_title_ingredients)
        listViewIngredients = findViewById(R.id.list_products_ingredients)

        imgPlusButton = findViewById(R.id.img_plus_button)
        imgMinusButton = findViewById(R.id.img_minus_button)
        txtQuantity = findViewById(R.id.txt_quantity)
        btnAddProduct = findViewById(R.id.btn_add_cart_product)

        imgPlusButton.setOnClickListener {
            addQuantity()
        }
        imgMinusButton.setOnClickListener {
            removeQuantity()
        }

        btnAddProduct.setOnClickListener{
            addToCart()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(CONSTANTS.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        cartService = retrofit.create(CartService::class.java)
        productService = retrofit.create(ProductService::class.java)
        productService.findById("application/json",mainInfo.token!!,productId).enqueue(object :
            Callback<Product> {
            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity,"Error", Toast.LENGTH_LONG).show()
                Log.d("productdetailactivity", t.toString())
            }

            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful){
                    Toast.makeText(this@ProductDetailActivity,"Login satisfactorio", Toast.LENGTH_LONG).show()
                    val productTemp = response.body()!!
                    updateInfo(productTemp)
                    product = productTemp

                }else{
                    Log.e("Product Error",response.toString())
                }
            }
        })

    }


    @SuppressLint("SetTextI18n")
    fun addQuantity(){
        if(product != null && quantity < 10){
            quantity += 1
            val totalPrice = quantity * product!!.price.toFloat()
            val strTotalPrice = "S/$totalPrice"
            btnAddProduct.text = "AGREGAR $strTotalPrice"
            txtQuantity.text = quantity.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    fun removeQuantity(){
        if(product != null && quantity > 1){
            quantity -= 1
            val totalPrice = quantity * product!!.price.toFloat()
            val strTotalPrice = "S/$totalPrice"
            btnAddProduct.text = "AGREGAR $strTotalPrice"
            txtQuantity.text = quantity.toString()
        }
    }

    fun addToCart(){
        val cartMealRequest = CartMealRequest(productId,quantity,"meal")
        cartService.addMealCart("application/json",mainInfo.token!!,cartId,cartMealRequest).enqueue(object :
            Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity,"Error", Toast.LENGTH_LONG).show()
                Log.d("productdetailactivity", t.toString())
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful){
                    if(requestCode == null){
                        Toast.makeText(this@ProductDetailActivity,"Agregado satisfactoriamente",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@ProductDetailActivity,"Editado satisfactoriamente",Toast.LENGTH_LONG).show()
                        setResult(RESULT_OK)
                    }
                    finish()

                }else{
                    Log.e("Product Error",response.toString())
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun updateInfo(product:Product){
        val uri : String = "@drawable/"+product.imageUrl
        val imageResource = this.resources.getIdentifier(uri,null,this.packageName)
        imgProduct.setImageDrawable( ResourcesCompat.getDrawable(this.resources,imageResource,null))
        txtQuantity.text = quantity.toString()
        txtNameProduct.text = product.name
        txtDescriptionProduct.text = product.description
        val totalPrice = product.price.toFloat()*quantity
        var txtPrice = "S/${product.price}"
        txtPriceProduct.text = txtPrice
        txtPrice = "S/$totalPrice"
        btnAddProduct.text = "AGREGAR $txtPrice"
        if(product.ingredients == null || product.ingredients.isEmpty()){
            txtIngredientsTitle.visibility = View.INVISIBLE
        }else{
            txtIngredientsTitle.text = "INGREDIENTES"
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, product.ingredients)
            listViewIngredients.adapter = adapter
        }

    }

}