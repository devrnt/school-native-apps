package com.jonasdevrient.citypinboard.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Static class used to format a date to our local used date format
 */
object DateUtil {
    fun toSimpleString(date: Date): String {
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }
}
