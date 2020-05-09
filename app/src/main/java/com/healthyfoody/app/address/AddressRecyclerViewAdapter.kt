package com.healthyfoody.app.address

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.healthyfoody.app.R


import com.healthyfoody.app.address.AddressFragment.OnListFragmentInteractionListener
import com.healthyfoody.app.dummy.DummyContent.DummyItem
import com.healthyfoody.app.models.Address

import kotlinx.android.synthetic.main.fragment_address.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class AddressRecyclerViewAdapter(
    private val mValues: List<Address>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_address, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.lat.toString()
        holder.mContentView.text = item.lng.toString()
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
