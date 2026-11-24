// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.face

import android.graphics.Canvas
import android.util.SizeF

/** Draws one clock style; the view around it only measures and invalidates. */
interface ClockPainter {

    /** The size of the face for the requested font size. */
    fun measure(fontSizePx: Float): SizeF

    fun draw(canvas: Canvas, state: FaceState)
}
