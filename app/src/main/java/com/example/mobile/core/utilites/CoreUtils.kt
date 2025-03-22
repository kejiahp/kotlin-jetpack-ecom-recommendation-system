package com.example.mobile.core.utilites

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random


/**
 * JSONObject extension function that allows the conversion of JSONObjects to maps.
 *
 * This function saved me stress of serializing JSON into data classes especially when you don't know the form of the JSON
 * */
fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
    when (val value = this[it]) {
        is JSONArray -> {
            val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
            JSONObject(map).toMap().values.toList()
        }

        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else -> value
    }
}

object CoreUtils {
    /**
     * utility method for maximum `System.out` efficiency while debugging 🙃
     * */
    fun printDebugger(tag: String, value: Any?) {
        print("$tag:    ")
        println(value)
    }

    /**
     * Custom function that aims to extract an error message from the given errorBody.
     *
     * If message is found it returns null (It is expected to return null)
     * */
    fun getErrorMsg(errorBody: ResponseBody): Any? {
        try {
            val jsonObject: JSONObject = JSONObject(errorBody.string());
            val map = jsonObject.toMap()
            val errorDetail = map.getValue("detail")

            val errorMsg = errorDetail?.let { errorIt ->
                when (errorIt) {
                    is List<*> -> {
                        errorIt.forEach { it ->
                            when (it) {
                                is Map<*, *> -> {
                                    if (it.containsKey("msg")) it["msg"] else null
                                }
                            }
                        }
                    }

                    is Map<*, *> -> {
                        if (errorIt.containsKey("message")) errorIt["message"] else null
                    }
                    else -> null
                }
            }
            return errorMsg

        } catch (e: Exception) {
            return null
        }
    }

    /**
     * Generates a random username
     * */
    fun randomUsernameGenerator(): String {
        val usernamesList1 = listOf(
            "Max", "Leo", "Eli", "Sam", "Ray",
            "Nia", "Lia", "Kai", "Zoe", "Eva"
        )
        val usernamesList2 = listOf(
            "Storm", "Wave", "Blaze", "Sky", "Frost",
            "Echo", "Dash", "Flare", "Stone", "Hawk"
        )
        return "${usernamesList1.random()}${usernamesList2.random()}"
    }


    /**
     * Generate a random number
     * */
    fun randomAgeGenerator(): Int {
        return Random.nextInt(18, 101)
    }


    /**
     * Utility function to check if there is an internet connection.
     *
     * User permissions where also added to the `AndroidManifest.xml` file to allow this.
     * */
    @RequiresApi(Build.VERSION_CODES.M)
    fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)
        if (actNw == null) {
            return false
        }
        val result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return result
    }

//    fun isInternetConnected(context: Context) {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val networkCapabilities = connectivityManager.activeNetwork ?: return false
//        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
//        val result = when {
//            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//            else -> false
//        }
//        return result
//    }
}