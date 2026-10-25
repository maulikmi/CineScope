package com.cinescope.movie.utils

object CurrencyFormatter {

    fun format(amount: Long?): String {
        if (amount == null || amount <= 0) return "-"
        return when {
            amount >= 1_000_000_000 -> {
                val b = amount / 1_000_000_000.0
                if (amount % 1_000_000_000 == 0L) "$${amount / 1_000_000_000}B"
                else "$${(b * 10).toInt() / 10.0}B"
            }
            amount >= 1_000_000 -> {
                val m = amount / 1_000_000.0
                if (amount % 1_000_000 == 0L) "$${amount / 1_000_000}M"
                else "$${(m * 10).toInt() / 10.0}M"
            }
            amount >= 1_000 -> {
                val k = amount / 1_000.0
                if (amount % 1_000 == 0L) "$${amount / 1_000}K"
                else "$${(k * 10).toInt() / 10.0}K"
            }
            else -> "$$amount"
        }
    }
}
