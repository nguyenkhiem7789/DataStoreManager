package com.nguyen.datastore

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder

class SharedPreferencesManager(private val context: Context) {

    private var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    init {
        val prefsFile = context.packageName
        sharedPreferences = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun delete(key: String) {
        if (sharedPreferences.contains(key)) {
            editor.remove(key).commit()
        }
    }

    fun savePref(key: String, value: Any?) {
        delete(key)
        if (value is Boolean) {
            editor.putBoolean(key, value)
        } else if (value is Int) {
            editor.putInt(key, value)
        } else if (value is Float) {
            editor.putFloat(key, value)
        } else if (value is Long) {
            editor.putLong(key, value)
        } else if (value is String) {
            editor.putString(key, value)
        } else if (value != null) {
            throw RuntimeException()
        }
        editor.commit()
    }

    fun <T> getPref(key: String): T {
        return sharedPreferences.all[key] as T
    }

    fun <T> getPref(key: String, defValue: T): T {
        val returnValue = sharedPreferences.all[key] as T?
        return returnValue ?: defValue
    }

    fun checkPrefExits(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    fun<T> saveDataClass(key: String, data: T) {
        val prettyGson = GsonBuilder().setPrettyPrinting().create()
        val prettyJson = prettyGson.toJson(data)
        savePref(key, prettyJson)
    }

//    fun<T> getDataClass(key: String): T? {
//        val gson = Gson()
//        val prettyJson = getPref(key, null) as? String
//        return gson.fromJson(prettyJson, T::class.java)
//    }

}