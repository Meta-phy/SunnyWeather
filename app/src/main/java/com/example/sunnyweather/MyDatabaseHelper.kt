package com.example.sunnyweather

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MyDatabaseHelper(val context: Context, name: String, version: Int):
    SQLiteOpenHelper(context, name, null, version) {
    private val createPlace = "create table Place (" +
            "id text primary key," +
            "name text," +
            "country text," +
            "adm1 text," +
            "adm2 text)"
    private val createWeather = "create table Weather (" +
            "id integer primary key," +
            "temp text," +
            "text text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createPlace)
        db.execSQL(createWeather)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion <= 1) {
            db.execSQL(createWeather)
        }
        if (oldVersion <= 2) {
            db.execSQL("alter table Place add column weather_id integer")
        }

    }
}