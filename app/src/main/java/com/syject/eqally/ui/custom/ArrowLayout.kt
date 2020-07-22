package com.syject.eqally.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.syject.eqally.R
import com.syject.eqally.data.enums.EmotionEnums

class ArrowLayout : RelativeLayout, ViewPager.OnPageChangeListener {
    private var previousArrowItems: Array<ArrowItem>? = null
    lateinit var currentEmotion: EmotionEnums
    private lateinit var viewPager: ViewPager
    private val arrows = HashMap<Int, Array<ArrowItem>>()
    private val arrowStraightHeight = 40
    private val arrowStraightWidth = 20

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, att: AttributeSet?) : this(context, att, 0)
    constructor(context: Context, att: AttributeSet?, def: Int) : super(context, att, def) {
        initLayout()
    }

    private fun initLayout() {
        View.inflate(context, R.layout.custom_arrow_layout, this)
    }

    fun showImage(bitmap: Bitmap) {
        val image = findViewById<ImageView>(R.id.emotionImage)
            .apply { setImageBitmap(bitmap) }
        image.post {
            createArrows()
            drawArrows(image.imageMatrix)
        }
    }

    private fun createArrows() {
        when (currentEmotion) {
            EmotionEnums.ANGER -> {
                arrows[0] = arrayOf(
                    ArrowItem(255, 460, ArrowType.RIGHT),
                    ArrowItem(300, 460, ArrowType.LEFT),
                    ArrowItem(205, 458, ArrowType.DOWN),
                    ArrowItem(160, 468, ArrowType.DOWN)
                )
                arrows[1] = arrayOf(
                    ArrowItem(382, 495, ArrowType.UP),
                    ArrowItem(382, 565, ArrowType.UP)
                )
                arrows[2] = arrayOf(
                    ArrowItem(220, 720, ArrowType.DOWN),
                    ArrowItem(220, 760, ArrowType.UP),
                    ArrowItem(355, 715, ArrowType.CIRCLE_DOWN),
                    ArrowItem(355, 755, ArrowType.CIRCLE_UP)
                )
                arrows[3] = arrayOf(ArrowItem(290, 820, ArrowType.UP))
            }
            EmotionEnums.CONTEMPT -> {
                arrows[0] = arrayOf(ArrowItem(170, 708, ArrowType.OBLIQUE_LEFT))
            }
            EmotionEnums.DISGUST -> {
                arrows[0] = arrayOf(
                    ArrowItem(380, 430, ArrowType.DOWN),
                    ArrowItem(459, 450, ArrowType.DOWN)
                )
                arrows[1] = arrayOf(ArrowItem(222, 610, ArrowType.UP))
                arrows[2] = arrayOf(ArrowItem(285, 650, ArrowType.UP))
                arrows[3] = arrayOf(ArrowItem(285, 780, ArrowType.DOWN))
            }
            EmotionEnums.FEAR -> {
                arrows[0] = arrayOf(
                    ArrowItem(255, 440, ArrowType.RIGHT),
                    ArrowItem(300, 440, ArrowType.LEFT),
                    ArrowItem(205, 410, ArrowType.UP),
                    ArrowItem(140, 430, ArrowType.UP)
                )
                arrows[1] = arrayOf(ArrowItem(190, 460, ArrowType.UP))
                arrows[2] = arrayOf(ArrowItem(390, 725, ArrowType.RIGHT))
            }
            EmotionEnums.HAPPINESS -> {
                arrows[0] = arrayOf(
                    ArrowItem(470, 510, ArrowType.DOWN),
                    ArrowItem(470, 550, ArrowType.UP)
                )
                arrows[1] = arrayOf(ArrowItem(400, 590, ArrowType.UP))
                arrows[2] = arrayOf(ArrowItem(170, 708, ArrowType.OBLIQUE_LEFT))
            }
            EmotionEnums.SADNESS -> {
                arrows[0] = arrayOf(
                    ArrowItem(255, 430, ArrowType.UP),
                    ArrowItem(330, 430, ArrowType.UP)
                )
                arrows[1] = arrayOf(ArrowItem(390, 460, ArrowType.DOWN))
                arrows[2] = arrayOf(ArrowItem(380, 745, ArrowType.DOWN))
            }
            else -> {
                arrows[0] = arrayOf(
                    ArrowItem(140, 380, ArrowType.UP),
                    ArrowItem(220, 380, ArrowType.UP)
                )
                arrows[1] = arrayOf(ArrowItem(190, 455, ArrowType.UP))
                arrows[2] = arrayOf(ArrowItem(295, 785, ArrowType.DOWN))
            }
        }
    }

    private fun drawArrows(matrix: Matrix) {
        arrows.forEach { (group, arrowItems) ->
            arrowItems.forEach { arrowItem ->
                matrix.mapPoints(arrowItem.startPoint)
                matrix.mapPoints(arrowItem.endPoint)
                val arrowImage = arrowItem.arrowView
                arrowImage.setImageResource(arrowItem.arrowType.notActiveArrowDrawable)
                arrowImage.layoutParams =
                    LayoutParams(
                        (arrowItem.endPoint[0] - arrowItem.startPoint[0]).toInt(),
                        (arrowItem.endPoint[1] - arrowItem.startPoint[1]).toInt()
                    )
                arrowImage.x = arrowItem.startPoint[0]
                arrowImage.y = arrowItem.startPoint[1]
                arrowImage.requestLayout()
                arrowImage.scaleType = ImageView.ScaleType.FIT_XY
                arrowImage.setOnClickListener { showExpressionCard(group) }
                changeCurrentArrow(0)
                addView(arrowImage)
            }
        }
    }

    private fun showExpressionCard(group: Int) {
        viewPager.currentItem = group
    }

    private inner class ArrowItem(
        arrowStartX: Int,
        arrowStartY: Int,
        val arrowType: ArrowType
    ) {
        val arrowView = ImageView(context)
        var startPoint = FloatArray(2)
        var endPoint = FloatArray(2)

        init {
            startPoint[0] = arrowStartX.toFloat()
            startPoint[1] = arrowStartY.toFloat()
            endPoint[0] = (arrowStartX + arrowStraightWidth).toFloat()
            endPoint[0] = when (arrowType) {
                ArrowType.UP, ArrowType.DOWN -> arrowStartX + arrowStraightWidth
                ArrowType.LEFT, ArrowType.RIGHT -> arrowStartX + arrowStraightHeight
                ArrowType.CIRCLE_UP, ArrowType.CIRCLE_DOWN -> arrowStartX + arrowStraightWidth + 8
                ArrowType.OBLIQUE_LEFT, ArrowType.OBLIQUE_RIGHT -> arrowStartX + arrowStraightHeight - 10
            }.toFloat()
            endPoint[1] = when (arrowType) {
                ArrowType.UP, ArrowType.DOWN -> arrowStartY + arrowStraightHeight
                ArrowType.LEFT, ArrowType.RIGHT -> arrowStartY + arrowStraightWidth
                ArrowType.CIRCLE_UP, ArrowType.CIRCLE_DOWN -> arrowStartY + arrowStraightHeight
                ArrowType.OBLIQUE_LEFT, ArrowType.OBLIQUE_RIGHT -> arrowStartY + arrowStraightHeight / 2
            }.toFloat()

        }
    }

    fun attachViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        this.viewPager.addOnPageChangeListener(this)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        changePreviousArrow()
        changeCurrentArrow(position)
    }

    private fun changePreviousArrow() {
        previousArrowItems?.forEach { arrowItem ->
            arrowItem.arrowView.setImageResource(arrowItem.arrowType.notActiveArrowDrawable)
        }
    }

    private fun changeCurrentArrow(position: Int) {
        arrows[position]?.let { arrowsItems ->
            arrowsItems.forEach { arrowItem ->
                arrowItem.arrowView.setImageResource(arrowItem.arrowType.activeArrowDrawable)
            }
            previousArrowItems = arrowsItems
        }
    }

    private enum class ArrowType constructor(
        val notActiveArrowDrawable: Int,
        val activeArrowDrawable: Int
    ) {
        DOWN(R.drawable.arrow_straight_down_white, R.drawable.arrow_straight_down_green),
        UP(R.drawable.arrow_straight_up_white, R.drawable.arrow_straight_up_green),
        OBLIQUE_LEFT(R.drawable.arrow_oblique_left_white, R.drawable.arrow_oblique_left_green),
        OBLIQUE_RIGHT(R.drawable.arrow_oblique_right_white, R.drawable.arrow_oblique_right_green),
        LEFT(R.drawable.arrow_straight_left_white, R.drawable.arrow_straight_left_green),
        RIGHT(R.drawable.arrow_straight_right_white, R.drawable.arrow_straight_right_green),
        CIRCLE_UP(R.drawable.arrow_circle_up_white, R.drawable.arrow_circle_up_green),
        CIRCLE_DOWN(R.drawable.arrow_circle_down_white, R.drawable.arrow_circle_down_green),

    }
}