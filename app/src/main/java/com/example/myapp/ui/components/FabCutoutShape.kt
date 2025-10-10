package com.example.myapp.ui.components

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private val FabDiameter = 56.dp
private val CutoutMargin = 8.dp

class FabCutoutShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val fabDiameterPx = with(density) { FabDiameter.toPx() }
            val cutoutMarginPx = with(density) { CutoutMargin.toPx() }

            val cutoutRadius = fabDiameterPx / 2f + cutoutMarginPx
            val cutoutCenterY = 0f

            val circleStartX = size.width / 2f - cutoutRadius
            val circleEndX = size.width / 2f + cutoutRadius

            moveTo(0f, cutoutCenterY)

            lineTo(circleStartX, cutoutCenterY)

            arcTo(
                rect = Rect(
                    left = circleStartX,
                    top = cutoutCenterY - cutoutRadius,
                    right = circleEndX,
                    bottom = cutoutCenterY + cutoutRadius
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )

            lineTo(size.width, cutoutCenterY)

            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}