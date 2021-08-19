package tk.zwander.lockscreenwidgets.data

import android.content.ComponentName
import android.content.Intent
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Persistent data for a widget added to the frame.
 *
 * @property id the ID of the widget
 */
@Parcelize
open class WidgetData(
    val id: Int,
    val type: WidgetType? = WidgetType.WIDGET,
    var label: String?,
    var icon: String?,
    var iconRes: Intent.ShortcutIconResource?,
    var shortcutIntent: Intent?,
    var widgetProvider: String?
) : Parcelable {
    companion object {
        fun shortcut(
            id: Int,
            label: String,
            icon: String?,
            iconRes: Intent.ShortcutIconResource?,
            shortcutIntent: Intent
        ): WidgetData {
            return WidgetData(
                id, WidgetType.SHORTCUT,
                label, icon, iconRes, shortcutIntent,
                null
            )
        }

        fun widget(
            id: Int,
            widgetProvider: ComponentName,
            label: String,
            icon: String?
        ): WidgetData {
            return WidgetData(id, WidgetType.WIDGET, label, icon,
                null, null, widgetProvider.flattenToString())
        }
    }

    val safeType: WidgetType
        get() = type ?: WidgetType.WIDGET

    val widgetProviderComponent: ComponentName?
        get() = widgetProvider?.let { ComponentName.unflattenFromString(it) }

    override fun equals(other: Any?): Boolean {
        return other is WidgetData && other.id == id
                && other.safeType == safeType
                && (safeType != WidgetType.WIDGET || widgetProviderComponent == other.widgetProviderComponent)
    }

    override fun hashCode(): Int {
        return if (safeType != WidgetType.WIDGET) {
            Objects.hash(id, safeType)
        } else {
            Objects.hash(id, safeType, widgetProviderComponent)
        }
    }
}

enum class WidgetType {
    WIDGET,
    SHORTCUT
}