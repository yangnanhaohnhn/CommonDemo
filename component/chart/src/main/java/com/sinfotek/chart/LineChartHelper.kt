package com.sinfotek.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.MPPointF
import com.sinfotek.lib.common.RxDateUtil
import com.sinfotek.lib.common.dp2px
import com.sinfotek.lib.common.log.RxLogUtil

/**
 *
 * @author Y&N
 * date: 2022/3/2
 * desc: 线性chart
 */
class LineChartHelper constructor(private val lineChart: LineChart) {
    private val colors = intArrayOf(
        Color.rgb(
            (Math.random() * 255).toInt(),
            (Math.random() * 255).toInt(),
            (Math.random() * 255).toInt()
        ),
        Color.rgb(
            (Math.random() * 255).toInt(),
            (Math.random() * 255).toInt(),
            (Math.random() * 255).toInt()
        ),
        Color.rgb(
            (Math.random() * 255).toInt(),
            (Math.random() * 255).toInt(),
            (Math.random() * 255).toInt()
        ),
        Color.rgb(
            (Math.random() * 255).toInt(),
            (Math.random() * 255).toInt(),
            (Math.random() * 255).toInt()
        ),
        Color.rgb(
            (Math.random() * 255).toInt(),
            (Math.random() * 255).toInt(),
            (Math.random() * 255).toInt()
        ),
    )
    private var axisLeft: YAxis = lineChart.axisLeft
    private var xAxis: XAxis = lineChart.xAxis

    /**
     * x轴值
     */
    private val tmpXValList = mutableListOf<String>()

    /**
     * 线的总数
     */
    private val tmpLineDataSets = mutableListOf<ILineDataSet>()

    //LineData
    private var tmpLineData = LineData()

    init {
        initLineChart()
    }

    /**
     * 初始化线性Chart
     */
    private fun initLineChart() {
        lineChart.setDrawGridBackground(false)
        //显示边界
        lineChart.setDrawBorders(false)
        //显示动画
        lineChart.animateX(1000)

        //折线图例 标签 设置
        val legend: Legend = lineChart.legend
        legend.form = Legend.LegendForm.LINE
        legend.textSize = 10f
        //显示位置
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(true)

        //X轴设置显示位置在底部
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.labelCount = 10
        //网格线颜色
        xAxis.gridColor = Color.WHITE

        //保证Y轴从0开始，不然会上移一点
        axisLeft.axisMinimum = 0f
        axisLeft.gridColor = Color.GRAY
        axisLeft.gridLineWidth = 0.5f
        //设置背景为虚线
        axisLeft.enableGridDashedLine(10f, 2f, 1f)
        //右侧Y轴不显示
        val axisRight = lineChart.axisRight
        axisRight.isEnabled = false
    }

    /**
     * 设置ValueFormatter
     */
    fun xAxisValueFormatter(
        formatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val date = tmpXValList[(value % tmpXValList.size).toInt()]
                return RxDateUtil.getDateByLong(date.toLong(), RxDateUtil.TYPE_1)
            }
        }
    ) = apply {
        xAxis.valueFormatter = formatter
    }

    /**
     * 添加 SingleEntry
     */
    fun addSingleEntry(
        label: String,
        color: Int = Color.BLUE,
        xValList: MutableList<String>,
        yValEntryList: MutableList<Entry>
    ) = apply {
        val lineDataSet = LineDataSet(yValEntryList, label)
        lineDataSet.color = color
        lineDataSet.lineWidth = 1f
        lineDataSet.fillColor = Color.WHITE
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawFilled(true)
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.valueTextSize = 10f
        tmpLineDataSets.add(lineDataSet)
        tmpLineData.addDataSet(lineDataSet)
        tmpXValList.addAll(xValList)
    }

    /**
     * 添加 MultiEntry
     */
    fun addMultiEntry(
        label: String,
        colorIndex: Int,
        xValList: MutableList<String>,
        yValEntryList: MutableList<Entry>
    ) = apply {
        val lineDataSet = LineDataSet(yValEntryList, label)
        lineDataSet.color = colors[colorIndex % colors.size]
        lineDataSet.lineWidth = 1f
        lineDataSet.fillColor = Color.WHITE
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawFilled(true)
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.valueTextSize = 10f
        tmpLineDataSets.add(lineDataSet)
        tmpLineData.addDataSet(lineDataSet)
        tmpXValList.addAll(xValList)
    }

//    /**
//     * 设置 圆角颜色
//     */
//    fun setCircleInfo(circleRadius: Float = 1f, highLightColor: Int = Color.GRAY) = apply {
//        lineDataSet!!.circleRadius = circleRadius
//        lineDataSet!!.highLightColor = highLightColor
//    }

    /**
     * 设置Y轴值
     */
    fun setYAxis(min: Float, max: Float, labelCount: Int) = apply {
        if (max >= min) {
            axisLeft.axisMaximum = max
            axisLeft.axisMinimum = min
            axisLeft.setLabelCount(labelCount, false)
            lineChart.invalidate()
        }
    }

    /**
     * 设置高限制线
     *
     * @param high
     * @param name
     */
    fun setHighLimitLine(high: Float, name: String, color: Int) = apply {
        var nameStr = name
        if (TextUtils.isEmpty(nameStr)) {
            nameStr = "高限制线"
        }
        val highLimit = LimitLine(high, nameStr)
        highLimit.lineWidth = 4f
        highLimit.textSize = 10f
        highLimit.lineColor = color
        highLimit.textColor = color
        axisLeft.addLimitLine(highLimit)
        lineChart.invalidate()
    }

    /**
     * 设置低限制线
     *
     * @param high
     * @param name
     */
    fun setLowLimitLine(low: Float, name: String) = apply {
        var nameStr = name
        if (TextUtils.isEmpty(nameStr)) {
            nameStr = "低限制线"
        }
        val lowLimit = LimitLine(low, nameStr)
        lowLimit.lineWidth = 4f
        lowLimit.textSize = 10f
        axisLeft.addLimitLine(lowLimit)
        lineChart.invalidate()
    }

    /**
     * 设置描述信息
     */
    fun setDescription(str: String) = apply {
        val description = Description()
        description.text = str
        lineChart.description = description
        lineChart.invalidate()
    }

    fun initMarkerView(
        context: Context,
        layoutId: Int = R.layout.marker_chart_layout,
        listener: ((ChartResult) -> Unit)? = null
    ) = apply {
        val marker = CustomMarkerView(context, layoutId, listener)
        lineChart.marker = marker
        lineChart.invalidate()
        marker.chartView = lineChart
    }

    fun notifyChart() {
        lineChart.data = tmpLineData
        lineChart.notifyDataSetChanged()
    }

    inner class CustomMarkerView(
        context: Context,
        layoutResource: Int,
        private val listener: ((ChartResult) -> Unit)? = null
    ) : MarkerView(context, layoutResource) {
        private var tvMarkerContent: TextView = findViewById(R.id.tv_marker_content)

        @SuppressLint("SetTextI18n")
        override fun refreshContent(e: Entry, highlight: Highlight) {
            //Entry, x: 2.0 y: 1.1 dataSetIndex:为第几个线，
            //Entry, x: 2.0 y: 1.1 Highlight, x: 2.0, y: 1.1, dataSetIndex: 0, stackIndex (only stacked barentry): -1
//            Entry, x: 2.0 y: 1.1 Highlight, x: 2.0, y: 1.1, dataSetIndex: 1, stackIndex (only stacked barentry): -1
            //e.data 需要在创建Entry的时候 传参数 data
            val yVal = tmpXValList[e.x.toInt()]
            val iLineDataSet = tmpLineDataSets[highlight.dataSetIndex]
            tvMarkerContent.text = "${iLineDataSet.label}\nx：${yVal}\ny：${e.y}"
            val res = ChartResult(highlight.x, highlight.y, highlight.dataSetIndex)
            listener?.invoke(res)
            RxLogUtil.e("TAG-> $e ${e.data}")
            super.refreshContent(e, highlight)
        }


        override fun getOffset(): MPPointF {
            val dip2px = 40F.dp2px(context)
            return MPPointF(0F, (-1 * dip2px).toFloat())
        }
    }

    data class ChartResult(val xPosition: Float, val yValue: Float, val dataSetIndex: Int)
}
