package com.healthyfoody.app.catalog

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
import android.widget.Toast
import com.healthyfoody.app.R

import com.healthyfoody.app.dummy.DummyContent
import com.healthyfoody.app.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.fragment_category_items_list.view.*

class CategoryItemsFragment : Fragment() {

    private var columnCount = 1
    private var txtTitle : TextView ?= null
    private var listener: OnListFragmentInteractionListener? = null

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

        // inicializar componentes
        txtTitle = view.findViewById(R.id.txt_title_product_items)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            txtTitle!!.setText(requireArguments().getString("title"))
            Toast.makeText(container!!.context,requireArguments().getString("title"),Toast.LENGTH_LONG).show()
        }


        // Set the adapter
        if (view.rv_items_category is RecyclerView) {
            with(view.rv_items_category) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter =
                    CategoryItemsRecyclerViewAdapter(
                        DummyContent.ITEMS,
                        listener
                    )
            }
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
