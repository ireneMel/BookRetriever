package com.example.bookretriever

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.withScale
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.google.android.material.math.MathUtils.lerp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class UIState {
    object Unlike : UIState()
    object Like : UIState()
    object Animate : UIState()
}

class LikeAnimationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyle) {

    private val _uiState = MutableStateFlow<UIState>(UIState.Unlike)
    var uiState = _uiState.asStateFlow()

    private var fraction: Float = 0f // from 0 to 1

    private val iconBitmap = VectorDrawableCompat.create(
        resources,
        R.drawable.ic_favorite_pressed,
        context.theme
    )?.toBitmap()!!

    private val drawableCanvas = Canvas(iconBitmap)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
    }

    private val srcInMode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private var fromBmColor: Int = Color.parseColor("#373c68")
    private var toBmColor: Int = Color.parseColor("#f6507d")

    private val argbEvaluator =
        ArgbEvaluator() //performs type interpolation between integer values that represent ARGB colors

    private fun getBitmapPaint() = paint.apply {
        val bgFraction = (fraction * 2).coerceAtMost(1f)
        color = argbEvaluator.evaluate(bgFraction, fromBmColor, toBmColor) as Int
    }

    private fun tintBitmap() {
        // change to src in
        paint.xfermode = srcInMode
        drawableCanvas.drawRect(
            0f,
            0f,
            this.iconBitmap.width.toFloat(),
            this.iconBitmap.height.toFloat(),
            getBitmapPaint()
        )
        paint.xfermode = null
    }

    private fun updateColor(uiState: UIState) {
        toBmColor = if (uiState == UIState.Unlike) {
            Color.parseColor("#373c68")
        } else Color.parseColor("#f6507d")

        fromBmColor = if (_uiState.value == UIState.Unlike) {
            Color.parseColor("#373c68")
        } else Color.parseColor("#f6507d")

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //draw heart
        getBitmapPaint().also { paint ->
            val scaleX = lerp(1f, 0.6f, 1f)

            canvas.withScale(scaleX, scaleX, pivotX, pivotY) {
                val left = width / 2f - iconBitmap.width / 2f
                val top = height / 2f - iconBitmap.height / 2f
                tintBitmap()
                canvas.drawBitmap(iconBitmap, left, top, paint)
            }
        }
    }


    fun setUIState(uiState: UIState, isAnim: Boolean) {
        updateColor(uiState)
        if (_uiState.value == UIState.Animate) return
        if (isAnim) {
            runAnimation().apply {
                doOnEnd {
                    this@LikeAnimationView._uiState.value = uiState
                }
            }
        } else {
            _uiState.value = uiState
            fraction = 1f
            invalidate()
        }
    }

    private fun runAnimation(): ValueAnimator {
        return ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                fraction = it.animatedValue as Float
                invalidate()
            }
            interpolator = FastOutSlowInInterpolator()
            doOnStart {
                this@LikeAnimationView._uiState.value = UIState.Animate
            }
            duration = 500L
            start()
        }
    }
}