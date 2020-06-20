package com.healthyfoody.app.address

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.healthyfoody.app.R


import com.healthyfoody.app.address.AddressFragment.OnListFragmentInteractionListener
import com.healthyfoody.app.dummy.DummyContent.DummyItem
import com.healthyfoody.app.models.Address

import kotlinx.android.synthetic.main.fragment_address.view.*


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
        holder.txtName.text = item.name
        holder.txtFullAddress.text = item.fullAddress
        holder.imgDeleteIcon.setOnClickListener {
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val txtName: TextView = mView.address_name_list
        val txtFullAddress: TextView = mView.address_full_address_list
        val imgDeleteIcon : ImageView = mView.image_delete_address
        override fun toString(): String {
            return super.toString() + " '" + txtFullAddress.text + "'"
        }
    }
}
