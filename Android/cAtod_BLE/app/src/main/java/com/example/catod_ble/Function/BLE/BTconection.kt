package com.example.catod_ble.Function.BLE

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.util.Log

class BTconection(private  val adapter: BluetoothAdapter,private val context: Context)
{
    private lateinit var conThread: connectThread

    fun connect(mac: String)
    {
        if(adapter.isEnabled && mac.isNotEmpty())
        {
            val devise = adapter.getRemoteDevice(mac)

            if(devise != null)
            {
                conThread = connectThread(devise,context)
                conThread.start()
            }
        }
    }

    fun CloseConnection()
    {
        conThread.closeConection()
    }
}