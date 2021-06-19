package com.example.sunnyweather

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.net.Uri

class WeatherContentProvider : ContentProvider() {
    private val placeDir = 0
    private val placeItem = 1
    private val weatherDir = 2
    private val weatherItem = 3

    private val authority = "com.example.sunnyweather.provider"
    private var dbHelper: MyDatabaseHelper? = null

    private val uriMatcher by lazy {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        matcher.addURI(authority, "place", placeDir)
        matcher.addURI(authority, "place/#", placeItem)
        matcher.addURI(authority, "weather", weatherDir)
        matcher.addURI(authority, "weather/#", weatherItem)
        matcher
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?)
            = dbHelper?.let {
        // 删除数据
        val db = it.writableDatabase
        val deletedRows = when (uriMatcher.match(uri)) {
            placeDir -> db.delete("Place", selection, selectionArgs)
            placeItem -> {
                val placeId = uri.pathSegments[1] 
                db.delete("Place", "id = ?", arrayOf(placeId))
            }
            weatherDir -> db.delete("Weather", selection, selectionArgs)
            weatherItem -> {
                val WeatherId = uri.pathSegments[1]
                db.delete("Weather", "id = ?", arrayOf(WeatherId))
            }
            else -> 0
        }
        deletedRows
    } ?: 0

    override fun getType(uri: Uri) = when (uriMatcher.match(uri)) {
        placeDir -> "vnd.android.cursor.dir/vnd.com.example.sunnyweather.provider.Place"
        placeItem -> "vnd.android.cursor.item/vnd.com.example.sunnyweather.provider.Place"
        weatherDir -> "vnd.android.cursor.dir/vnd.com.example.sunnyweather.provider.Weather"
            weatherItem -> "vnd.android.cursor.item/vnd.com.example.sunnyweather.provider.Weather"
        else -> null
    }

    override fun insert(uri: Uri, values: ContentValues?) = dbHelper?.let {
        // 添加数据
        val db = it.writableDatabase
        val uriReturn = when (uriMatcher.match(uri)) {
            placeDir, placeItem -> {
                val newPlaceId = db.insert("Place", null, values)
                Uri.parse("content://$authority/Place/$newPlaceId")
            }
            weatherDir, weatherItem -> {
                val newWeatherId = db.insert("Weather", null, values)
                Uri.parse("content://$authority/Weather/$newWeatherId")
            }
            else -> null
        }
        uriReturn
    }


    override fun onCreate() = context?.let {
        dbHelper = MyDatabaseHelper(it, "PlaceStore.db", 2)
        true
    } ?: false

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?) = dbHelper?.let {
        // 查询数据
        val db = it.readableDatabase
        val cursor = when (uriMatcher.match(uri)) {
            placeDir -> db.query("Place", projection, selection, selectionArgs,
                null, null, sortOrder)
            placeItem -> {
                val PlaceId = uri.pathSegments[1]
                db.query("Place", projection, "id = ?", arrayOf(PlaceId), null, null,
                    sortOrder)
            }
            weatherDir -> db.query("Weather", projection, selection, selectionArgs,
                null, null, sortOrder)
            weatherItem -> {
                val WeatherId = uri.pathSegments[1]
                db.query("Weather", projection, "id = ?", arrayOf(WeatherId),
                    null, null, sortOrder)
            }
            else -> null
        }
        cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?) = dbHelper?.let {
        // 更新数据
        val db = it.writableDatabase
        val updatedRows = when (uriMatcher.match(uri)) {
            placeDir -> db.update("Place", values, selection, selectionArgs)
            placeItem -> {
                val PlaceId = uri.pathSegments[1]
                db.update("Place", values, "id = ?", arrayOf(PlaceId))
            }
            weatherDir -> db.update("Weather", values, selection, selectionArgs)
            weatherItem -> {
                val WeatherId = uri.pathSegments[1]
                db.update("Weather", values, "id = ?", arrayOf(WeatherId))
            }
            else -> 0
        }
        updatedRows
    } ?: 0
}