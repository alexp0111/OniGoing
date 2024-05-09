package ru.alexp0111.onigoing.utils

import java.util.concurrent.TimeUnit

fun Long.asFormattedTimeString(): String {
    val day = TimeUnit.SECONDS.toDays(this)
    val hours: Long = TimeUnit.SECONDS.toHours(this) -
            TimeUnit.DAYS.toHours(day)
    val minutes: Long = TimeUnit.SECONDS.toMinutes(this) -
            TimeUnit.DAYS.toMinutes(day) -
            TimeUnit.HOURS.toMinutes(hours)
    val seconds: Long = TimeUnit.SECONDS.toSeconds(this) -
            TimeUnit.DAYS.toSeconds(day) -
            TimeUnit.HOURS.toSeconds(hours) -
            TimeUnit.MINUTES.toSeconds(minutes)
    return when {
        day != 0L -> "$day days"
        hours != 0L -> "$hours hours"
        minutes != 0L -> "$minutes minutes"
        else -> "$seconds seconds"
    }
}