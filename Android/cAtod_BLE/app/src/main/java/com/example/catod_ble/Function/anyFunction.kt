package com.example.catod_ble.Function

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import co.yml.charts.common.model.Point
import com.example.catod_ble.data.Data
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.delay


// Получение доступных устройств
@RequiresApi(Build.VERSION_CODES.S)
fun getPairedDevices(context: Context, btAdapter: BluetoothAdapter) {

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        getPermission(context) // Запрос разрешений

    } else {

        val pairedDevices: Set<BluetoothDevice>? = btAdapter?.bondedDevices // Подключенные устройства

        for ( i in pairedDevices!!)
        {
           Data.NameBLEarray.add(i.name)
           Data.MacBLEarray.add(i.address)
        }
    }
}

suspend fun apdateUi(
    arrayUIP: MutableState<ArrayList<Point>>,
    UIPmax: MutableState<Float>,
    UIPmin: MutableState<Float>,
    arrayPWM: MutableState<ArrayList<Point>>,
    PWMmax: MutableState<Float>,
    PWMmin: MutableState<Float>,
    arrayReactT: MutableState<ArrayList<Point>>,
    ReactTmax: MutableState<Float>,
    ReactTmin: MutableState<Float>,
    arrayReactD: MutableState<ArrayList<Point>>,
    ReactDmax: MutableState<Float>,
    ReactDmin: MutableState<Float>,
    arrayVodorodT: MutableState<ArrayList<Point>>,
    VodorodTmax: MutableState<Float>,
    VodorodTmin: MutableState<Float>,
    VodorodDmax: MutableState<Float>,
    VodorodDmin: MutableState<Float>,
    arrayVodorodD: MutableState <ArrayList<Point>>,
    cpuTemp: MutableState<Double>,
    kulStatus: MutableState<String>,
    status: MutableState<String>,
    length: MutableState<Int>,
    color: MutableState<Color>)
{

    while (true)
    {
        checkConnect(color)
        if (Data.ParcelJson != null)
        {
            try {
                // Добавление данных в выходные характеристики
                arrayUIP.value.add(
                    Point(
                        x =  arrayUIP.value.lastIndex.toFloat(),
                        y = Data.ParcelJson!!.getString("OutP").toFloat()
                    ))

                if(arrayUIP.value.last().y > UIPmax.value)
                {
                    UIPmax.value = arrayUIP.value.last().y
                }
                if (arrayUIP.value.last().y < UIPmin.value)
                {
                    UIPmin.value = arrayUIP.value.last().y
                }

                arrayPWM.value.add(
                    Point(
                        x =  arrayPWM.value.lastIndex.toFloat(),
                        y = Data.ParcelJson!!.getString("PWM").toFloat()
                    ))

                if(arrayPWM.value.last().y > PWMmax.value)
                {
                    PWMmax.value = arrayPWM.value.last().y
                }
                if (arrayPWM.value.last().y < PWMmin.value)
                {
                    PWMmin.value = arrayPWM.value.last().y
                }

                arrayReactT.value.add(
                    Point(
                        x =  arrayReactT.value.lastIndex.toFloat(),
                        y = Data.ParcelJson!!.getString("ReactT").toFloat()
                    ))

                if(arrayReactT.value.last().y > ReactTmax.value)
                {
                    ReactTmax.value = arrayReactT.value.last().y
                }
                if (arrayReactT.value.last().y < ReactTmin.value)
                {
                    ReactTmin.value = arrayReactT.value.last().y
                }

                arrayReactD.value.add(
                    Point(
                        x =  arrayReactD.value.lastIndex.toFloat(),
                        y = Data.ParcelJson!!.getString("ReactD").toFloat()
                    ))

                if(arrayReactD.value.last().y > ReactDmax.value)
                {
                    ReactDmax.value = arrayReactD.value.last().y
                }
                if (arrayReactD.value.last().y < ReactDmin.value)
                {
                    ReactDmin.value = arrayReactD.value.last().y
                }

                arrayVodorodT.value.add(
                    Point(
                        x =  arrayVodorodT.value.lastIndex.toFloat(),
                        y = Data.ParcelJson!!.getString("Vodorod_T").toFloat()
                    ))

                if(arrayVodorodT.value.last().y > VodorodTmax.value)
                {
                    VodorodTmax.value = arrayVodorodT.value.last().y
                }
                if (arrayVodorodT.value.last().y < VodorodTmin.value)
                {
                    VodorodTmin.value = arrayVodorodT.value.last().y
                }

                arrayVodorodD.value.add(
                    Point(
                        x =  arrayVodorodD.value.lastIndex.toFloat(),
                        y = Data.ParcelJson!!.getString("Vodorod_D").toFloat()
                    ))

                if(arrayVodorodD.value.last().y > VodorodDmax.value)
                {
                    VodorodDmax.value = arrayVodorodD.value.last().y
                }
                if (arrayVodorodD.value.last().y < VodorodDmin.value)
                {
                    VodorodDmin.value = arrayVodorodD.value.last().y
                }

                cpuTemp.value = Data.ParcelJson!!.getString("CPU_temp").toDouble()
                kulStatus.value = Data.ParcelJson!!.getString("Sost_kul")
                status.value = Data.ParcelJson!!.getString("Status")

                length.value++

            }catch (e: Exception)
            {
                Log.d("kkk", e.toString())
            }
            Data.ParcelJson = null

        }

        delay(1000)
        Log.d("ppp", "Corotine run ")
    }
}

// Провека подключения к устройству
suspend fun checkConnect(color: MutableState<Color>) {

    while (true)
    {
        if (Data.BLEconnected)
        {
            color.value = Color.Green
            break
        }
        else
        {
            color.value = Color.White
        }
        delay(1000)
    }
}

// Получение разрешений
@RequiresApi(Build.VERSION_CODES.S)
fun getPermission(context: Context) {
    val dexter = Dexter.withContext(context)
        .withPermissions(

            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE


        ).
        withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                report.let {

                    if (report.areAllPermissionsGranted()) {

                        Toast.makeText(context, "Permissions Granted..", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Permissions Denied..", Toast.LENGTH_SHORT).show()
                    }
                }
            }


            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {

                token?.continuePermissionRequest()
            }
        }).withErrorListener {

            Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
        }
    dexter.check()
}

