// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks

import android.content.Context
import android.util.TypedValue
import com.android.systemui.customization.clocks.TimeKeeperImpl
import com.android.systemui.plugins.keyguard.ui.clocks.ClockConfig
import com.android.systemui.plugins.keyguard.ui.clocks.ClockController
import com.android.systemui.plugins.keyguard.ui.clocks.ClockMessageBuffers
import com.android.systemui.plugins.keyguard.ui.clocks.ClockMetadata
import com.android.systemui.plugins.keyguard.ui.clocks.ClockPickerConfig
import com.android.systemui.plugins.keyguard.ui.clocks.ClockProviderPlugin
import com.android.systemui.plugins.keyguard.ui.clocks.ClockSettings
import org.lineageos.pcclocks.face.ClockPainters
import org.lineageos.pcclocks.face.ThumbnailDrawable
import org.lineageos.pcclocks.model.ClockStyle

/** Provides the one clock of [style]; each clock APK declares a subclass as its plugin service. */
abstract class PcClockProvider(
    private val style: ClockStyle,
    private val nameRes: Int,
    private val descriptionRes: Int,
) : ClockProviderPlugin {
    private lateinit var hostCtx: Context
    private lateinit var pluginCtx: Context

    override fun onCreate(hostContext: Context, pluginContext: Context) {
        hostCtx = hostContext
        pluginCtx = pluginContext
    }

    override fun initialize(buffers: ClockMessageBuffers?) {}

    override fun getClocks(): List<ClockMetadata> = listOf(ClockMetadata(style.clockId))

    override fun createClock(ctx: Context, settings: ClockSettings): ClockController? =
        settings.clockId
            ?.takeIf { it == style.clockId }
            ?.let { PcClockController(ctx, pluginCtx, style, settings, config(), TimeKeeperImpl()) }

    override fun getClockPickerConfig(settings: ClockSettings) =
        ClockPickerConfig(
            id = style.clockId,
            name = pluginCtx.getString(nameRes),
            description = pluginCtx.getString(descriptionRes),
            thumbnail = ThumbnailDrawable(ClockPainters.of(style).large(), thumbnailSizePx()),
        )

    private fun config() =
        ClockConfig(
            style.clockId,
            pluginCtx.getString(nameRes),
            pluginCtx.getString(descriptionRes),
        )

    private fun thumbnailSizePx() =
        TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                THUMBNAIL_SIZE_DP,
                pluginCtx.resources.displayMetrics,
            )
            .toInt()

    private companion object {
        const val THUMBNAIL_SIZE_DP = 72f
    }
}
