package com.example.jobflick.core.common

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import kotlin.math.floor

@RequiresApi(Build.VERSION_CODES.O)
fun String?.toTimeAgoLabel(): String {
    if (this.isNullOrBlank()) return "-"

    return try {
        val time = OffsetDateTime.parse(this)
        val now = OffsetDateTime.now()

        val duration = Duration.between(time, now)
        val seconds = duration.seconds.coerceAtLeast(0)

        // tingkat dasar
        val minutes = seconds / 60
        val hours = seconds / 3600
        val days = seconds / 86400

        // tingkat menengah
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        when {
            seconds < 60 -> "$seconds detik lalu"
            minutes < 60 -> "$minutes menit lalu"
            hours < 24 -> "$hours jam lalu"
            days < 7 -> "$days hari lalu"
            weeks < 5 -> "$weeks minggu lalu"
            months < 12 -> "$months bulan lalu"
            else -> "$years tahun lalu"
        }
    } catch (e: DateTimeParseException) {
        "-"
    }
}
