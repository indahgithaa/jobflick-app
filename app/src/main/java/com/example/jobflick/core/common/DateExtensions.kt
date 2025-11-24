package com.example.jobflick.core.common

/**
 * Mengubah jumlah hari menjadi label "Hari ini" / "1 hari lalu" / "X hari lalu".
 * Contoh:
 *  0 -> "Hari ini"
 *  1 -> "1 hari lalu"
 *  5 -> "5 hari lalu"
 * null -> "-"
 */
fun Int?.toDaysAgoLabel(): String = when (this) {
    null -> "-"
    0 -> "Hari ini"
    1 -> "1 hari lalu"
    else -> "$this hari lalu"
}
