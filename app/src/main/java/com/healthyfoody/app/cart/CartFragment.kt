package com.healthyfoody.app.cart

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.healthyfoody.app.R

import com.healthyfoody.app.cart.dummy.DummyContent
import com.healthyfoody.app.cart.dummy.DummyContent.DummyItem
import com.healthyfoody.app.models.Cart
import com.healthyfoody.app.services.CartService
import kotlinx.android.synthetic.main.fragment_address_list.view.*
import kotlinx.android.synthetic.main.fragment_cart_list.view.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [CartFragment.OnListFragmentInteractionListener] interface.
 */
class CartFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private var cartService : CartService = CartService()
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var listCart : List<Cart>

    private lateinit var recyclerView: RecyclerView
    private lateinit var txtTitle : TextView

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
        val view = inflater.inflate(R.layout.fragment_cart_list, container, false)

        recyclerView = view.findViewById(R.id.rv_items_cart)
        txtTitle = view.txt_title_cart

        listCart = cartService.findAll()
        // Set the adapter
        with(recyclerView) {
            layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = CartRecyclerViewAdapter(listCart, listener)
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
        fun onListFragmentInteraction(item: Cart?)
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
