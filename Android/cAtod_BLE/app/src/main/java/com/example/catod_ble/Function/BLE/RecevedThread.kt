package com.example.catod_ble.Function.BLE

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.catod_ble.data.Data
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class RecevedThread(bluetoothSocket: BluetoothSocket,):Thread()
{
    var inputStream: InputStream? = null    // Входной поток
    var outputStream: OutputStream? = null  // Выходной поток

    init {
       try {
           inputStream = bluetoothSocket.inputStream // Инициализация входного потока
       }
       catch (_:IOException)
       {}

       try {
           outputStream = bluetoothSocket.outputStream // Инициализация выходного потока
       }catch (_:IOException){}
    }

    override fun run() {
        super.run()

        var buffer = ByteArray(300)
        Data.BLEconnected = true
        sendMassege("a".toByteArray())

        try {
            while (true)
            {
                val size = inputStream!!.read(buffer)
                val message = String(buffer,0,size)

                if(size > 20)
                {
                    val JSONOBJECT = JSONObject(message)
                    //Data.ParcelJSON.add(JSONOBJECT)
                    Data.ParcelJson = JSONOBJECT
                }

                if(Data.sendComand)
                {
                    // Отправляем данные
                    sendMassege(Data.command.toString().toByteArray())
                    Data.sendComand = false
                }
                else{
                    sendMassege("a".toByteArray())
                }

            }

        }catch (_: Exception){ }
    }

    public fun sendMassege(byteArray: ByteArray)
    {
        try {
            outputStream!!.write(byteArray)
        }
        catch (_:Exception){}
    }
}