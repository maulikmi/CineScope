package com.cinescope.movie.utils

object RuntimeFormatter {

    fun format(minutes: Int?): String {
        if (minutes == null || minutes <= 0) {
            return "Runtime unavailable"
        }
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return when {
            hours > 0 && remainingMinutes > 0 ->
                "${hours}h ${remainingMinutes}m"
            hours > 0 ->
                "${hours}h"
            else ->
                "${remainingMinutes}m"
        }
    }
}