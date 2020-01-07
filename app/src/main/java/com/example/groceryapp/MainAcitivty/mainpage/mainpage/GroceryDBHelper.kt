package com.example.groceryapp.MainAcitivty.mainpage.mainpage

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GroceryDBHelper(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {
    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_GROCERYLIST_TABLE = "CREATE TABLE " +
                GroceryContract.GroceryEntry.TABLE_NAME.toString() + " (" +
                GroceryContract.GroceryEntry.MAIN_ID.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GroceryContract.GroceryEntry.COLUMN_NAME.toString() + " TEXT NOT NULL, " +
                GroceryContract.GroceryEntry.COLUMN_AMOUNT.toString() + " INTEGER NOT NULL, " +
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP.toString() + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");"
        db.execSQL(SQL_CREATE_GROCERYLIST_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS " + GroceryContract.GroceryEntry.TABLE_NAME)
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "grocerylist.db"
        const val DATABASE_VERSION = 1
    }
}
