package com.healthyfoody.app.catalog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.healthyfoody.app.R
import com.healthyfoody.app.common.CONSTANTS
import com.healthyfoody.app.common.SharedPreferences
import com.healthyfoody.app.models.Category
import com.healthyfoody.app.models.MainUserValues
import com.healthyfoody.app.services.CategoryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 2
    private lateinit var serviceCategory: CategoryService
    private var listener: OnListFragmentInteractionListener? = null
    private var listCategory : List<Category> = listOf()
    private var recyclerView : RecyclerView ?= null
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var mainInfo : MainUserValues
    private lateinit var viewGroup: ViewGroup
    private var gson = Gson()


    private fun loadCategories() {

        val retrofit = Retrofit.Builder()
            .baseUrl(CONSTANTS.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        serviceCategory = retrofit.create(CategoryService::class.java)
        serviceCategory.findAll("application/json",mainInfo.token!!).enqueue(object : Callback<List<Category>> {
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.d("CATEGORIA",gson.toJson(mainInfo))
                Log.d("ERROR CATEGORIA F",t.toString())
                Toast.makeText(viewGroup.context,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful) {
                    //Toast.makeText(viewGroup.context,"SATISFACTORIO??",Toast.LENGTH_LONG).show()

                    listCategory = response.body()!!
                    Log.d("LISTA CATEGORIA",gson.toJson(listCategory))
                    recyclerView!!.adapter =   CategoryRecyclerViewAdapter(
                        listCategory,
                        listener,
                        viewGroup.context
                    )
                    recyclerView!!.adapter!!.notifyDataSetChanged()

                }else{
                    Log.d("CATEGORIA",gson.toJson(mainInfo))
                    //Toast.makeText(viewGroup.context,response.toString(),Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = SharedPreferences(container!!.context)
        viewGroup = container
        mainInfo = gson.fromJson(sharedPreferences.getValue(CONSTANTS.MAIN_INFO),MainUserValues::class.java)
        loadCategories()

        val view = inflater.inflate(R.layout.fragment_category_list, container, false)
        recyclerView = view.findViewById(R.id.rv_category_list)

        // Set the adapter
        with(recyclerView!!) {
               layoutManager = when {
                   columnCount <= 1 -> LinearLayoutManager(context)
                   else -> GridLayoutManager(context, columnCount)
               }
               adapter =
                   CategoryRecyclerViewAdapter(
                       listCategory,
                       listener,
                       context
                   )
           }
        //recyclerView!!.adapter!!.notifyDataSetChanged()

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
        fun onListFragmentInteraction(item: Category?)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
