package com.healthyfoody.app

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.healthyfoody.app.models.Category
import kotlinx.android.synthetic.main.fragment_category_list.view.*

class CategoryFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 2
    private var serviceCategory = ServiceCategory()
    private var listener: OnListFragmentInteractionListener? = null
    private var listCategory : List<Category> ?= null
    private var recyclerView : RecyclerView ?= null

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
        val view = inflater.inflate(R.layout.fragment_category_list, container, false)
        listCategory = serviceCategory.findAll()
        recyclerView = view.findViewById(R.id.rv_category_list)

        // Set the adapter
        with(recyclerView!!) {
               layoutManager = when {
                   columnCount <= 1 -> LinearLayoutManager(context)
                   else -> GridLayoutManager(context, columnCount)
               }
               adapter = CategoryRecyclerViewAdapter(serviceCategory.findAll(), listener)
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
