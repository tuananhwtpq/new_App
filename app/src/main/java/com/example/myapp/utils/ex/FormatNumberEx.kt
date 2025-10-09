package com.example.myapp.utils.ex

import java.util.Locale

fun Int?.toFormattedString(): String {
    if (this == null) {
        return "0"
    }

    val number = this.toLong()

    return when {
        number >= 1_000_000 -> {
            val value = number / 1_000_000.0
            String.format(Locale.US, "%.1fM", value).replace(".0M", "M")
        }
        number >= 1_000 -> {
            val value = number / 1_000.0
            String.format(Locale.US, "%.1fK", value).replace(".0K", "K")
        }
        else -> {
            number.toString()
        }
    }
}