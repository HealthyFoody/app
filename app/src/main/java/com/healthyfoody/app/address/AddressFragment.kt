package com.healthyfoody.app.address

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.healthyfoody.app.R

import com.healthyfoody.app.models.Address
import com.healthyfoody.app.services.AddressService
import kotlinx.android.synthetic.main.fragment_address_list.view.*


class AddressFragment : Fragment() {

    // TODO: Customize parameters
    private var REQUEST_ADD_CODE : Int = 1
    private var columnCount = 1
    private var addressService = AddressService()
    private lateinit var listAdresss : List<Address>
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var btnAddAddress : Button
    private lateinit var txtTitle : TextView
    private lateinit var viewGroup : ViewGroup
    private lateinit var recyclerView : RecyclerView

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
        listAdresss = addressService.findAll()
        btnAddAddress = view.findViewById(R.id.btn_add_address_activity)
        txtTitle = view.findViewById(R.id.txt_title_address)
        viewGroup = container!!
        recyclerView = view.findViewById(R.id.rv_address_list)
        btnAddAddress.setOnClickListener{
            openAddAddressActivity(viewGroup)
        }
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = AddressRecyclerViewAdapter(
                listAdresss,
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_ADD_CODE){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(viewGroup.context,"LLEGUE JEJE",Toast.LENGTH_LONG).show()
                listOf(Address(12.00f, 13.0f)).also {
                    listAdresss = it.toList()
                }
                with(recyclerView) {
                    adapter =
                        AddressRecyclerViewAdapter(
                            listAdresss,
                            listener
                        )
                    adapter!!.notifyDataSetChanged()
                }


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
        fun onListFragmentInteraction(item: Address?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            AddressFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
