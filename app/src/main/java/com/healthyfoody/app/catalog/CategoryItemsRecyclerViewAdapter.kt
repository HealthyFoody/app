package com.healthyfoody.app.catalog

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.healthyfoody.app.R


import com.healthyfoody.app.catalog.CategoryItemsFragment.OnListFragmentInteractionListener
import com.healthyfoody.app.dummy.DummyContent.DummyItem
import com.healthyfoody.app.models.Product

import kotlinx.android.synthetic.main.fragment_category_items.view.*

class CategoryItemsRecyclerViewAdapter(
    private val mValues: List<Product>,
    private val mListener: OnListFragmentInteractionListener?,
    private val context:Context
) : RecyclerView.Adapter<CategoryItemsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_category_items, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = "S/." + item.price
        holder.mContentView.text = item.name
        val uri : String = "@drawable/"+item.imageUrl
        val imageResource = context.resources.getIdentifier(uri,null,context.packageName)

        holder.mImageview.setImageDrawable( ResourcesCompat.getDrawable(context.resources,imageResource,null))
        holder.mBtnAddItem.setOnClickListener {
            mListener!!.onListFragmentInteraction(item)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.price
        val mContentView: TextView = mView.content
        val mImageview: ImageView = mView.iv_product
        val mBtnAddItem: ImageView = mView.btn_add_item_cart
        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
