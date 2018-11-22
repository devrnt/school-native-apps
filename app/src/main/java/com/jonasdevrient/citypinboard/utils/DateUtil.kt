package com.jonasdevrient.citypinboard.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun toSimpleString(date: Date): String {
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }
}
