package com.healthyfoody.app.address

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.healthyfoody.app.R
import com.healthyfoody.app.common.CONSTANTS
import com.healthyfoody.app.common.SharedPreferences

import com.healthyfoody.app.models.Address
import com.healthyfoody.app.models.MainUserValues
import com.healthyfoody.app.services.AddressService
import kotlinx.android.synthetic.main.fragment_address_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AddressFragment : Fragment() {

    // TODO: Customize parameters
    private var REQUEST_ADD_CODE : Int = 1
    private var columnCount = 1
    private lateinit var addressService : AddressService
    private var listAdresses : List<Address> = listOf()
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var btnAddAddress : Button
    private lateinit var txtTitle : TextView
    private lateinit var viewGroup : ViewGroup
    private lateinit var recyclerView : RecyclerView
    private var gson = Gson()
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var mainInfo : MainUserValues

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
        val view = inflater.inflate(R.layout.fragment_address_list, container, false)
        recyclerView = view.findViewById(R.id.rv_address_list)
        initAddressService()
        sharedPreferences = SharedPreferences(container!!.context)
        viewGroup = container!!
        mainInfo = gson.fromJson(sharedPreferences.getValue(CONSTANTS.MAIN_INFO),MainUserValues::class.java)
        Log.d("TOKEN INFO",sharedPreferences.getValue(CONSTANTS.MAIN_INFO))
        reloadAddresses()

        btnAddAddress = view.findViewById(R.id.btn_add_address_activity)
        txtTitle = view.findViewById(R.id.txt_title_address)
        viewGroup = container!!

        btnAddAddress.setOnClickListener{
            openAddAddressActivity(viewGroup)
        }
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = AddressRecyclerViewAdapter(
                listAdresses,
                listener
            )
        }
        return view
    }
    fun openAddAddressActivity(container : ViewGroup){
        var addAddressActivity = Intent(
            container.context,
            GoogleMapsActivity::class.java)
        startActivityForResult(addAddressActivity,REQUEST_ADD_CODE)

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

    fun initAddressService(){
        val retrofit = Retrofit.Builder()
            .baseUrl(CONSTANTS.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        addressService = retrofit.create(AddressService::class.java)
    }
    fun deleteAddress(address: Address){
        addressService.deleteAddressById("application/json",mainInfo.token!!,address.id).enqueue(object : Callback<Void>{
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(viewGroup.context,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                   reloadAddresses()
                }else{
                    Log.d("ADDRESS NOT SUCCESSFUL",gson.toJson(response.code()))
                    Toast.makeText(viewGroup.context,"ERROR?",Toast.LENGTH_LONG).show()
                }
            }
        })
    }
    fun reloadAddresses(){
        addressService.findAddressesByIdCustomer("application/json",mainInfo.token!!,mainInfo.customerId!!).enqueue(object : Callback<List<Address>> {
            override fun onFailure(call: Call<List<Address>>, t: Throwable) {
                Log.d("ERROR CATEGORIA F",t.toString())
                Toast.makeText(viewGroup.context,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<Address>>,
                response: Response<List<Address>>
            ) {
                if (response.isSuccessful) {
                    //Toast.makeText(viewGroup.context,"SATISFACTORIO??",Toast.LENGTH_LONG).show()
                    listAdresses = response.body()!!
                    Log.d("LISTA ADDRESS",gson.toJson(listAdresses))
                    with(recyclerView) {
                        adapter =
                            AddressRecyclerViewAdapter(
                                listAdresses,
                                listener
                            )
                        adapter!!.notifyDataSetChanged()
                    }
                }else{
                    Log.d("ADDRESS NOT SUCCESSFUL",gson.toJson(response.code()))
                    Toast.makeText(viewGroup.context,"ERROR?",Toast.LENGTH_LONG).show()
                }
            }
        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_ADD_CODE){
            if(resultCode == Activity.RESULT_OK){
                //Toast.makeText(viewGroup.context,"LLEGUE JEJE",Toast.LENGTH_LONG).show()
                reloadAddresses()
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
        fun onListFragmentInteraction(item: Address?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            AddressFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
