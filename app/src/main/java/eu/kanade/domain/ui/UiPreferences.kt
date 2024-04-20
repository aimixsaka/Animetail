package eu.kanade.domain.ui

import android.os.Build
import androidx.compose.ui.graphics.toArgb
import eu.kanade.domain.ui.model.AppTheme
import eu.kanade.domain.ui.model.NavStyle
import eu.kanade.domain.ui.model.StartScreen
import eu.kanade.domain.ui.model.TabletUiMode
import eu.kanade.domain.ui.model.ThemeMode
import eu.kanade.presentation.theme.colorscheme.BaseColorScheme
import eu.kanade.presentation.theme.colorscheme.CloudflareColorScheme
import eu.kanade.presentation.theme.colorscheme.TachiyomiColorScheme
import eu.kanade.tachiyomi.util.system.DeviceUtil
import eu.kanade.tachiyomi.util.system.isDynamicColorAvailable
import tachiyomi.core.preference.PreferenceStore
import tachiyomi.core.preference.getEnum
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

class UiPreferences(
    private val preferenceStore: PreferenceStore,
) {

    fun themeMode() = preferenceStore.getEnum(
        "pref_theme_mode_key",
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { ThemeMode.SYSTEM } else { ThemeMode.LIGHT },
    )

    fun appTheme() = preferenceStore.getEnum(
        "pref_app_theme",
        if (DeviceUtil.isDynamicColorAvailable) { AppTheme.MONET } else { AppTheme.DEFAULT },
    )

    fun colorTheme() = preferenceStore.getInt(
        "pref_color-theme",
        if(themeMode().get() == ThemeMode.DARK) { TachiyomiColorScheme.darkScheme.primary.toArgb() } else {TachiyomiColorScheme.lightScheme.primary.toArgb() }
    )

    fun colorPickerCoordinates() = preferenceStore.getString(
        "color_picker_coordinates",
        ""
    )

    fun themeDarkAmoled() = preferenceStore.getBoolean("pref_theme_dark_amoled_key", false)

    fun relativeTime() = preferenceStore.getBoolean("relative_time_v2", true)

    fun dateFormat() = preferenceStore.getString("app_date_format", "")

    fun tabletUiMode() = preferenceStore.getEnum("tablet_ui_mode", TabletUiMode.AUTOMATIC)

    fun startScreen() = preferenceStore.getEnum("start_screen", StartScreen.ANIME)

    fun navStyle() = preferenceStore.getEnum("bottom_rail_nav_style", NavStyle.MOVE_HISTORY_TO_MORE)

    companion object {
        fun dateFormat(format: String): DateFormat = when (format) {
            "" -> DateFormat.getDateInstance(DateFormat.SHORT)
            else -> SimpleDateFormat(format, Locale.getDefault())
        }
    }
}
