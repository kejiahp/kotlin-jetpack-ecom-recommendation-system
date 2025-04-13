package com.example.mobile.product_cart_order

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class ProdCartOrderSharedPreferenceService @Inject constructor( private val context: Context){
    companion object {
        const val PREFERENCE_KEY = "product_cart_order_prefs"
        const val RECENT_VIEW = "recent_view"
    }

    private val sharedPref = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)

    private fun saveStringToSharedPreference(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }

    private fun getStringFromSharedPreference(key: String): String? {
        return sharedPref.getString(key, null)
    }

    fun saveRecentViewToPreference(value: List<String>) {
        val serializedValue = Gson().toJson(value)
        saveStringToSharedPreference(RECENT_VIEW, serializedValue)
    }

    fun clearRecentViewPreference() {
        sharedPref.edit().remove(RECENT_VIEW).apply()
    }

    fun getRecentViewFromPreference(): List<String> {
        val recentViewString = getStringFromSharedPreference(RECENT_VIEW)

        if(recentViewString != null) {
            val type = object : TypeToken<List<String>>() {}.type
            val recentViewList: List<String> = Gson().fromJson(recentViewString, type)
            return recentViewList
        } else {
            return emptyList()
        }
    }

}