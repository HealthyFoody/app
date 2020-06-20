package com.healthyfoody.app.catalog

import android.content.Context

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.healthyfoody.app.R
import com.healthyfoody.app.common.CONSTANTS
import com.healthyfoody.app.common.SharedPreferences

import com.healthyfoody.app.dummy.DummyContent
import com.healthyfoody.app.dummy.DummyContent.DummyItem
import com.healthyfoody.app.models.MainUserValues
import com.healthyfoody.app.models.Product
import com.healthyfoody.app.services.ProductService
import kotlinx.android.synthetic.main.fragment_category_items_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryItemsFragment : Fragment() {

    private var columnCount = 1
    private var txtTitle : TextView ?= null
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var categoryId :String
    private lateinit var listProduct : List<Product>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewGroup: ViewGroup
    private lateinit var productService: ProductService
    private lateinit var mainInfo : MainUserValues
    private lateinit var recyclerView : RecyclerView
    private var gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        * Agregar ingreso de parametros
        * arguments?.let {
          columnCount = it.getInt(ARG_COLUMN_COUNT)
          txtTitle!!.setText(arguments!!.getString("title"))
          }
        * */
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category_items_list, container, false)
        listProduct = listOf()
        sharedPreferences = SharedPreferences(container!!.context)
        mainInfo = gson.fromJson(sharedPreferences.getValue(CONSTANTS.MAIN_INFO),MainUserValues::class.java)
        viewGroup = container!!
        recyclerView = view.rv_items_category
        // inicializar componentes
        txtTitle = view.findViewById(R.id.txt_title_product_items)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            txtTitle!!.setText(requireArguments().getString("title"))
            categoryId = requireArguments().getString("categoryId")!!
            Toast.makeText(container!!.context,categoryId,Toast.LENGTH_LONG).show()
        }

        loadProducts()

        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter =
                CategoryItemsRecyclerViewAdapter(
                    listProduct,
                    listener,
                    viewGroup.context
                )
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }
    fun loadProducts(){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.62:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        productService = retrofit.create(ProductService::class.java)
        productService.findAll("application/json",mainInfo.token!!,categoryId).enqueue(object : Callback<List<Product>> {
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(viewGroup.context,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    //Toast.makeText(viewGroup.context,"Exito",Toast.LENGTH_LONG).show()

                    listProduct = response.body()!!

                    recyclerView!!.adapter =   CategoryItemsRecyclerViewAdapter(
                        listProduct!!,
                        listener,
                        viewGroup.context
                    )
                    recyclerView!!.adapter!!.notifyDataSetChanged()

                }else{
                    Log.d("PRODUCTO",gson.toJson(mainInfo))
                    Toast.makeText(viewGroup.context,response.toString(),Toast.LENGTH_LONG).show()
                }
            }
        })
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
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
        fun onListFragmentInteraction(item: DummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CategoryItemsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
