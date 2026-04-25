import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.catod_ble.R


@SuppressLint("SuspiciousIndentation", "DefaultLocale")
@Composable
fun Mychart(
    xyArray: MutableState<ArrayList<Point>>,
    maxValue: MutableState<Float>,
    minValue: MutableState<Float>,
    length: MutableState<Int>,
    key: Int = 1,
    @SuppressLint("MutableCollectionMutableState")
    xyArray2: MutableState<ArrayList<Point>> = mutableStateOf(arrayListOf(Point(0f,0f))),
    maxValue2: MutableState<Float> = mutableFloatStateOf(0f),
    minValue2: MutableState<Float> = mutableFloatStateOf(0f),
    scale: MutableState<Int>
    )
{
    val max = getMax(xyArray.value)
    val min = getMin(xyArray.value)

    val step = 4

    val xAxisData = AxisData.Builder()
        .axisStepSize(scale.value.dp)
        .backgroundColor(colorResource(id = R.color.Blue_Black_v1))
        .steps( length.value)
//        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .axisLabelColor(Color.White) // Цвет цифр по оси Х
        .axisLineColor(Color.DarkGray) // Цвет линии оси X
        .shouldDrawAxisLineTillEnd(true) // хз что это, просто так вписал

        .build()

    val yAxisData = AxisData.Builder()
        .steps(step)
        .backgroundColor(colorResource(id = R.color.Blue_Black_v1))
        .labelAndAxisLinePadding(20.dp)
        .axisLabelColor(Color.White) // Цвет цифр по оси У
        .axisLineColor(Color.Transparent) // Цвет линии оси У
        .labelData { i ->
            val yScale = (max - min) / step.toFloat()
            String.format("%.2f", ((i * yScale) + min)
            )
        }.build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = xyArray.value,
                    LineStyle(
                        width = 1f,
                        lineType = LineType.Straight(),
                        color = colorResource(id = R.color.purple_200)
                    ),
                    IntersectionPoint(radius = 0.dp),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(color = Color.Transparent),
                    SelectionHighlightPopUp()
                ),
            ),
        ),

        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = Color.DarkGray, lineWidth = 1.dp, enableVerticalLines = false),
        backgroundColor = colorResource(id = R.color.Blue_Black_v1),
        paddingRight = 0.dp,
    )

    val lineChartData2 = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = xyArray.value,
                    LineStyle(
                        width = 1f,
                        lineType = LineType.Straight(),
                        color = colorResource(id = R.color.purple_200)
                    ),
                    IntersectionPoint(radius = 0.dp),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(color = Color.Transparent),
                    SelectionHighlightPopUp()
                ),
                Line(
                    dataPoints = xyArray2.value,
                    LineStyle(
                        width = 1f,
                        lineType = LineType.Straight(),
                        color = colorResource(id = R.color.teal_200)
                    ),
                    IntersectionPoint(radius = 0.dp),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(color = Color.Transparent),
                    SelectionHighlightPopUp()
                ),
            ),
        ),

        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = Color.DarkGray, lineWidth = 1.dp, enableVerticalLines = false),
        backgroundColor = colorResource(id = R.color.Blue_Black_v1),
        paddingRight = 0.dp,
    )

    Column(
        modifier = Modifier.padding(bottom = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData =
            if(key == 1)
            {
                lineChartData
            } else
            {
                lineChartData2
            }
        )
        Box(modifier = Modifier.height(5.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        )
        {
            Button(
                modifier = Modifier.height(35.dp),
                onClick = {
                    if(scale.value >= 5)
                    {
                        scale.value -=  5
                    }
                    else{
                        scale.value -=  1
                    }
                },
                colors = ButtonColors(
                    containerColor = colorResource(id = R.color.purple_200),
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                )
            )
            {
                Text(text = "-", fontSize = 13.sp)
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 10.dp)
                    .height(35.dp),
                colors = ButtonColors(
                    containerColor = colorResource(id = R.color.purple_200),
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                ),
                onClick = {
                    xyArray.value = arrayListOf(Point(0f, 0f))
                    if(key != 1){xyArray2.value = arrayListOf(Point(0f,0f))}
                    length.value = 0
                    maxValue.value = 0.0f
                })
            {
                Text(text = "Clear graph", fontSize = 12.sp)
            }

            Button(
                modifier =  Modifier.height(35.dp),
                onClick = {
                    scale.value +=  5
                },
                colors = ButtonColors(
                    containerColor = colorResource(id = R.color.purple_200),
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                )
            )
            {
                Text(text = "+", fontSize = 13.sp)
            }

        }

        HorizontalDivider(color = Color.Black, thickness = 3.dp)

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 3.dp),
            verticalAlignment = Alignment.CenterVertically)
        {

            Box(modifier = Modifier
                .height(8.dp)
                .fillMaxWidth(0.1f)
                .background(colorResource(id = R.color.purple_200))
            )

            Box(
                Modifier
                    .fillMaxWidth(0.8f)
                    .padding(start = 5.dp)
            )
            {
                Text(text = "Максимальное значение: ", fontSize = 13.sp ,color = Color.White)
            }

            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
            {
                Text( text = String.format("%.3f", maxValue.value),
                    fontSize = 13.sp,  color = Color.White)
            }
        }


        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 3.dp),
            verticalAlignment = Alignment.CenterVertically)
        {
            Box(modifier = Modifier
                .height(8.dp)
                .fillMaxWidth(0.1f)
                .background(colorResource(id = R.color.purple_200))
            )

            Box(
                Modifier
                    .fillMaxWidth(0.8f)
                    .padding(start = 5.dp)
            )
            {
                Text(text = "Минимальное значение: ", fontSize = 13.sp, color = Color.White)
            }


            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
            {
                Text(
                    text = String.format("%.3f", minValue.value),
                    fontSize = 13.sp, color = Color.White)
            }

        }

        if(key != 1)
        {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 3.dp),
                verticalAlignment = Alignment.CenterVertically)
            {

                Box(modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth(0.1f)
                    .background(colorResource(id = R.color.teal_200))
                )

                Box(
                    Modifier
                        .fillMaxWidth(0.8f)
                        .padding(start = 5.dp)
                )
                {
                    Text(text = "Максимальное значение: ", fontSize = 13.sp, color = Color.White)
                }

                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
                {
                    Text(//text = maxValue2.value.toString(),
                        text = String.format("%.3f", maxValue2.value),
                        fontSize = 13.sp, color = Color.White)
                }
            }


            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 3.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                Box(modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth(0.1f)
                    .background(colorResource(id = R.color.teal_200))
                )

                Box(
                    Modifier
                        .fillMaxWidth(0.8f)
                        .padding(start = 5.dp)
                )
                {
                    Text(text = "Минимальное значение: ", fontSize = 13.sp, color = Color.White)
                }


                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
                {
                    Text(
                        text = String.format("%.3f", minValue.value),
                        fontSize = 13.sp, color = Color.White)
                }

            }
        }

    }

}



private fun getMax(list: List<Point>): Float{
    var max = 0F
    list.forEach { point ->
        if(max < point.y) max = point.y
    }
    return (max + max * 0.1f)
}

private fun getMin(list: List<Point>): Float{
    var min = 100F
    list.forEach { point ->
        if(min > point.y) min = point.y
    }
    return min
}

@Composable
fun ViewChart(mutableState: MutableState<ArrayList<Point>>)
{
    val max = getMax(mutableState.value)
    val min = getMin(mutableState.value)
    val step = 4

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp) // Расстояние между точками Х
        .backgroundColor(colorResource(id = R.color.Blue_Black_v1))
        .steps( 10) // Колличество шагов по оси Х
//        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp) // Отступ
        .axisLabelColor(Color.White) // Цвет цифр по оси Х
        .axisLineColor(Color.DarkGray) // Цвет линии оси X
        .shouldDrawAxisLineTillEnd(true) // хз что это, просто так вписал

        .build()

    val yAxisData = AxisData.Builder()
        .steps(10) // Колличество шагов по оси У
        .backgroundColor(colorResource(id = R.color.Blue_Black_v1))
        .labelAndAxisLinePadding(20.dp) // Отступ
        .axisLabelColor(Color.White) // Цвет цифр по оси У
        .axisLineColor(Color.Transparent) // Цвет линии оси У
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = mutableState.value,
                    LineStyle(
                        width = 3f,
                        lineType = LineType.Straight(),
                        color = colorResource(id = R.color.purple_200)
                    ),
                    IntersectionPoint(radius = 0.dp),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(color = colorResource(id = R.color.purple_200)),
                    SelectionHighlightPopUp()
                ),
            )

        ),

        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = Color.DarkGray, lineWidth = 0.2.dp, enableVerticalLines = false),// Сетка
        backgroundColor = colorResource(id = R.color.Blue_Black_v1),
        paddingRight = 0.dp,
        isZoomAllowed = false, // отключение скрола графика

    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )
}
