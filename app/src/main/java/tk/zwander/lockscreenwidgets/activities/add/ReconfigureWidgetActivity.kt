package tk.zwander.lockscreenwidgets.activities.add

import android.annotation.SuppressLint
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.graphics.drawable.toBitmap
import tk.zwander.lockscreenwidgets.activities.DismissOrUnlockActivity
import tk.zwander.lockscreenwidgets.data.WidgetData
import tk.zwander.lockscreenwidgets.util.prefManager
import tk.zwander.lockscreenwidgets.util.toBase64

class ReconfigureWidgetActivity : BaseBindWidgetActivity() {
    companion object {
        const val EXTRA_PREVIOUS_ID = "previous_id"
        const val EXTRA_PROVIDER_INFO = "provider_info"

        fun launch(context: Context, id: Int, providerInfo: AppWidgetProviderInfo) {
            val intent = Intent(context, ReconfigureWidgetActivity::class.java)

            intent.putExtra(EXTRA_PREVIOUS_ID, id)
            intent.putExtra(EXTRA_PROVIDER_INFO, providerInfo)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)
        }
    }

    private val prevId by lazy { intent.getIntExtra(EXTRA_PREVIOUS_ID, -1) }
    private val providerInfo by lazy { intent.getParcelableExtra(EXTRA_PROVIDER_INFO) as AppWidgetProviderInfo? }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (prevId == -1 || providerInfo == null) {
            finish()
            return
        }

        tryBindWidget(providerInfo!!, widgetHost.allocateAppWidgetId())
    }

    @SuppressLint("NewApi")
    override fun tryBindWidget(info: AppWidgetProviderInfo, id: Int) {
        val canBind = appWidgetManager.bindAppWidgetIdIfAllowed(id, info.provider)

        if (!canBind) getWidgetPermission(id, info.provider)
        else {
            if (info.configure != null) {
                configureWidget(id, info)
            } else {
                addNewWidget(id, info)
            }
        }
    }

    override fun configureWidget(id: Int, provider: AppWidgetProviderInfo) {
        DismissOrUnlockActivity.launch(this, false)
        super.configureWidget(id, provider)
    }

    override fun addNewWidget(id: Int, provider: AppWidgetProviderInfo) {
        val widget = WidgetData.widget(
            id,
            provider.provider,
            provider.loadLabel(packageManager),
            provider.loadPreviewImage(this, 0).toBitmap().toBase64()
        )
        val newSet = prefManager.currentWidgets.toMutableList()

        val oldWidgetIndex = newSet.indexOf(WidgetData.widget(prevId, provider.provider, "", null))

        newSet.removeAt(oldWidgetIndex)
        newSet.add(oldWidgetIndex, widget)

        prefManager.currentWidgets = LinkedHashSet(newSet)

        finish()
    }
}