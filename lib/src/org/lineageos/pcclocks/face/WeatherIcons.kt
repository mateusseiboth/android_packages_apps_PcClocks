// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.face

import android.graphics.Canvas
import android.graphics.Paint
import org.lineageos.pcclocks.model.WeatherIconKind

/** Line icons for the weather face, drawn in a square of side [size] centered on (cx, cy). */
object WeatherIcons {
    private val DRAWERS: Map<WeatherIconKind, (Canvas, Float, Float, Float, Paint) -> Unit> =
        mapOf(
            WeatherIconKind.SUN to ::drawSun,
            WeatherIconKind.MOON to ::drawMoon,
            WeatherIconKind.CLOUD to ::drawCloud,
            WeatherIconKind.FOG to ::drawFog,
            WeatherIconKind.RAIN to ::drawRain,
            WeatherIconKind.STORM to ::drawStorm,
            WeatherIconKind.SNOW to ::drawSnow,
            WeatherIconKind.WIND to ::drawWind,
        )

    fun draw(
        canvas: Canvas,
        kind: WeatherIconKind,
        cx: Float,
        cy: Float,
        size: Float,
        paint: Paint,
    ) {
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = size * 0.07f
        DRAWERS.getValue(kind)(canvas, cx, cy, size, paint)
    }

    private fun drawSun(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        canvas.drawCircle(cx, cy, size * 0.2f, paint)
        (0 until 8).forEach { ray ->
            canvas.save()
            canvas.rotate(ray * 45f, cx, cy)
            canvas.drawLine(cx, cy - size * 0.33f, cx, cy - size * 0.45f, paint)
            canvas.restore()
        }
    }

    private fun drawMoon(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        val radius = size * 0.38f
        canvas.drawArc(cx - radius, cy - radius, cx + radius, cy + radius, 60f, 240f, false, paint)
        val inner = radius * 0.8f
        val shift = radius * 0.45f
        canvas.drawArc(
            cx + shift - inner,
            cy - shift - inner,
            cx + shift + inner,
            cy - shift + inner,
            105f,
            150f,
            false,
            paint,
        )
    }

    private fun drawCloud(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        val base = cy + size * 0.15f
        canvas.drawArc(
            cx - size * 0.42f,
            base - size * 0.3f,
            cx - size * 0.02f,
            base + size * 0.1f,
            90f,
            180f,
            false,
            paint,
        )
        canvas.drawArc(
            cx - size * 0.2f,
            base - size * 0.45f,
            cx + size * 0.3f,
            base + size * 0.05f,
            180f,
            180f,
            false,
            paint,
        )
        canvas.drawArc(
            cx + size * 0.08f,
            base - size * 0.28f,
            cx + size * 0.44f,
            base + size * 0.08f,
            270f,
            180f,
            false,
            paint,
        )
        canvas.drawLine(
            cx - size * 0.22f,
            base + size * 0.1f,
            cx + size * 0.26f,
            base + size * 0.08f,
            paint,
        )
    }

    private fun drawFog(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        listOf(-0.25f, 0f, 0.25f).forEachIndexed { index, offset ->
            val inset = size * 0.08f * (index % 2)
            val y = cy + size * offset
            canvas.drawLine(cx - size * 0.4f + inset, y, cx + size * 0.4f - inset, y, paint)
        }
    }

    private fun drawRain(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        drawCloud(canvas, cx, cy - size * 0.15f, size, paint)
        listOf(-0.2f, 0f, 0.2f).forEach { offset ->
            val x = cx + size * offset
            canvas.drawLine(x, cy + size * 0.22f, x - size * 0.06f, cy + size * 0.4f, paint)
        }
    }

    private fun drawStorm(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        drawCloud(canvas, cx, cy - size * 0.15f, size, paint)
        canvas.drawLine(
            cx + size * 0.05f,
            cy + size * 0.12f,
            cx - size * 0.08f,
            cy + size * 0.3f,
            paint,
        )
        canvas.drawLine(
            cx - size * 0.08f,
            cy + size * 0.3f,
            cx + size * 0.06f,
            cy + size * 0.3f,
            paint,
        )
        canvas.drawLine(
            cx + size * 0.06f,
            cy + size * 0.3f,
            cx - size * 0.06f,
            cy + size * 0.46f,
            paint,
        )
    }

    private fun drawSnow(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        (0 until 3).forEach { arm ->
            canvas.save()
            canvas.rotate(arm * 60f, cx, cy)
            canvas.drawLine(cx, cy - size * 0.4f, cx, cy + size * 0.4f, paint)
            canvas.restore()
        }
    }

    private fun drawWind(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        listOf(-0.18f to 0.32f, 0f to 0.42f, 0.18f to 0.24f).forEach { (offset, length) ->
            val y = cy + size * offset
            canvas.drawLine(cx - size * 0.42f, y, cx - size * 0.42f + size * length * 2, y, paint)
        }
    }
}
