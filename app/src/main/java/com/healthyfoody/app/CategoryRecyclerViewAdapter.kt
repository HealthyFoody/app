package com.healthyfoody.app

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView


import com.healthyfoody.app.CategoryFragment.OnListFragmentInteractionListener
import com.healthyfoody.app.models.Category

import kotlinx.android.synthetic.main.fragment_category.view.*

class CategoryRecyclerViewAdapter(
    private val mValues: List<Category>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.id
        holder.mContentView.text = item.name
        holder.mCardView.setOnClickListener({
            mListener?.onListFragmentInteraction(item)
        })
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content
        val mCardView: CardView = mView.cardview_category

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
