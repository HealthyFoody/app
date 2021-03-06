package com.healthyfoody.app.catalog

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.healthyfoody.app.R


import com.healthyfoody.app.catalog.CategoryFragment.OnListFragmentInteractionListener
import com.healthyfoody.app.models.Category

import kotlinx.android.synthetic.main.fragment_category.view.*

class CategoryRecyclerViewAdapter(
    private val mValues: List<Category>,
    private val mListener: OnListFragmentInteractionListener?,
    private val context : Context
) : RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mContentView.text = item.name
        val uri : String = "@drawable/"+item.imageUrl
        var imageResource = context.resources.getIdentifier(uri,null,context.packageName)

        holder.mImageView.setImageDrawable( ResourcesCompat.getDrawable(context.resources,imageResource,null))
        holder.mCardView.setOnClickListener({
            mListener?.onListFragmentInteraction(item)
        })
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content
        val mCardView: CardView = mView.cardview_category
        val mImageView : ImageView = mView.item_image

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
