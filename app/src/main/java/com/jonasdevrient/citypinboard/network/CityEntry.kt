package com.jonasdevrient.citypinboard.network


import android.content.res.Resources
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jonasdevrient.citypinboard.R
import java.io.BufferedReader
import java.util.*

/**
 * A product entry in the list of products.
 */
class CityEntry(
        val title: String, dynamicUrl: String="https://storage.googleapis.com/material-vignettes.appspot.com/image/0-0.jpg", val url: String, val price: String, val description: String) {
    val dynamicUrl: Uri = Uri.parse(dynamicUrl)

    companion object {
        /**
         * Loads a raw JSON at R.raw.cities and converts it into a list of CityEntry objects
         */
        fun initProductEntryList(resources: Resources): List<CityEntry> {
            val inputStream = resources.openRawResource(R.raw.cities)
            val jsonProductsString = inputStream.bufferedReader().use(BufferedReader::readText)
            val gson = Gson()
            val productListType = object : TypeToken<ArrayList<CityEntry>>() {}.type
            return gson.fromJson<List<CityEntry>>(jsonProductsString, productListType)
        }
    }
}