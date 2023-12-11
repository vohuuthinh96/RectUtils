package com.example.myapplication

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.values

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<CtView>(R.id.ctview)
        val left = findViewById<Button>(R.id.left)
        val right = findViewById<Button>(R.id.right)
        val revese = findViewById<Button>(R.id.revese)

        var fullRect = Rect(10, 10, 200, 400)
        var partialRect = Rect(10, 50, 150, 380)
        view.fullRect.set(fullRect)
        view.partialRect.set(partialRect)
        view.setIn()
        left.setOnClickListener {
           rotateRectForOrientation(-90, fullRect, partialRect)
            view.fullRect.set(fullRect)
            view.partialRect.set(partialRect)
            view.setIn()
        }

        right.setOnClickListener {
            rotateRectForOrientation(90, fullRect, partialRect)
            view.fullRect.set(fullRect)
            view.partialRect.set(partialRect)
            Log.d("thinhvh1", "output: $fullRect")
            view.setIn()
        }

        revese.setOnClickListener {
           rotateRectForOrientation(180, fullRect, partialRect)
            view.fullRect.set(fullRect)
            view.partialRect.set(partialRect)
            view.setIn()
        }
    }

    fun rotateRectForOrientation(
        orientation: Int, fullRect: Rect,
        partialRect: Rect
    ) {

        val matrix = Matrix()
        // Exif orientation specifies how the camera is rotated relative to the actual subject.
        // First rotate in the opposite direction.
        matrix.setRotate(-orientation.toFloat())
        val fullRectF = RectF(fullRect)
        val partialRectF = RectF(partialRect)
        matrix.mapRect(fullRectF)
        matrix.mapRect(partialRectF)
        // Then translate so that the upper left corner of the rotated full rect is at (0,0).
        matrix.reset()
        matrix.setTranslate(-fullRectF.left, -fullRectF.top)
        Log.d("thinhvh", "rotateRectForOrientation: ${matrix.translationX}")
        Log.d("thinhvh", "rotateRectForOrientation: ${matrix.translationY}")
        matrix.postTranslate(fullRect.left.toFloat(), fullRect.top.toFloat())
        matrix.mapRect(fullRectF)
        matrix.mapRect(partialRectF)
        // Orientation transformation is complete.
        fullRect[fullRectF.left.toInt(), fullRectF.top.toInt(), fullRectF.right.toInt()] =
            fullRectF.bottom.toInt()
        partialRect[partialRectF.left.toInt(), partialRectF.top.toInt(), partialRectF.right.toInt()] =
            partialRectF.bottom.toInt()
    }
}

class CtView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    var fullRect = Rect()
    var partialRect = Rect()

    fun setIn() {
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(Rect(0, 0, measuredWidth, measuredHeight), Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
        })

        canvas?.drawRect(fullRect, Paint().apply {
            color = Color.RED
            style = Paint.Style.FILL
        })

        canvas?.drawRect(partialRect, Paint().apply {
            color = Color.GREEN
            style = Paint.Style.FILL
        })
    }
}

val Matrix.translationX: Float
    get() { return values()[Matrix.MTRANS_X] }

val Matrix.translationY: Float
    get() { return values()[Matrix.MTRANS_Y] }