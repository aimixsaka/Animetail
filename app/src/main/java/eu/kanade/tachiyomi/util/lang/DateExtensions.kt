package eu.kanade.tachiyomi.util.lang

import android.content.Context
import eu.kanade.tachiyomi.R
import tachiyomi.core.common.i18n.pluralStringResource
import tachiyomi.core.common.i18n.stringResource
import tachiyomi.i18n.MR
import java.text.DateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.Date
import kotlin.math.absoluteValue

fun LocalDateTime.toDateTimestampString(dateTimeFormatter: DateTimeFormatter): String {
    val date = dateTimeFormatter.format(this)
    val time = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(this)
    return "$date $time"
}

fun Date.toTimestampString(): String {
    return DateFormat.getTimeInstance(DateFormat.SHORT).format(this)
}

fun Long.convertEpochMillisZone(
    from: ZoneId,
    to: ZoneId,
): Long {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), from)
        .atZone(to)
        .toInstant()
        .toEpochMilli()
}

fun Long.toLocalDate(): LocalDate {
    return LocalDate.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}

fun Instant.toLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
    return LocalDate.ofInstant(this, zoneId)
}

fun Instant.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return LocalDateTime.ofInstant(this, zoneId)
}

fun LocalDate.toRelativeString(
    context: Context,
    relative: Boolean = true,
    dateFormat: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT),
): String {
    if (!relative) {
        return dateFormat.format(this)
    }
    val now = LocalDate.now()
    val difference = ChronoUnit.DAYS.between(this, now)
    return when {
        difference < -7 -> dateFormat.format(this)
        difference < 0 -> context.pluralStringResource(
            MR.plurals.upcoming_relative_time,
            difference.toInt().absoluteValue,
            difference.toInt().absoluteValue,
        )
        difference < 1 -> context.stringResource(MR.strings.relative_time_today)
        difference < 7 -> context.resources.getQuantityString(
            R.plurals.relative_time,
            difference.toInt(),
            difference.toInt(),
        )
        else -> dateFormat.format(this)
    }
}

fun LocalDateTime.toRelativeString(
    context: Context,
    relative: Boolean = true,
    dateFormat: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT),
): String {
    if (!relative) {
        return dateFormat.format(this)
    }
    val now = LocalDateTime.now()
    val difference = ChronoUnit.DAYS.between(this, now)
    return when {
        difference < -7 -> dateFormat.format(this)
        difference < -1 -> context.pluralStringResource(
            MR.plurals.upcoming_relative_time,
            difference.toInt().absoluteValue,
            difference.toInt().absoluteValue,
        )
        difference < 0 -> {
            val hourDifference = ChronoUnit.HOURS.between(now, this)
            if (hourDifference < 1) {
                val minuteDifference = ChronoUnit.MINUTES.between(now, this)
                context.pluralStringResource(
                    MR.plurals.upcoming_relative_time_minutes,
                    minuteDifference.toInt(),
                    minuteDifference.toInt(),
                )
            } else {
                context.pluralStringResource(
                    MR.plurals.upcoming_relative_time_hours,
                    hourDifference.toInt(),
                    hourDifference.toInt(),
                )
            }
        }
        difference < 1 -> {
            val hourDifference = ChronoUnit.HOURS.between(this, now)
            if (hourDifference < 1) {
                val minuteDifference = ChronoUnit.MINUTES.between(this, now)
                context.pluralStringResource(
                    MR.plurals.relative_time_minutes,
                    minuteDifference.toInt(),
                    minuteDifference.toInt(),
                )
            } else {
                context.pluralStringResource(
                    MR.plurals.relative_time_hours,
                    hourDifference.toInt(),
                    hourDifference.toInt(),
                )
            }
        }
        difference < 7 -> context.resources.getQuantityString(
            R.plurals.relative_time,
            difference.toInt(),
            difference.toInt(),
        )
        else -> dateFormat.format(this)
    }
}
