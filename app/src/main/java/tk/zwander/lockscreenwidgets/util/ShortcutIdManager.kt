package tk.zwander.lockscreenwidgets.util

import android.annotation.SuppressLint
import android.content.Context
import tk.zwander.lockscreenwidgets.host.widgetHostCompat
import kotlin.random.Random

val Context.shortcutIdManager: ShortcutIdManager
    get() = ShortcutIdManager.getInstance(this)

class ShortcutIdManager private constructor(private val context: Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: ShortcutIdManager? = null

        fun getInstance(context: Context): ShortcutIdManager {
            if (instance == null) instance = ShortcutIdManager(context.safeApplicationContext)
            return instance!!
        }
    }

    private val host by lazy { context.widgetHostCompat }

    @SuppressLint("NewApi")
    fun allocateShortcutId(): Int {
        val current = context.prefManager.shortcutIds

        val random = Random(System.currentTimeMillis())
        var id = random.nextInt()

        //AppWidgetHost.appWidgetIds has existed since at least 5.1.1, just hidden
        while (current.contains(id.toString()) && (host.appWidgetIds?.contains(id) == true)) {
            id = random.nextInt()
        }

        context.prefManager.shortcutIds = current.apply { add(id.toString()) }

        return id
    }

    fun removeShortcutId(id: Int) {
        context.prefManager.shortcutIds = context.prefManager.shortcutIds.apply { remove(id.toString()) }
    }
}