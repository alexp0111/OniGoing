package ru.alexp0111.onigoing.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.alexp0111.onigoing.R
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

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.snack(message: String) {
    Snackbar.make(
        requireView(),
        message,
        Snackbar.LENGTH_LONG,
    ).setBackgroundTint(resources.getColor(R.color.dark, requireActivity().theme))
        .setTextColor(resources.getColor(R.color.white, requireActivity().theme))
        .show()
}