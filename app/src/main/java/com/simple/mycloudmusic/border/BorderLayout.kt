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
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.graphics.transform
import com.example.myapplication.border.BorderAnimationMode
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

        //动画加载单位时间，人眼的反应时间为0.1s，超过0.1s将会出现明显的卡顿
        private const val BORDER_ANIMATION_INTERVAL_TIME = 100L

        //单个周期的动画时间，如scale动画扩展为一个周期，缩小为一个周期
        private const val BORDER_ANIMATION_DEFAULT_TIME = 1000

        //动画每次扩展频率
        private const val BORDER_ANIMATION_DEFAULT_FREQUENCY = 10
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
    private var animationDuration = BORDER_ANIMATION_DEFAULT_TIME

    /**
     * 动画频率
     */
    private var animationFrequency = BORDER_ANIMATION_DEFAULT_FREQUENCY

    private var mHandler: Handler = MyHandler(Looper.getMainLooper())

    /**
     * 是否需要自定义radius，而非使用背景默认的radius
     */
    private var needCustomRadius = false

    /**
     * 是否正在执行animation动画
     */
    private var animatingFlag = false

    /**
     * 动画效果重复次数
     */
    private var animationRepeatCount = -1

    /**
     * scale放大动画的X/Y方向的比例
     */
    private var animationScaleX = 1f
    private var animationScaleY = 1f

    /**
     * 是否需要绘制边框
     */
    private var drawBorder = true

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
        val borderRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            resourceValue.getDimension(R.styleable.BorderLayout_border_radius, 0f),
            context.resources.displayMetrics
        )
        leftTopRadiusX = borderRadius
        leftTopRadiusY = borderRadius
        leftBottomRadiusX = borderRadius
        leftBottomRadiusY = borderRadius
        rightTopRadiusX = borderRadius
        rightTopRadiusY = borderRadius
        rightBottomRadiusX = borderRadius
        rightBottomRadiusY = borderRadius
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
        animationMode = resourceValue.getInt(R.styleable.BorderLayout_border_animation_mode, BorderAnimationMode.NONE)
        when (animationMode) {
            BorderAnimationMode.PART_SCALE -> {
                animationFrequency = if (animationDuration / BORDER_ANIMATION_INTERVAL_TIME <= 0L) {
                    BORDER_ANIMATION_DEFAULT_FREQUENCY
                } else {
                    (animationDuration / BORDER_ANIMATION_INTERVAL_TIME).toInt()
                }
                animationDuration = resourceValue.getInt(R.styleable.BorderLayout_border_animation_time, BORDER_ANIMATION_DEFAULT_TIME)
            }
            BorderAnimationMode.SCALE -> {
                animationDuration = resourceValue.getInt(R.styleable.BorderLayout_border_animation_time, BORDER_ANIMATION_DEFAULT_TIME)
                animationScaleX = resourceValue.getFloat(R.styleable.BorderLayout_border_animation_scaleX, 1f)
                animationScaleY = resourceValue.getFloat(R.styleable.BorderLayout_border_animation_scaleY, 1f)
            }
        }
        drawBorder = resourceValue.getBoolean(R.styleable.BorderLayout_border_draw_border, true)
        animationRepeatCount = resourceValue.getInteger(R.styleable.BorderLayout_border_animation_repeat_count, -1)
        //修正边框宽度
        if (borderWidth <= 0f) {
            borderWidth = BORDER_DEFAULT_WIDTH
        }
        //修正横纵向padding，否则interval间隔属性不会生效
        if (interval > horizontalPadding) {
            horizontalPadding = interval.toInt()
            interval = interval.toInt().toFloat()
        }
        if (interval > verticalPadding) {
            verticalPadding = interval.toInt()
            interval = interval.toInt().toFloat()
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
        mPointPaint.style = Paint.Style.STROKE
        mPointPaint.strokeWidth = borderWidth
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        when {
            childCount == 0 -> {
                //do nothing
            }
            childCount == BORDER_CRITICAL_COUNT -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    return
                }
                if (!hasFocus()) {
                    if (animatingFlag) {
//                        handleOverallAnimation(animationScaleX, 1f, animationScaleY, 1f)
                        clearAnimation()
                        animatingFlag = false
                    }
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
        if (drawBorder) {
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
                rectMode -> {
                    val rectF = handleRectCoordinate(rect, matrix)
                    val borderLayoutWidth = rectF.right - rectF.left
                    val borderLayoutHeight = rectF.bottom - rectF.top
                    if (!needCustomRadius) {
                        val radius = outLine.radius
                        leftTopRadiusX = radius
                        leftTopRadiusY = radius
                        leftBottomRadiusX = radius
                        leftBottomRadiusY = radius
                        rightTopRadiusX = radius
                        rightTopRadiusY = radius
                        rightBottomRadiusX = radius
                        rightBottomRadiusY = radius
                    }
                    amendRadius(borderLayoutWidth, borderLayoutHeight)
                    //从矩形左上方处顺时针绘制
                    val tempPath = Path()
                    tempPath.moveTo(rectF.left + leftTopRadiusX, rectF.top)
                    tempPath.lineTo(rectF.right - rightTopRadiusX, rectF.top)
                    tempPath.arcTo(rectF.right - rightTopRadiusX * 2, rectF.top, rectF.right, rectF.top + rightTopRadiusY * 2, 270f, 90f, true)
                    tempPath.lineTo(rectF.right, rectF.bottom - rightBottomRadiusY)
                    tempPath.arcTo(
                        rectF.right - rightBottomRadiusX * 2,
                        rectF.bottom - rightBottomRadiusY * 2,
                        rectF.right,
                        rectF.bottom,
                        0f,
                        90f,
                        true
                    )
                    tempPath.lineTo(rectF.left + leftBottomRadiusX, rectF.bottom)
                    tempPath.arcTo(rectF.left, rectF.bottom - leftBottomRadiusY * 2, rectF.left + leftBottomRadiusX * 2, rectF.bottom, 90f, 90f, true)
                    tempPath.lineTo(rectF.left, rectF.top + leftTopRadiusY)
                    tempPath.arcTo(rectF.left, rectF.top, rectF.left + leftTopRadiusX * 2, rectF.top + leftTopRadiusY * 2, 180f, 90f, true)
                    canvas?.drawPath(tempPath, mPaint)
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
        }
        when (animationMode) {
            BorderAnimationMode.PART_SCALE -> {
                val msg = Message.obtain()
                msg.what = 1
                if (!mHandler.hasMessages(msg.what)) mHandler.sendMessageDelayed(msg, BORDER_ANIMATION_INTERVAL_TIME)
            }
            BorderAnimationMode.SCALE -> {
                if (animatingFlag) return
                handleOverallAnimation(1f, animationScaleX, 1f, animationScaleY)
                animatingFlag = true
            }
        }
    }

    /**
     * 处理整体动画
     */
    private fun handleOverallAnimation(fromScaleX: Float, toScaleX: Float, fromScaleY: Float, toScaleY: Float, cancelAnimation: Boolean = false) {
        Log.i(TAG, "handleOverallAnimation: animatingFlag is $animatingFlag")
        val scaleAnimation =
            ScaleAnimation(fromScaleX, toScaleX, fromScaleY, toScaleY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = animationDuration.toLong()
        scaleAnimation.isFillEnabled = true
        scaleAnimation.fillAfter = true
        scaleAnimation.repeatCount = animationRepeatCount
        scaleAnimation.repeatMode = Animation.REVERSE
        if (cancelAnimation) scaleAnimation.setAnimationListener(animationListener)
        startAnimation(scaleAnimation)
    }

    private val animationListener = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {
            clearAnimation()
        }

        override fun onAnimationRepeat(animation: Animation?) {}
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
            BorderAnimationMode.PART_SCALE -> {
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
        canvasWidth: Float, canvasHeight: Float, childWidth: Int, childHeight: Int, defaultScaleX: Float, defaultScaleY: Float
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
        canvasWidth: Float, canvasHeight: Float, defaultScaleX: Float, defaultScaleY: Float
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
}