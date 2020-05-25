package com.example.physicalfitnessexamination.view

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.physicalfitnessexamination.R
import com.example.physicalfitnessexamination.util.dp2px
import com.example.physicalfitnessexamination.util.sp2px
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * 积分分差设置控件
 */
class PointsDifferView : View {

    companion object {
        const val TAG = "PointsDifferView"
    }

    //最大间隔数
    var maxCount = 20
        set(value) {
            field = value
            invalidateTextPaintAndMeasurements()
            postInvalidate()
        }

    private var _exampleString: String = "" // TODO: use a default from R.string...
    private var _exampleColor: Int = ContextCompat.getColor(context, R.color._B7CFF3)
    private var _exampleDimension: Float = 13F.sp2px // TODO: use a default from R.dimen...

    private lateinit var textPaint: TextPaint
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    private lateinit var proLinePaint: Paint //进度条画笔
    private val proRect: RectF = RectF() //进度条区域
    private val proLineMarginStart = 5.dp2px
    private val proLineMarginEnd = 5.dp2px
    private val PRO_HEIGHT = 10F.dp2px

    private val startPoint: PointBean = PointBean() //开头Point
    private val endPoint: PointBean = PointBean() //结尾Point

    private lateinit var sliderPaint: Paint //滑动块画笔
    private val SLIDER_WIDTH = 8.dp2px
    private val SLIDER_HEIGHT = 26.dp2px

    //文字和滑动块之间间距
    private val sizeBetweenTextAndSlider = 5.dp2px

    //进度条位置测算
    private var proMiddleHeight = 0F

    private lateinit var divLinePaint: Paint //进度条画笔

    private var sliderTargetPoint: PointBean? = null //按压住的目标滑块
    private val sliderList = mutableListOf<PointBean>() //添加的点集合

    var onPointsDifferListener: OnPointsDifferListener? = null
        set(value) {
            field = value
            value?.onChange(sliderList)
        }

    /**
     * The text to draw
     */
    var exampleString: String
        get() = _exampleString
        set(value) {
            _exampleString = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * The font color
     */
    var exampleColor: Int
        get() = _exampleColor
        set(value) {
            _exampleColor = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this dimension is the font size.
     */
    var exampleDimension: Float
        get() = _exampleDimension
        set(value) {
            _exampleDimension = value
            invalidateTextPaintAndMeasurements()
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.PointsDifferView, defStyle, 0
        )

        _exampleString = a.getString(
                R.styleable.PointsDifferView_exampleString
        ) ?: ""
        _exampleColor = a.getColor(
                R.styleable.PointsDifferView_exampleColor,
                exampleColor
        )
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _exampleDimension = a.getDimension(
                R.styleable.PointsDifferView_exampleDimension,
                exampleDimension
        )

        a.recycle()

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        // Set up a default TextPaint object
        textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.black)
            textAlign = Paint.Align.LEFT
            textSize = exampleDimension
            textWidth = measureText(exampleString)
            textHeight = abs(fontMetrics.ascent)
        }

        proLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL_AND_STROKE
            color = exampleColor
        }

        sliderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL_AND_STROKE
            color = ContextCompat.getColor(context, R.color._FFB14B)
        }

        divLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL_AND_STROKE
            color = ContextCompat.getColor(context, R.color.black)
            strokeWidth = 0.5F.dp2px
        }

        startPoint.indexCode = 1
        endPoint.indexCode = maxCount
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthResult = 0
        var heightResult = 0

        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        when (widthSpecMode) {
            MeasureSpec.UNSPECIFIED -> widthResult = widthSpecSize
            MeasureSpec.AT_MOST -> widthResult = 200.dp2px
            MeasureSpec.EXACTLY -> widthResult = widthSpecSize
        }

        when (heightSpecMode) {
            MeasureSpec.UNSPECIFIED -> heightResult = heightSpecSize
            MeasureSpec.AT_MOST -> heightResult = (textHeight + sizeBetweenTextAndSlider + SLIDER_HEIGHT).toInt()
            MeasureSpec.EXACTLY -> heightResult = heightSpecSize
        }

        setMeasuredDimension(widthResult, heightResult);
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        //左上右下点坐标
        val startTopX = paddingLeft
        val startTopY = paddingTop
        val endBottomX = width - paddingRight
        val endBottomY = height - paddingBottom

        proMiddleHeight = textHeight + sizeBetweenTextAndSlider + SLIDER_HEIGHT / 2

        proRect.set(
                (startTopX + proLineMarginStart).toFloat(), proMiddleHeight - PRO_HEIGHT / 2,
                (endBottomX - proLineMarginEnd).toFloat(), proMiddleHeight + PRO_HEIGHT / 2
        )
        //绘制进度条
        canvas.drawRect(proRect, proLinePaint)

        run {
            //分割线
            for (index in 0 until maxCount) {
                canvas.drawLine(proRect.left + getEachSectionLength() * index,
                        proRect.top,
                        proRect.left + getEachSectionLength() * index,
                        proRect.bottom, divLinePaint)
            }
        }

        //绘制起点、终点拖动块
        startPoint.x = startTopX + proLineMarginStart
        startPoint.y = proMiddleHeight.toInt()
        endPoint.x = endBottomX - proLineMarginEnd
        endPoint.y = proMiddleHeight.toInt()
        drawSlider(canvas, startPoint)
        drawSlider(canvas, endPoint)

        //绘制其他拖动块
        drawSliders(canvas)
    }

    private fun drawSliders(canvas: Canvas) {
        sliderList.forEach { p ->
            drawSlider(canvas, p)
        }
    }

    private fun drawSlider(canvas: Canvas, p: PointBean) {
        val sliderRect = getSliderRectByPoint(p)
        //绘制滑块
        canvas.drawRect(sliderRect, sliderPaint)

        //滑块上的文案
        val textWidth = textPaint.measureText(p.indexCode.toString())
        canvas.drawText(p.indexCode.toString(), sliderRect.left + sliderRect.width() / 2 - textWidth / 2,
                sliderRect.top.toFloat() - sizeBetweenTextAndSlider, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val proLineStart = paddingLeft + proLineMarginStart
        val proLineEnd = width - paddingRight - proLineMarginEnd

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (touchValidity(
                                Point(
                                        proLineStart,
                                        (height - paddingTop - paddingBottom) / 2
                                ),
                                Point(event.x.toInt(), event.y.toInt())
                        )
                ) {
                    //新增一个点
                    sliderTargetPoint = PointBean(1, paddingLeft + proLineMarginStart, proMiddleHeight.toInt())
                    sliderList.add(sliderTargetPoint!!)
                } else {
                    sliderList.forEach { p ->
                        if (touchValidity(p, Point(event.x.toInt(), event.y.toInt()))) {
                            if (sliderTargetPoint == null) {
                                sliderTargetPoint = p
                            }
                        }
                    }
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                event.x.toInt().let { moveX ->
                    val formatX = when {
                        moveX < proLineStart -> {
                            proLineStart
                        }
                        moveX > proLineEnd -> {
                            proLineEnd
                        }
                        else -> {
                            moveX
                        }
                    }

                    sliderTargetPoint?.let { p ->
                        val code = ((p.x - proRect.left) / getEachSectionLength()).roundToInt()
                        p.indexCode = code + 1
                        p.x = formatX
                    }
                }

                postInvalidate()
                return true
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                sliderTargetPoint?.let { p ->
                    val code = ((p.x - proRect.left) / getEachSectionLength()).roundToInt()
                    p.indexCode = code + 1
                    p.x = (code * getEachSectionLength() + proRect.left).toInt()
                }
                sliderTargetPoint = null

                val zeroPoints = sliderList.filter { p ->
                    p.indexCode == 0
                }
                sliderList.removeAll(zeroPoints)

                postInvalidate()

                onPointsDifferListener?.onChange(sliderList)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 判断用户触及滑动块有效性
     */
    private fun touchValidity(targetPoint: Point, touchDownPoint: Point): Boolean {
        val validityRect = Rect(
                targetPoint.x - SLIDER_WIDTH, targetPoint.y - SLIDER_HEIGHT,
                targetPoint.x + SLIDER_WIDTH, targetPoint.y + SLIDER_HEIGHT
        )

        return validityRect.contains(touchDownPoint.x, touchDownPoint.y)
    }

    /**
     * 最小分段的间距长度
     */
    private fun getEachSectionLength(): Float = proRect.width() / (maxCount - 1)

    private fun getSliderRectByPoint(point: Point): Rect {
        return getSliderRectByPoint(point.x, point.y)
    }

    private fun getSliderRectByPoint(pointX: Int, pointY: Int): Rect {
        return Rect(
                pointX - SLIDER_WIDTH / 2,
                pointY - SLIDER_HEIGHT / 2,
                pointX + SLIDER_WIDTH / 2,
                pointY + SLIDER_HEIGHT / 2
        )
    }

    class PointBean : Point {
        constructor() : super()
        constructor(code: Int, x: Int, y: Int) : super(x, y) {
            indexCode = code
        }

        var indexCode = 0
    }

    interface OnPointsDifferListener {
        fun onChange(divisionList: List<PointBean>)
    }
}