package com.example.catod_ble.Pages

import Mychart

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import co.yml.charts.common.model.Point
import com.example.catod_ble.Function.BLE.BTconection
import com.example.catod_ble.Function.apdateUi
import com.example.catod_ble.Function.checkConnect
import com.example.catod_ble.Function.getPairedDevices
import com.example.catod_ble.R
import com.example.catod_ble.ui.theme.CAtod_BLETheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private var btAdapter: BluetoothAdapter? = null // Переменная блютуз адаптера
    private lateinit var bTconection: BTconection
    private var launcher: ActivityResultLauncher<Intent>? = null // Интент для запуска активити
    var job: Job? = null // Управление корутиной

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBL()
        getPairedDevices(this, btAdapter!!)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setContent {

            val UIParray = remember {
                mutableStateOf(arrayListOf(Point(0f, 0f))) } // Состояние массив выходных характеристик

            val UIPmax = remember {
                mutableFloatStateOf(0.0f)
            }

            val UIPmin = remember {
                mutableFloatStateOf(4.0f)
            }

            val PWMarray = remember {
                mutableStateOf(arrayListOf(Point(0f, 0f))) } // Состояние массив ШИМ

            val PWMmax = remember {
                mutableFloatStateOf(0.0f)
            }

            val PWMmin = remember {
                mutableFloatStateOf(1000.0f)
            }

            val VodorodTarray = remember {
                mutableStateOf(arrayListOf(Point(0f, 0f))) } // Состояние массив ШИМ

            val VodTmax = remember {
                mutableFloatStateOf(0.0f)
            }

            val VodTmin = remember {
                mutableFloatStateOf(20.0f)
            }

            val VodorodDarray = remember {
                mutableStateOf(arrayListOf(Point(0f, 0f)))
            }

            val VodDmax = remember {
                mutableFloatStateOf(0.0f)
            }

            val VodDmin = remember {
                mutableFloatStateOf(20.0f)
            }

            val ReactTarray = remember {
                mutableStateOf(arrayListOf(Point(0f, 0f))) } // Состояние массив выходных характеристик


            val ReactTmax = remember {
                mutableFloatStateOf(0.0f)
            }

            val ReactTmin = remember {
                mutableFloatStateOf(20.0f)
            }

            val ReactDarray = remember {
                mutableStateOf(arrayListOf(Point(0f, 0f))) } // Состояние массив выходных характеристик


            val ReactDmax = remember {
                mutableFloatStateOf(0.0f)
            }

            val ReactDmin = remember {
                mutableFloatStateOf(20.0f)
            }

            val colorem = remember { // меняем цвет при подключении
                mutableStateOf(Color.White)
            }

            val cpuTemp = remember {
                mutableDoubleStateOf(0.0)
            }

            val kulStatus = remember {
                mutableStateOf("On")
            }

            val status = remember {
                mutableStateOf("Ok")
            }

            val length = remember {
                mutableIntStateOf(1)
            }

            val scale = remember {
                mutableIntStateOf(100)
            }

            val coroutineScope = rememberCoroutineScope()

            CAtod_BLETheme {

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.Blue_Black_v1))
                    .padding(top = 35.dp), contentAlignment = Alignment.BottomEnd)
                {
                   Row {
                       IconButton(onClick = {

                            startActivity(Intent(this@MainActivity, SendComandPage::class.java))
                       }) {
                           Icon(
                               modifier = Modifier.size(20.dp),
                               painter = painterResource(id = R.drawable.send),
                               contentDescription = "send command", tint = Color.White )
                       }


                       // Кнопка подключения к устройству
                       IconButton(onClick = {

                           launcher?.launch(Intent(this@MainActivity, BLEActivity::class.java))// Запускаем 2 страницу

                       }) {
                           Icon(
                               modifier = Modifier.size(20.dp),
                               painter = painterResource(id = R.drawable.bluetooth_searching),
                               contentDescription = "chosing device", tint = colorem.value )
                       }
                   }
                }

                 job = coroutineScope.launch(start =  CoroutineStart.LAZY) {
                   // checkConnect(colorem) // Первыичная перерисовка цвета
                    apdateUi(
                        arrayUIP =  UIParray,
                        UIPmax = UIPmax,
                        UIPmin = UIPmin,
                        arrayPWM = PWMarray,
                        PWMmax = PWMmax,
                        PWMmin = PWMmin,
                        arrayReactT = ReactTarray,
                        ReactTmax = ReactTmax,
                        ReactTmin = ReactTmin,
                        arrayReactD = ReactDarray,
                        ReactDmax = ReactDmax,
                        ReactDmin = ReactDmin,
                        arrayVodorodT = VodorodTarray,
                        VodorodTmax = VodTmax,
                        VodorodTmin = VodTmin,
                        arrayVodorodD = VodorodDarray,
                        VodorodDmax = VodDmax,
                        VodorodDmin = VodDmin,
                        cpuTemp = cpuTemp,
                        kulStatus = kulStatus,
                        status = status,
                        length = length,
                        color = colorem)
                }

                TabScreen(
                    arrayUIP =  UIParray,
                    uipMax = UIPmax,
                    uipMin = UIPmin,
                    arrayPWM = PWMarray,
                    pwmMax = PWMmax,
                    pwmMin = PWMmin,
                    arrayReactT = ReactTarray,
                    reactTMax = ReactTmax,
                    reactTmin = ReactTmin,
                    arrayReactD = ReactDarray,
                    reactDuitmin = ReactDmin,
                    reactDuitmax = ReactDmax,
                    arrayVodorodT = VodorodTarray,
                    VodorodTmax = VodTmax,
                    VodorodTmin = VodTmin,
                    arrayVodorodD = VodorodDarray,
                    VodorodDmax = VodDmax,
                    VodorodDmin = VodDmin,
                    length = length,
                    cpuTemp = cpuTemp,
                    kulStatus = kulStatus,
                    status = status,
                    scale = scale
                )
            }
        }

        launcher = registerForActivityResult( ActivityResultContracts.StartActivityForResult()){ // collback для запуска активити
                resulte: androidx.activity.result.ActivityResult ->

            if (resulte.resultCode == RESULT_OK)
            {
                val mac = resulte.data?.getStringExtra("mac") // По ключу mac получаем macadress

                if (mac == ""){ // При отключениии

                    bTconection.CloseConnection()
                }
                else{ // При подключении

                    bTconection = BTconection(btAdapter!!, this) // Инициализировали соеденение
                    bTconection.connect(mac!!)
                    job!!.start()
                }
            }
        }

    }

    // Инициализация блютуза
    private fun initBL()
    {
        val blManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = blManager.adapter // Инициализировали блютуз
    }
}


@Composable
fun TabScreen(
    arrayUIP: MutableState<ArrayList<Point>>,
    uipMax: MutableState<Float>,
    uipMin: MutableState<Float>,
    arrayPWM: MutableState<ArrayList<Point>>,
    pwmMax: MutableState<Float>,
    pwmMin: MutableState<Float>,
    arrayReactT: MutableState<ArrayList<Point>>,
    reactTMax: MutableState<Float>,
    reactTmin: MutableState<Float>,
    arrayReactD: MutableState<ArrayList<Point>>,
    reactDuitmin: MutableState<Float>,
    reactDuitmax: MutableState<Float>,
    arrayVodorodT: MutableState<ArrayList<Point>>,
    VodorodTmax: MutableState<Float>,
    VodorodTmin: MutableState<Float>,
    arrayVodorodD: MutableState<ArrayList<Point>>,
    VodorodDmax: MutableState<Float>,
    VodorodDmin: MutableState<Float>,
    length: MutableState<Int>,
    cpuTemp: MutableState<Double>,
    kulStatus: MutableState<String>,
    status: MutableState<String>,
    scale: MutableState<Int>
) {

    val tabIndex = remember { mutableIntStateOf(0) } // Состояние выбора экрана

    val tabs = listOf("UIP", "PWM", "Reactor", "Vodorod")


    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.Transparent)
        
    )
    {
        Box(Modifier.height(80.dp))
        TabRow(
            containerColor = colorResource(id = R.color.Blue_Black_v1), // Цвет контейнера
            contentColor = Color.White, // Цвет контента
            selectedTabIndex = tabIndex.intValue)
        {
            tabs.forEachIndexed {
                    index, title ->
                Tab(
                    modifier = Modifier.background(colorResource(id = R.color.Blue_Black_v1)),
                    text = { Text(title) },
                    selected = tabIndex.intValue == index,
                    onClick = { tabIndex.intValue = index }
                )
            }
        }

        when (tabIndex.intValue) {
            0 -> {
                UIPScreen(arrayUIP, uipMax, uipMin, length, cpuTemp, kulStatus, status, scale)
            }
            1 -> {
                PWMScreen(arrayPWM, pwmMax, pwmMin, length, cpuTemp, kulStatus, status, scale)
            }
            2 -> {
                ReactScreen(arrayReactT, reactTMax, reactTmin, arrayReactD,reactDuitmax,reactDuitmin,
                    length, cpuTemp, kulStatus, status, scale)
            }
            3 -> {
                VodorodScreen(arrayVodorodT, VodorodTmax, VodorodTmin, arrayVodorodD,
                    VodorodDmax,VodorodDmin, length,  cpuTemp, kulStatus, status, scale)
            }
        }
    }
}

@Composable
fun UIPScreen(UIParray: MutableState<ArrayList<Point>>,
              maxValue: MutableState<Float>,
              minValue: MutableState<Float>,
              length: MutableState<Int>,
              cpuTemp: MutableState<Double>,
              kulStatus: MutableState<String>,
              status: MutableState<String>,
              scale: MutableState<Int>)
{
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 25.dp, start = 15.dp, end = 15.dp),
        horizontalAlignment =  Alignment.CenterHorizontally
    )
    {
        TopScreen(cpuTemp = cpuTemp, kulStatus = kulStatus, status = status)

        Box(modifier = Modifier
            .background(colorResource(id = R.color.Blue_Black_v1))
            .fillMaxWidth()
        )
        {
            Mychart(xyArray = UIParray, maxValue = maxValue,length = length, minValue = minValue, scale = scale)
        }

    }
}

@Composable
fun PWMScreen(PWMarray: MutableState<ArrayList<Point>>,
              maxValue: MutableState<Float>,
              minValue: MutableState<Float>,
              length: MutableState<Int>,
              cpuTemp: MutableState<Double>,
              kulStatus: MutableState<String>,
              status: MutableState<String>,
              scale: MutableState<Int>)
{

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 25.dp, start = 15.dp, end = 15.dp),
        horizontalAlignment =  Alignment.CenterHorizontally
    )
    {
        TopScreen(cpuTemp = cpuTemp, kulStatus = kulStatus, status = status)
        Box(modifier = Modifier
            .background(colorResource(id = R.color.Blue_Black_v1))
            .fillMaxWidth()
        )
        {
            Mychart(xyArray = PWMarray, length = length, maxValue = maxValue, minValue = minValue, scale = scale)
        }
    }

}

@Composable
fun ReactScreen(ReactTarray: MutableState<ArrayList<Point>>,
                maxValue: MutableState<Float>,
                minValue: MutableState<Float>,
                ReactDarray: MutableState<ArrayList<Point>>,
                maxValue2: MutableState<Float>,
                minValue2: MutableState<Float>,
                length: MutableState<Int>,
                cpuTemp: MutableState<Double>,
                kulStatus: MutableState<String>,
                status: MutableState<String>,
                scale: MutableState<Int>)
{
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 25.dp, start = 15.dp, end = 15.dp),
        horizontalAlignment =  Alignment.CenterHorizontally
    )
    {

        TopScreen(cpuTemp = cpuTemp, kulStatus = kulStatus, status = status)
        Box(modifier = Modifier
            .background(colorResource(id = R.color.Blue_Black_v1))
            .fillMaxWidth()
        )
        {
            Mychart(xyArray = ReactTarray,
                length = length,
                maxValue = maxValue,
                minValue = minValue,
                key = 2,
                xyArray2 = ReactDarray,
                maxValue2 = maxValue2,
                minValue2 = minValue2,
                scale = scale
            )
        }
    }
}

@Composable
fun VodorodScreen(vodorod_Temp_array: MutableState<ArrayList<Point>>,
                  maxValue: MutableState<Float>,
                  minValue: MutableState<Float>,
                  vodorod_Duit_array: MutableState<ArrayList<Point>>,
                  maxValue2: MutableState<Float>,
                  minValue2: MutableState<Float>,
                  length: MutableState<Int>,
                  cpuTemp: MutableState<Double>,
                  kulStatus: MutableState<String>,
                  status: MutableState<String>,
                  scale: MutableState<Int>)
{
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 25.dp, start = 15.dp, end = 15.dp),
        horizontalAlignment =  Alignment.CenterHorizontally
    )
    {
        TopScreen(cpuTemp = cpuTemp, kulStatus = kulStatus, status = status)
        Box(modifier = Modifier
            .background(colorResource(id = R.color.Blue_Black_v1))
            .fillMaxWidth()
        )
        {
            Mychart(xyArray = vodorod_Temp_array,
                length = length,
                maxValue = maxValue,
                minValue = minValue,
                key = 2,
                xyArray2 = vodorod_Duit_array,
                maxValue2 = maxValue2,
                minValue2 = minValue2,
                scale = scale)
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun TopScreen(cpuTemp: MutableState<Double>, kulStatus: MutableState<String>, status: MutableState<String>)
{
   Column(modifier = Modifier
       .fillMaxWidth()
       .background(colorResource(id = R.color.Blue_Black_v1))
       .padding(bottom = 10.dp)
   )
   {
       HorizontalDivider(color = Color.Black, thickness = 3.dp)

       Row(modifier = Modifier
           .fillMaxWidth()
           .padding(start = 3.dp),
           verticalAlignment = Alignment.CenterVertically)
       {

           Box(
               Modifier
                   .fillMaxWidth(0.8f)
                   .padding(start = 13.dp)
           )
           {
               Text(text = "Температура чипа: ", fontSize = 13.sp, color = Color.White)
           }

           Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
           {
               Text(
                   text = String.format("%.3f", cpuTemp.value),
                   fontSize = 13.sp, color = Color.White)
           }
       }

       Row(modifier = Modifier
           .fillMaxWidth()
           .padding(start = 3.dp),
           verticalAlignment = Alignment.CenterVertically)
       {

           Box(
               Modifier
                   .fillMaxWidth(0.8f)
                   .padding(start = 13.dp)
           )
           {
               Text(text = "Статус кулера: ", fontSize = 13.sp, color = Color.White)
           }

           Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
           {
               Text(text = kulStatus.value, fontSize = 13.sp, color = Color.White)
           }
       }

       Row(modifier = Modifier
           .fillMaxWidth()
           .padding(start = 3.dp),
           verticalAlignment = Alignment.CenterVertically)
       {

           Box(
               Modifier
                   .fillMaxWidth(0.8f)
                   .padding(start = 13.dp)
           )
           {
               Text(text = "Статус : ", fontSize = 13.sp, color = Color.White)
           }

           Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
           {
               Text(text = status.value, fontSize = 13.sp, color = Color.White)
           }
       }
       HorizontalDivider(color = Color.Black, thickness = 3.dp)
   }
}
