package me.gblrod.mavira.features.statistics.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import me.gblrod.mavira.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun Long.toStatisticsDate(): String {
    val locale = LocalConfiguration.current.locales[0]

    val date = Instant
        .ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return when (date) {
        LocalDate.now() -> stringResource(id = R.string.statistics_today)
        LocalDate.now().minusDays(1) -> stringResource(id = R.string.statistics_yesterday)
        else -> DateTimeFormatter.ofPattern("dd MMM", locale).format(date)
    }
}

@Composable
fun Long.toStatisticsTime(): String {
    val locale = LocalConfiguration.current.locales[0]

    val formatter = DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(locale)
        .withZone(ZoneId.systemDefault())

    return formatter.format(Instant.ofEpochMilli(this))
}