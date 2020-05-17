package com.example.utrack.model

import android.bluetooth.BluetoothDevice

class Sensor {
    private var bluetoothDevice : BluetoothDevice? = null

    fun getABluetoothDevice() : BluetoothDevice? {
        return this.bluetoothDevice
    }

    fun setABluetoothDevice(_device : BluetoothDevice) {
        this.bluetoothDevice = _device
    }


}