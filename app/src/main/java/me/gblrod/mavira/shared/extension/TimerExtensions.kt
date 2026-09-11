package me.gblrod.mavira.shared.extension

fun Long.toTimerFormat(): String {
    val minutes = this / 60
    val seconds = this % 60

    return "%02d:%02d".format(minutes, seconds)
}