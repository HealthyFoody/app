package com.healthyfoody.app.cart

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.healthyfoody.app.R


import com.healthyfoody.app.cart.CartFragment.OnListFragmentInteractionListener
import com.healthyfoody.app.cart.dummy.DummyContent.DummyItem
import com.healthyfoody.app.models.Cart
import com.healthyfoody.app.models.CartMealItem

import kotlinx.android.synthetic.main.fragment_cart.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class CartRecyclerViewAdapter(
    private val mValues: List<CartMealItem>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder>() {
    private lateinit var viewGroup : ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_cart, parent, false)
        viewGroup = parent
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mItemName.text = item.name
        holder.mItemQuantity.text = "x${item.quantity}"
        holder.mItemPrice.text = "S/${item.price}"

        holder.mDeleteButton.setOnClickListener {
            mListener!!.onListFragmentInteraction(item,1)
        }
        holder.mCardview.setOnClickListener {
            mListener!!.onListFragmentInteraction(item,2)
        }
        val uri : String = "@drawable/"+item.imageUrl
        val imageResource = viewGroup.context.resources.getIdentifier(uri,null,viewGroup.context.packageName)
        holder.imgItem.setImageDrawable( ResourcesCompat.getDrawable(viewGroup.context.resources,imageResource,null))
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mItemName: TextView = mView.cart_item_name
        val mItemQuantity: TextView = mView.cart_item_quantity
        val mItemPrice: TextView = mView.cart_item_total_price
        val mDeleteButton: ImageView = mView.btn_delete_item_cart
        val mCardview : CardView = mView.cardview_cart
        val imgItem :ImageView = mView.img_cart_item
    }
}
