package com.simple.mycloudmusic.border

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.graphics.transform
import com.simple.mycloudmusic.R

/**
 * @description 单一子布局周围选中轮廓的控件
 * @author simple.wu
 * @date 2022/11/29
 */
class BorderLayout : FrameLayout {
    companion object {
        private const val TAG = "BorderLayout"

        //支持的子view数量
        private const val BORDER_CRITICAL_COUNT = 1

        //默认边框宽度
        private const val BORDER_DEFAULT_WIDTH = 1.5f

        //默认内边距
        private const val BORDER_DEFAULT_PADDING = 3f

        //默认边框与子view间隔
        private const val BORDER_DEFAULT_INTERVAL = 4f

        //动画加载单位时间，该值不建议改动，如需改动只可改小，不可改大，人眼的反应时间为0.1s，超过0.1s将会出现卡顿
        private const val BORDER_ANIMATION_INTERVAL_TIME = 100L

        //单个周期的动画时间，如scale动画扩展为一个周期，缩小为一个周期
        private const val BORDER_ANIMATION_DEFAULT_TIME = 1000
    }

    private lateinit var mPaint: Paint
    private lateinit var mPointPaint: Paint

    /**
     * 横向padding
     */
    private var horizontalPadding = 0

    /**
     * 纵向padding
     */
    private var verticalPadding = 0

    /**
     * 边框颜色
     */
    private var borderColor = Color.RED

    /**
     * 边框宽度
     */
    private var borderWidth = 0f

    /**
     * 边框和子布局之间间隔
     */
    private var interval = 0f

    /**
     * 圆角矩形的X,Y弧度
     */
    private var leftTopRadiusX = 0f
    private var leftTopRadiusY = 0f
    private var rightTopRadiusX = 0f
    private var rightTopRadiusY = 0f
    private var leftBottomRadiusX = 0f
    private var leftBottomRadiusY = 0f
    private var rightBottomRadiusX = 0f
    private var rightBottomRadiusY = 0f

    /**
     * 当前缩放比例
     */
    private var currentScaleX = 0f
    private var currentScaleY = 0f

    /**
     * scale动画方向，BorderDirection.FORWARD表示正在向外扩展，BorderDirection.BACKWARD表示正在向内缩放
     */
    private var animationDirection = BorderDirection.FORWARD

    /**
     * 动画类型，默认为NONE
     */
    private var animationMode = BorderAnimationMode.NONE

    /**
     * 动画时间，毫秒为单位
     */
    private var animationFrequency = 10

    private var mHandler: Handler = MyHandler(Looper.getMainLooper())

    /**
     * 是否需要自定义radius，而非使用背景默认的radius
     */
    private var needCustomRadius = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context, attrs, 0
    ) {
        if (isInEditMode) return
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context, attrs, defStyleAttr, 0
    )

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(
        context, attrs, defStyleAttr, defStyleRes
    ) {
        val resourceValue = context.obtainStyledAttributes(attrs, R.styleable.BorderLayout)
        initAttributes(resourceValue)
        initPaint()
        resourceValue.recycle()
    }

    inner class MyHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.d(TAG, "handleMessage: ${System.currentTimeMillis()}")
            postInvalidate()
        }
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    /**
     * 初始化属性
     */
    private fun initAttributes(resourceValue: TypedArray) {
        leftTopRadiusX = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_left_top_radius_x, 0f),
            context.resources.displayMetrics
        )
        leftTopRadiusY = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_left_top_radius_y, 0f),
            context.resources.displayMetrics
        )
        rightTopRadiusX = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_right_top_radius_x, 0f),
            context.resources.displayMetrics
        )
        rightTopRadiusY = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_right_top_radius_y, 0f),
            context.resources.displayMetrics
        )
        leftBottomRadiusX = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_left_bottom_radius_x, 0f),
            context.resources.displayMetrics
        )
        leftBottomRadiusY = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_left_bottom_radius_y, 0f),
            context.resources.displayMetrics
        )
        rightBottomRadiusX = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_right_bottom_radius_x, 0f),
            context.resources.displayMetrics
        )
        rightBottomRadiusY = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_right_bottom_radius_y, 0f),
            context.resources.displayMetrics
        )
        //判断是否需要特殊处理圆角矩形的边角
        needCustomRadius =
            (leftTopRadiusX > 0f && leftTopRadiusY > 0f) || (leftBottomRadiusX > 0f && leftBottomRadiusY > 0f) || (rightTopRadiusX > 0f && rightTopRadiusY > 0f) || (rightBottomRadiusX > 0f && rightBottomRadiusY > 0f)
        verticalPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_vertical_padding, BORDER_DEFAULT_PADDING),
            context.resources.displayMetrics
        ).toInt()
        horizontalPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_horizontal_padding, BORDER_DEFAULT_PADDING),
            context.resources.displayMetrics
        ).toInt()
        interval = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_interval, BORDER_DEFAULT_INTERVAL),
            context.resources.displayMetrics
        )
        //此处是为了保证intervalWidth最小为3，为了给相关边框和子布局的背景之间留白，不至于挤压在一起，可以通过leave_blank属性来控制是否留白
        borderColor = resourceValue.getColor(R.styleable.BorderLayout_border_color, Color.RED)
        borderWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_width, BORDER_DEFAULT_WIDTH),
            context.resources.displayMetrics
        )
        //默认动画效果为NONE
        animationMode = resourceValue.getString(R.styleable.BorderLayout_border_animation_mode) ?: BorderAnimationMode.NONE

        val animationTime = resourceValue.getInt(R.styleable.BorderLayout_border_animation_time, BORDER_ANIMATION_DEFAULT_TIME)
        animationFrequency = if (animationTime / BORDER_ANIMATION_INTERVAL_TIME <= 0L) {
            10
        } else {
            (animationTime / BORDER_ANIMATION_INTERVAL_TIME).toInt()
        }
        //修正边框宽度
        if (borderWidth <= 0f) {
            borderWidth = BORDER_DEFAULT_WIDTH
        }
        //修正横纵向padding，否则interval间隔属性不会生效
        if (interval > horizontalPadding) {
            horizontalPadding = interval.toInt()
        }
        if (interval > verticalPadding) {
            verticalPadding = interval.toInt()
        }
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
    }

    /**
     * 初始化画笔等
     */
    private fun initPaint() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = borderColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = borderWidth

        mPointPaint = Paint()
        mPointPaint.isAntiAlias = true
        mPointPaint.color = Color.BLACK
        mPointPaint.style = Paint.Style.FILL
        mPointPaint.strokeWidth = 10f
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        when {
            childCount == 0 -> {
                //do nothing
            }
            childCount == BORDER_CRITICAL_COUNT -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || !hasFocus()) {
                    return
                }
                drawBorder(canvas)
            }
            childCount > BORDER_CRITICAL_COUNT -> {
                throw java.lang.IllegalArgumentException("child view should be controlled in only one")
            }
        }
    }

    /**
     * 绘制边框
     */
    @SuppressLint("SoonBlockedPrivateApi")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun drawBorder(canvas: Canvas?) {
        val childView = getChildAt(0)
        //获取子模块的轮廓
        val outLine = Outline()
        childView.background.getOutline(outLine)
        //获取边框相对于子布局背景的比例
        val matrix = dispatchAnimationMode(canvas, childView)
        val rect = Rect()
        val rectMode = outLine.getRect(rect)
        Log.d(TAG, "drawBorder: rectMode is $rectMode, needCustomRadius is $needCustomRadius")
        when {
            rectMode && needCustomRadius -> {
                val rectF = handleRectCoordinate(rect, matrix)
                val borderWidth = rectF.right - rectF.left
                val borderHeight = rectF.bottom - rectF.top
                amendRadius(borderWidth, borderHeight)
                //从矩形左上方处顺时针绘制
                val tempPath = Path()
                tempPath.moveTo(rectF.left + leftTopRadiusX / 2, rectF.top)
                tempPath.lineTo(rectF.right - rightTopRadiusX / 2, rectF.top)
                tempPath.arcTo(rectF.right - rightTopRadiusX, rectF.top, rectF.right, rectF.top + rightTopRadiusY, 270f, 90f, true)
                tempPath.lineTo(rectF.right, rectF.bottom - rightBottomRadiusY / 2)
                tempPath.arcTo(rectF.right - rightBottomRadiusX, rectF.bottom - rightBottomRadiusY, rectF.right, rectF.bottom, 0f, 90f, true)
                tempPath.lineTo(rectF.left + leftBottomRadiusX / 2, rectF.bottom)
                tempPath.arcTo(rectF.left, rectF.bottom - leftBottomRadiusY, rectF.left + leftBottomRadiusX, rectF.bottom, 90f, 90f, true)
                tempPath.lineTo(rectF.left, rectF.top + leftTopRadiusY / 2)
                tempPath.arcTo(rectF.left, rectF.top, rectF.left + leftTopRadiusX, rectF.top + leftTopRadiusY, 180f, 90f, true)
                canvas?.drawPath(tempPath, mPaint)
            }
            rectMode && !needCustomRadius -> {
                //获取子模块的圆角半径
                val radius = outLine.radius
                val rectF = handleRectCoordinate(rect, matrix)
                canvas?.drawRoundRect(rectF, radius, radius, mPaint)
            }
            else -> {
                //反射获取path路径
                val pathField = outLine.javaClass.getDeclaredField("mPath")
                pathField.isAccessible = true
                val path = pathField.get(outLine) as? Path
                path?.let {
                    it.offset(horizontalPadding.toFloat(), verticalPadding.toFloat())
                    it.transform(matrix)
                    canvas?.drawPath(it, mPaint)
                }
            }
        }
        if (animationMode == BorderAnimationMode.NONE) return
        val msg = Message.obtain()
        msg.what = 1
        if (!mHandler.hasMessages(msg.what))
            mHandler.sendMessageDelayed(msg, BORDER_ANIMATION_INTERVAL_TIME)
    }

    /**
     * 根据动画属性值分发处理动画效果
     */
    private fun dispatchAnimationMode(canvas: Canvas?, childView: View): Matrix {
        //默认值取1，保证除法作为分母时报错
        val canvasWidth = canvas?.width?.toFloat() ?: 1f
        val canvasHeight = canvas?.height?.toFloat() ?: 1f
        val childWidth = childView.width
        val childHeight = childView.height
        val defaultScaleX = (interval + childWidth) / childWidth
        val defaultScaleY = (interval + childHeight) / childHeight
        return when (animationMode) {
            BorderAnimationMode.SCALE -> {
                handleScaleAnimationMode(canvasWidth, canvasHeight, childWidth, childHeight, defaultScaleX, defaultScaleY)
            }
            else -> {
                handleNoneAnimationMode(canvasWidth, canvasHeight, defaultScaleX, defaultScaleY)
            }
        }
    }

    /**
     * 处理scale动画
     */
    private fun handleScaleAnimationMode(
        canvasWidth: Float,
        canvasHeight: Float,
        childWidth: Int,
        childHeight: Int,
        defaultScaleX: Float,
        defaultScaleY: Float
    ): Matrix {
        val matrix = Matrix()
        //获取边框相对于子布局背景的比例
        val maxScaleX = (horizontalPadding.toFloat() + childWidth) / childWidth
        val maxScaleY = (verticalPadding.toFloat() + childHeight) / childHeight
        Log.d(TAG, "maxScaleX is $maxScaleX, maxScaleY is $maxScaleY")
        //当最大比例和最小比例相等，无法执行动画，并将动画属性值置为NONE
        if (maxScaleX == defaultScaleX || maxScaleY == defaultScaleY) {
            matrix.setScale(defaultScaleX, defaultScaleY, canvasWidth / 2, canvasHeight / 2)
            animationMode = BorderAnimationMode.NONE
            return matrix
        }
        val periodScaleX = (maxScaleX - defaultScaleX) / animationFrequency
        val periodScaleY = (maxScaleY - defaultScaleY) / animationFrequency
        if (currentScaleX == 0f) {
            animationDirection = BorderDirection.FORWARD
            currentScaleX = defaultScaleX
            currentScaleY = defaultScaleY
        } else {
            if (animationDirection == BorderDirection.FORWARD) {
                if (currentScaleX + periodScaleX <= maxScaleX) {
                    currentScaleX += periodScaleX
                    currentScaleY += periodScaleY
                } else {
                    animationDirection = BorderDirection.BACKWARD
                    currentScaleX -= periodScaleX
                    currentScaleY -= periodScaleY
                }
            } else {
                if (currentScaleX - periodScaleX >= defaultScaleX) {
                    currentScaleX -= periodScaleX
                    currentScaleY -= periodScaleY
                } else {
                    animationDirection = BorderDirection.FORWARD
                    currentScaleX += periodScaleX
                    currentScaleY += periodScaleY
                }
            }
        }
        matrix.setScale(currentScaleX, currentScaleY, canvasWidth / 2, canvasHeight / 2)
        Log.d(TAG, "currentScaleX is $currentScaleX, currentScaleY is $currentScaleY")
        return matrix
    }

    /**
     * 处理没有动画的场景
     */
    private fun handleNoneAnimationMode(
        canvasWidth: Float,
        canvasHeight: Float,
        defaultScaleX: Float,
        defaultScaleY: Float
    ): Matrix {
        val matrix = Matrix()
        matrix.setScale(defaultScaleX, defaultScaleY, canvasWidth / 2, canvasHeight / 2)
        return matrix
    }

    /**
     * 处理矩形的坐标变换
     */
    private fun handleRectCoordinate(rect: Rect, matrix: Matrix): RectF {
        //获取padding后的坐标
        rect.offset(horizontalPadding, verticalPadding)
        val rectF = RectF(rect)
        rectF.transform(matrix)
        return rectF
    }

    /**
     * 修错数据处理(x方向和y方向如有一个值为0都置为0，x方向超过边框宽度一半的修正为宽度的一半， y方向超过边框高度一半的修正为高度的一半)
     */
    private fun amendRadius(borderLayoutWidth: Float, borderLayoutHeight: Float) {
        if (leftTopRadiusX <= 0f || leftTopRadiusY <= 0f) {
            leftTopRadiusX = 0f
            leftTopRadiusY = 0f
        }
        if (leftBottomRadiusX <= 0f || leftBottomRadiusY <= 0f) {
            leftBottomRadiusX = 0f
            leftBottomRadiusY = 0f
        }
        if (rightTopRadiusX <= 0f || rightTopRadiusY <= 0f) {
            rightTopRadiusX = 0f
            rightTopRadiusY = 0f
        }
        if (rightBottomRadiusX <= 0f || rightBottomRadiusY <= 0f) {
            rightBottomRadiusX = 0f
            rightBottomRadiusY = 0f
        }
        if (borderLayoutWidth / 2 < leftTopRadiusX) {
            leftTopRadiusX = borderLayoutWidth / 2
        }
        if (borderLayoutWidth / 2 < leftBottomRadiusX) {
            leftBottomRadiusX = borderLayoutWidth / 2
        }
        if (borderLayoutWidth / 2 < rightTopRadiusX) {
            rightTopRadiusX = borderLayoutWidth / 2
        }
        if (borderLayoutWidth / 2 < rightBottomRadiusX) {
            rightBottomRadiusX = borderLayoutWidth / 2
        }

        if (borderLayoutHeight / 2 < leftTopRadiusY) {
            leftTopRadiusY = borderLayoutHeight / 2
        }
        if (borderLayoutHeight / 2 < leftBottomRadiusY) {
            leftBottomRadiusY = borderLayoutHeight / 2
        }
        if (borderLayoutHeight / 2 < rightTopRadiusY) {
            rightTopRadiusY = borderLayoutHeight / 2
        }
        if (borderLayoutHeight / 2 < rightBottomRadiusY) {
            rightBottomRadiusY = borderLayoutHeight / 2
        }
    }

//    fun addOnClickListener(listener: OnClickListener?) {
//        setOnClickListener {
//            if (hasFocus()) {
//                super.setOnClickListener(listener)
//            } else {
//                requestFocus()
//            }
//        }
//    }
}