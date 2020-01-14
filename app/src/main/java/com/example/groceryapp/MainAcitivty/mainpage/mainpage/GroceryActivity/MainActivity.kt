package com.example.groceryapp.MainAcitivty.mainpage.mainpage.GroceryActivity

import GroceryContract
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.groceryapp.MainAcitivty.mainpage.mainpage.GroceryActivity.NoteActivity.Main2Activity
import com.example.groceryapp.R
import kotlinx.android.synthetic.main.activity_main.*


open class MainActivity : AppCompatActivity() {

    private var mDatabase: SQLiteDatabase? = null
    private var mAdapter: GroceryAdapter? = null
    private var mEditTextName: EditText? = null
    private var mTextViewAmount: TextView? = null
    private var mAmount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val dbHelper =
            GroceryDBHelper(
                this
            )
        mDatabase = dbHelper.writableDatabase

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter =
            GroceryAdapter(
                this,
                allItems
            )
        recyclerView.adapter = mAdapter
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                removeItem(viewHolder.itemView.tag as Long)
            }
        }).attachToRecyclerView(recyclerView)

        mEditTextName = findViewById(R.id.edittext_name)
        mTextViewAmount = findViewById(R.id.textview_amount)

        val buttonIncrease: Button = findViewById(R.id.button_increase)
        val buttonDecrease: Button = findViewById(R.id.button_decrease)

        val buttonAdd: Button = findViewById(R.id.button_add)

        buttonIncrease.setOnClickListener { increase() }
        buttonDecrease.setOnClickListener { decrease() }
        buttonAdd.setOnClickListener { addItem() }
        ViewListBtn.setOnClickListener{
            changeIntent()}
    }

    private fun increase() {
        mAmount++
        mTextViewAmount!!.text = mAmount.toString()
    }

    private fun decrease() {
        if (mAmount > 0) {
            mAmount--
            mTextViewAmount!!.text = mAmount.toString()
        }
    }

    private fun addItem() {
        if (mEditTextName!!.text.toString().trim { it <= ' ' }.isEmpty() || mAmount == 0) {
            return
        }
        val name = mEditTextName!!.text.toString()
        val cv = ContentValues()
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name)
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmount)
        mDatabase!!.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv)
        mAdapter!!.swapCursor(allItems)
        mEditTextName!!.text.clear()
    }

    private fun removeItem(id: Long) {
        mDatabase!!.delete(
            GroceryContract.GroceryEntry.TABLE_NAME,
            GroceryContract.GroceryEntry.MAIN_ID.toString() + "=" + id, null
        )
        mAdapter!!.swapCursor(allItems)
    }

    private fun changeIntent(){
        var intent = Intent(this, Main2Activity::class.java)
        startActivity(intent)

    }

    private val allItems: Cursor
        get() = mDatabase!!.query(
            GroceryContract.GroceryEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null, GroceryContract.GroceryEntry.COLUMN_TIMESTAMP.toString() + " DESC"
        )

}
