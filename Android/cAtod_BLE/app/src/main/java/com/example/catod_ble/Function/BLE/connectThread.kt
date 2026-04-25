package com.example.catod_ble.Function.BLE

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.catod_ble.data.Data
import java.io.IOException
import java.util.UUID

class connectThread(device: BluetoothDevice, private val context: Context):Thread()
{
    private var uuid = "00001101-0000-1000-8000-00805F9B34FB"
    private var BLESocket: BluetoothSocket? = null
    private lateinit var rThread: RecevedThread

    // При инициализации также инициализируется блютуз сокет
    init {
        try {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED)
            {
                BLESocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
            }
        }
        catch (io: IOException){ }
    }

    override fun run() {
        super.run()

        try {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED)
            {
                BLESocket?.connect()
                rThread = RecevedThread(BLESocket!!)
                rThread.start()
            }
        }
            catch (_: IOException)
        {

        }
    }

    fun closeConection()
    {
        try {
            BLESocket?.close()

        }catch (io: IOException){

        }
    }
}