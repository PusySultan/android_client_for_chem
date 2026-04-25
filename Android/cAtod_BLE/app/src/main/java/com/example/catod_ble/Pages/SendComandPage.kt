package com.example.catod_ble.Pages

import ViewChart
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import co.yml.charts.common.model.Point
import com.example.catod_ble.Pages.ui.theme.CAtod_BLETheme
import com.example.catod_ble.R
import com.example.catod_ble.data.Data

class SendComandPage : ComponentActivity() {

    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        enableEdgeToEdge()

        setContent {

            val mutableState = remember {
                mutableStateOf(arrayListOf(
                    Point(0f,0f), Point(0f,1f), Point(0f,1f), Point(0f,0f) ,Point(1f,0f), Point(1f,1f),
                    Point(1f,1f), Point(1f,0f), Point(2f,0f), Point(2f,1f), Point(2f,1f), Point(2f,0f),
                    Point(3f,0f), Point(3f,1f), Point(3f,1f), Point(3f,0f), Point(4f,0f), Point(4f,1f),
                    Point(4f,1f), Point(4f,0f), Point(5f,0f), Point(5f,1f), Point(5f,1f), Point(5f,0f),
                    Point(6f,0f), Point(6f,1f), Point(6f,1f), Point(6f,0f), Point(7f,0f), Point(7f,1f),
                    Point(7f,1f), Point(7f,0f), Point(8f,0f), Point(8f,1f), Point(8f,1f), Point(8f,0f),
                    Point(9f,0f), Point(9f,1f), Point(9f,1f), Point(9f,0f)
                ))
            }

            val arSave = arrayListOf(
                Point(0f,0f), Point(0f,1f), Point(0f,1f), Point(0f,0f) ,Point(1f,0f), Point(1f,1f),
                Point(1f,1f), Point(1f,0f), Point(2f,0f), Point(2f,1f), Point(2f,1f), Point(2f,0f),
                Point(3f,0f), Point(3f,1f), Point(3f,1f), Point(3f,0f), Point(4f,0f), Point(4f,1f),
                Point(4f,1f), Point(4f,0f), Point(5f,0f), Point(5f,1f), Point(5f,1f), Point(5f,0f),
                Point(6f,0f), Point(6f,1f), Point(6f,1f), Point(6f,0f), Point(7f,0f), Point(7f,1f),
                Point(7f,1f), Point(7f,0f), Point(8f,0f), Point(8f,1f), Point(8f,1f), Point(8f,0f),
                Point(9f,0f), Point(9f,1f), Point(9f,1f), Point(9f,0f)
            )

            val message = remember{mutableStateOf("")}

            val delta = remember {
                mutableFloatStateOf(0f)
            }

            val isImpulse = remember {
                mutableStateOf(false)
            }

            val isLine = remember {
                mutableStateOf(true)
            }

            val pauseTime = remember {
                mutableStateOf("0")
            }

            val jobTime = remember {
                mutableStateOf("infinity")
            }

            CAtod_BLETheme {
               Column (modifier = Modifier
                   .fillMaxSize()
                   .background(colorResource(id = R.color.Blue_Black_v1)),
                   horizontalAlignment = Alignment.CenterHorizontally

               )
               {
                   Header(componentActivity = this@SendComandPage)
                   Body(
                       mutableState =  mutableState,
                       message = message,
                       delta = delta,
                       arSave = arSave,
                       isLine = isLine,
                       isImpulse = isImpulse,
                       pauseTime = pauseTime,
                       jobTime = jobTime
                   )
              }
           }

       }
   }
}


@Composable
fun Header(componentActivity: ComponentActivity)
{
    Box(modifier = Modifier
        .background(colorResource(id = R.color.Blue_Black_v1))
        .padding(top = 35.dp))
    {
        Row(modifier = Modifier
            .fillMaxWidth())
        {
            Box(modifier = Modifier.fillMaxWidth(0.1f))
            {
                IconButton(
                    onClick = { componentActivity.finish()})
                {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "send command", tint = Color.White )
                }
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter)
            {
                Text(text = "Управление", color = Color.White)
            }

        }
    }
}

@Composable
fun Body(
    mutableState: MutableState<ArrayList<Point>>,
    message: MutableState<String>,
    delta: MutableState<Float>,
    isImpulse: MutableState<Boolean>,
    isLine: MutableState<Boolean>,
    pauseTime: MutableState<String>,
    jobTime: MutableState<String>,
    arSave: ArrayList<Point>,
) {
    val isRead = remember {
        mutableStateOf(true)
    }

    Column(modifier = Modifier
        .fillMaxWidth(0.8f),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        ViewChart(mutableState = mutableState)

        Text(fontSize = 13.sp, color = Color.White,
             text = "Форма сигнала на драйвере устройства")

        // Время паузы ввод текста
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        )
        {
            Text(modifier =  Modifier.fillMaxWidth(0.4f),
                text = "Время паузы",
                color = if(isRead.value)
                {
                    Color.Gray
                }
                else
                {
                    Color.White
                },
                fontSize = 15.sp
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                value =  pauseTime.value,
                onValueChange = {
                    pauseTime.value = it
                },
                textStyle = TextStyle(fontSize = 15.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                readOnly = isRead.value,

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(id = R.color.Blue_Black_v1),
                    unfocusedTextColor = Color.White, //цвет, используемый для ввода текста этого текстового поля, когда он не сфокусирован
                    focusedContainerColor = colorResource(id = R.color.Blue_Black_v1),
                    focusedTextColor = Color.White,
                )
            )

        }

        // Время работы ввод текста
        Row (modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        )
        {
            Text(
                modifier =  Modifier.fillMaxWidth(0.4f),
                text = "Вреям работы",
                color = if(isRead.value)
                {
                    Color.Gray
                }
                else
                {
                    Color.White
                },
                fontSize = 15.sp
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                value = jobTime.value,
                onValueChange = {
                    jobTime.value = it
                },
                textStyle = TextStyle(fontSize = 15.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                readOnly = isRead.value,

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(id = R.color.Blue_Black_v1),
                    unfocusedTextColor = Color.White, //цвет, используемый для ввода текста этого текстового поля, когда он не сфокусирован
                    focusedContainerColor = colorResource(id = R.color.Blue_Black_v1),
                    focusedTextColor = Color.White,
                )
            )
        }

        // Отступ
        Box(modifier = Modifier.height(25.dp))

        // check box
        Row {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isLine.value, onCheckedChange = {
                    isLine.value = it
                    isImpulse.value = !it
                    isRead.value = true
                    pauseTime.value = "0"
                    jobTime.value = "infinity"
                })
                Text(text = "Лин.режим", color = Color.White, fontSize = 10.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isImpulse.value, onCheckedChange = {
                        isImpulse.value = it
                        isLine.value = !it
                        isRead.value = false
                    })
                Text(text = "Импульсный режим", color = Color.White, fontSize = 10.sp)
            }
        }

        TextField(
            value =  message.value,
            onValueChange =  {
                message.value = it.replace(',', '.')
                try {
                    delta.value = it.toFloat()

                    if(delta.value >= 0 && delta.value < 1)
                    {
                        mutableState.value = arSave
                        mutableState.value = correctedData(
                            points = mutableState,
                            delta = message.value.toFloat())
                    }

                }catch (_: Exception){}
            },
            modifier = Modifier.width(200.dp),
            textStyle = TextStyle(fontSize = 15.sp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),

            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = colorResource(id = R.color.Blue_Black_v1),
                unfocusedTextColor = Color.White, //цвет, используемый для ввода текста этого текстового поля, когда он не сфокусирован
                focusedContainerColor = colorResource(id = R.color.Blue_Black_v1),
                focusedTextColor = Color.White,
            )
        )
        
        Row {
            Button(
                modifier = Modifier.width(50.dp),
                onClick = {
                    if (delta.value >= 0.1) {
                        delta.value -= 0.05f
                        message.value = delta.value.toString()
                        mutableState.value = arSave
                        mutableState.value =
                            correctedData(points = mutableState, delta = delta.value)
                    }
                }
            )
            { Text(text = "-") }

            Box(modifier = Modifier.width(15.dp))

            Button(
                modifier = Modifier.width(70.dp),
                onClick = {
                    Data.sendComand = true
                    Data.command.put("mode", if( isLine.value ){ "Line" } else{ "Impulse" } )
                    Data.command.put("borehole", delta.value)
                    Data.command.put("jobTime", jobTime.value)
                    Data.command.put("pauseTime", pauseTime.value)
                }
            )
            { Text(text = "Ok") }

            Box(modifier = Modifier.width(15.dp))

            Button(
                modifier = Modifier.width(50.dp),
                onClick = {
                    if (delta.value <= 0.9f)
                    {
                        delta.value += 0.05f
                        message.value = delta.value.toString()
                        mutableState.value = arSave
                        mutableState.value =
                            correctedData(points = mutableState, delta = delta.value)
                    }
                }
            )
            { Text(text = "+") }

        }

    }
}


fun correctedData(points: MutableState<ArrayList<Point>>, delta: Float): ArrayList<Point>
{
    val newPoints = arrayListOf(Point(0f,0f))

   try {
       for (i in 1..<points.value.size)
       {
           newPoints.add(Point(
               x = if(points.value[i].y != 0f && points.value[i - 1].y != 0f)
               {
                   points.value[i].x + delta
               }
               else if(points.value[i].y == 0f && points.value[i - 1].y != 0f)
               {
                   points.value[i].x + delta
               }
               else{
                   points.value[i].x
               },
               y = points.value[i].y
           ))
       }
   }catch (e: Exception){
       Log.d("lll", e.message.toString())
   }
    return newPoints
}
