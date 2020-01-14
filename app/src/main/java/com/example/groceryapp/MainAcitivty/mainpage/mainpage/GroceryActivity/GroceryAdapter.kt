package com.example.groceryapp.MainAcitivty.mainpage.mainpage.GroceryActivity

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.groceryapp.R


class GroceryAdapter(context: Context, cursor: Cursor?) :
    RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder>() {
    private val mContext: Context
    private var mCursor: Cursor?

    inner class GroceryViewHolder(itemView: View) : ViewHolder(itemView) {
        var nameText: TextView
        var countText: TextView

        init {
            nameText = itemView.findViewById(R.id.textview_name_item)
            countText = itemView.findViewById(R.id.textview_amount_item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroceryViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view: View = inflater.inflate(R.layout.grocery_item, parent, false)
        return GroceryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: GroceryViewHolder,
        position: Int
    ) {
        if (!mCursor!!.moveToPosition(position)) {
            return
        }
        val name: String =
            mCursor!!.getString(mCursor!!.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME))
        val amount: Int =
            mCursor!!.getInt(mCursor!!.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_AMOUNT))
        val id: Long =
            mCursor!!.getLong(mCursor!!.getColumnIndex(GroceryContract.GroceryEntry.MAIN_ID))
        holder.nameText.text = name
        holder.countText.text = amount.toString()
        holder.itemView.tag = id
    }

    override fun getItemCount(): Int {
        return mCursor!!.count
    }

    fun swapCursor(newCursor: Cursor?) {
        if (mCursor != null) {
            mCursor!!.close()
        }
        mCursor = newCursor
        if (newCursor != null) {
            notifyDataSetChanged()
        }
    }

    init {
        mContext = context
        mCursor = cursor
    }
}