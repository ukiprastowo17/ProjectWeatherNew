package com.projectweather.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.projectweather.remote.Constant.CITY_COL
import com.projectweather.remote.Constant.CITY_TABLE_NAME
import com.projectweather.remote.Constant.COUNTRY_COL
import com.projectweather.remote.Constant.DATABASE_NAME
import com.projectweather.remote.Constant.DATABASE_VERSION
import com.projectweather.remote.Constant.ID_COL
import com.projectweather.remote.Constant.LAT_COL
import com.projectweather.remote.Constant.LON_COL
import com.projectweather.remote.Constant.ZIP_COL

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query: String =
            ("CREATE TABLE $CITY_TABLE_NAME (" +
                    "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$ZIP_COL TEXT, " +
                    "$CITY_COL TEXT, " +
                    "$LAT_COL TEXT, " +
                    "$LON_COL TEXT, " +
                    "$COUNTRY_COL TEXT )")
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $CITY_TABLE_NAME")
        onCreate(db)
    }
}