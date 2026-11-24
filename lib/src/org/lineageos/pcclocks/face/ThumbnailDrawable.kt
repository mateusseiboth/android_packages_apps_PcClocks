// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.face

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import java.time.LocalDate
import java.util.Locale
import kotlin.math.min
import org.lineageos.pcclocks.model.ClockTime
import org.lineageos.pcclocks.model.TonePalette
import org.lineageos.pcclocks.model.WeatherIconKind
import org.lineageos.pcclocks.model.WeatherText

/**
 * The picker's thumbnail of a style: the style's own large face at a fixed time, in white tones,
 * scaled into the bounds. Tints like a vector icon.
 */
class ThumbnailDrawable(private val painter: ClockPainter, private val sizePx: Int) : Drawable() {
    private val layer = Paint()
    private val state =
        FaceState(
            time = ClockTime(hourOfDay = 10, minute = 38, second = 0, is24Hour = true),
            date = LocalDate.of(2026, 8, 17),
            locale = Locale.getDefault(),
            palette = TonePalette(WHITE_TONES),
            fontSizePx = FONT_SIZE,
            weather = WeatherInfo(WeatherIconKind.SUN, WeatherText.temperature(21)),
        )

    override fun draw(canvas: Canvas) {
        val face = painter.measure(FONT_SIZE)
        val scale = min(bounds.width() / face.width, bounds.height() / face.height)
        val count =
            canvas.saveLayer(
                bounds.left.toFloat(),
                bounds.top.toFloat(),
                bounds.right.toFloat(),
                bounds.bottom.toFloat(),
                layer,
            )
        canvas.translate(
            bounds.exactCenterX() - face.width * scale / 2,
            bounds.exactCenterY() - face.height * scale / 2,
        )
        canvas.scale(scale, scale)
        painter.draw(canvas, state)
        canvas.restoreToCount(count)
    }

    override fun getIntrinsicWidth() = sizePx

    override fun getIntrinsicHeight() = sizePx

    override fun setAlpha(alpha: Int) {
        layer.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        layer.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun setTintList(tint: ColorStateList?) {
        setColorFilter(tint?.let { PorterDuffColorFilter(it.defaultColor, PorterDuff.Mode.SRC_IN) })
    }

    @Deprecated("Deprecated in Java") override fun getOpacity() = PixelFormat.TRANSLUCENT

    private companion object {
        const val FONT_SIZE = 200f

        val WHITE_TONES = listOf(0xFFFFFFFF, 0xB3FFFFFF, 0x80FFFFFF, 0x59FFFFFF).map { it.toInt() }
    }
}
