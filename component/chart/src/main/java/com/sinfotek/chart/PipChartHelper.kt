package com.sinfotek.chart

import android.graphics.Color
import android.graphics.Typeface
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

/**
 *
 * @author Y&N
 * date: 2022/3/2
 * desc:圆饼图
 */
class PipChartHelper constructor(private val pieChart: PieChart) {
    init {
        initPieChart()
    }

    /**
     * 初始化圆饼图
     */
    private fun initPieChart() {
        //  是否显示中间的洞
        pieChart.isDrawHoleEnabled = true
        //设置中间洞的大小
        pieChart.holeRadius = 40f

        // 半透明圈
        pieChart.transparentCircleRadius = 0f
        //设置半透明圆圈的颜色
        pieChart.setTransparentCircleColor(Color.TRANSPARENT)
        //设置半透明圆圈的透明度
        pieChart.setTransparentCircleAlpha(125)

        //饼状图中间可以添加文字
        pieChart.setDrawCenterText(false)

        // 设置pieChart图表展示动画效果，动画运行1.4秒结束
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        // 初始旋转角度
        pieChart.rotationAngle = 0F
        // 可以手动旋转
        pieChart.isRotationEnabled = true
        //显示成百分比
        pieChart.setUsePercentValues(true)
        //取消右下角描述
        pieChart.description.isEnabled = false

        //是否显示每个部分的文字描述
        pieChart.setDrawEntryLabels(true)
        //描述文字的颜色
        pieChart.setEntryLabelColor(Color.GREEN)
        //描述文字的大小
        pieChart.setEntryLabelTextSize(12F)
        //描述文字的样式
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT)

        //图相对于上下左右的偏移
        pieChart.setExtraOffsets(0F, 0F, 0F, 0F)
        //图标的背景色
        pieChart.setBackgroundColor(Color.TRANSPARENT)
//        设置pieChart图表转动阻力摩擦系数[0,1]
        pieChart.dragDecelerationFrictionCoef = 0.75f

        initPipLegend()
    }

    /**
     * 初始化图饼Legend
     */
    private fun initPipLegend() {
        //获取图例
        val legend = pieChart.legend
        //设置图例水平显示
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        //顶部
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        //右对其
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.textColor = Color.BLACK
        //是否绘制在内部
        legend.setDrawInside(true)
        //x轴的间距
        legend.xEntrySpace = 5f
        //y轴的间距
        legend.yEntrySpace = 10f
        //图例的y偏移量
        legend.yOffset = 10f
        //图例x的偏移量
        legend.xOffset = 10f
        //图例文字的颜色
        legend.textColor = Color.parseColor("#333333")
        //图例文字的大小
        legend.textSize = 13F
    }

    /**
     * 显示实心圆
     * @param yvals
     * @param colors
     */
    fun showSolidPieChart(yValueList: MutableList<PieEntry>, colors: MutableList<Int>) {
        //数据集合
        val dataSet = PieDataSet(yValueList, "")
        //填充每个区域的颜色
        dataSet.colors = colors
        //是否在图上显示数值
        dataSet.setDrawValues(true)
//        文字的大小
        dataSet.valueTextSize = 14F
//        文字的颜色
        dataSet.valueTextColor = Color.GRAY
//        文字的样式
        dataSet.valueTypeface = Typeface.DEFAULT_BOLD
//      当值位置为外边线时，表示线的前半段长度。
        dataSet.valueLinePart1Length = 0.4f
//      当值位置为外边线时，表示线的后半段长度。
        dataSet.valueLinePart2Length = 0.4f
//      当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
        dataSet.valueLinePart1OffsetPercentage = 80f
        // 当值位置为外边线时，表示线的颜色。
        dataSet.valueLineColor = Color.parseColor("#a1a1a1")
//        设置Y值的位置是在圆内还是圆外
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
//        设置Y轴描述线和填充区域的颜色一致
        dataSet.isUsingSliceColorAsValueLineColor = false
//        设置每条之前的间隙
        dataSet.sliceSpace = 0F

        //设置饼状Item被选中时变化的距离
        dataSet.selectionShift = 5f
        //填充数据
        val pieData = PieData(dataSet)
//        格式化显示的数据为%百分比
        pieData.setValueFormatter(PercentFormatter())
//        显示试图
        pieChart.data = pieData
    }

    /**
     * 显示圆环
     * @param yValueList
     * @param colors
     */
    fun showRingPieChart(yValueList: MutableList<PieEntry>, colors: MutableList<Int>) {
        //显示为圆环
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 40f//设置中间洞的大小

        //数据集合
        val dataSet = PieDataSet(yValueList, "")
        //填充每个区域的颜色
        dataSet.colors = colors
        //是否在图上显示数值
        dataSet.setDrawValues(true)
//        文字的大小
        dataSet.valueTextSize = 14F
//        文字的颜色
        dataSet.valueTextColor = Color.BLUE
//        文字的样式
        dataSet.valueTypeface = Typeface.DEFAULT_BOLD

//      当值位置为外边线时，表示线的前半段长度。
        dataSet.valueLinePart1Length = 0.4f
//      当值位置为外边线时，表示线的后半段长度。
        dataSet.valueLinePart2Length = 0.4f
//      当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
        dataSet.valueLinePart1OffsetPercentage = 80f
        // 当值位置为外边线时，表示线的颜色。
        dataSet.valueLineColor = Color.parseColor("#a1a1a1")
//        设置Y值的位置是在圆内还是圆外
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
//        设置Y轴描述线和填充区域的颜色一致
        dataSet.isUsingSliceColorAsValueLineColor = false
//        设置每条之前的间隙
        dataSet.sliceSpace = 0F

        //设置饼状Item被选中时变化的距离
        dataSet.selectionShift = 5f
        //填充数据
        val pieData = PieData(dataSet)
//        格式化显示的数据为%百分比
        pieData.setValueFormatter(PercentFormatter())
//        显示试图
        pieChart.data = pieData
    }
}