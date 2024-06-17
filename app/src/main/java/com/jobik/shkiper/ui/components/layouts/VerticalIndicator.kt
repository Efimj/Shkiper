package com.jobik.shkiper.ui.components.layouts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jobik.shkiper.ui.theme.AppTheme

@Composable
fun VerticalIndicator(
    pagerState: PagerState,
    color: Color = AppTheme.colors.secondaryContainer,
    activeColor: Color = AppTheme.colors.primary,
    spacing: Dp = 15.dp,
    dotWidth: Dp = 8.dp,
    dotHeight: Dp = 25.dp,
    dotActiveHeight: Dp = 58.dp,
) {
    // To get scroll offset
    val pageOffset = pagerState.currentPage + pagerState.currentPageOffsetFraction

    Canvas(modifier = Modifier) {
        val spacing = spacing.toPx()
        val dotWidth = dotWidth.toPx()
        val dotHeight = dotHeight.toPx()

        val activeDotHeight = dotActiveHeight.toPx()
        var y = 0f
        val x = center.x

        repeat(pagerState.pageCount) { i ->
            val posOffset = pageOffset
            val dotOffset = posOffset % 1
            val current = posOffset.toInt()

            val factor = (dotOffset * (activeDotHeight - dotHeight))

            val calculatedHeight = when {
                i == current -> activeDotHeight - factor
                i - 1 == current || (i == 0 && posOffset > pagerState.pageCount - 1) -> dotHeight + factor
                else -> dotHeight
            }

            val currentColor = lerp(color, activeColor, calculatedHeight / activeDotHeight)

            drawIndicator(
                x = x,
                y = y,
                width = dotWidth,
                height = calculatedHeight,
                radius = CornerRadius(2000f),
                color = currentColor
            )
            y += calculatedHeight + spacing
        }
    }
}

private fun DrawScope.drawIndicator(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    radius: CornerRadius,
    color: Color
) {
    val rect = RoundRect(
        left = x - width / 2,
        top = y,
        right = x + width / 2,
        bottom = y + height,
        cornerRadius = radius
    )
    val path = Path().apply { addRoundRect(rect) }
    drawPath(path = path, color = color)
}