package com.example.catod_ble.data

import org.json.JSONObject

object Data {
    var NameBLEarray = ArrayList<String>() // Массив имен доступных устройств
    var MacBLEarray = ArrayList<String>() // Массив мак адресов доступных устройств
    //var ParcelJSON = ArrayList<JSONObject>() // Массив принятых объектов
    var ParcelJson: JSONObject? = null // Обьект принятых данных

    var choosingMac: String = "" // Выбранный мак адрес устройства
    var BLEconnected: Boolean = false // Для отслеживания подключено ли устройство

    var StopCorutin = false // Для приостановки работы обавления UI

    var sendComand: Boolean = false // Для отслеживания нажата ли кнопка отправить команду

    var command = JSONObject()
}