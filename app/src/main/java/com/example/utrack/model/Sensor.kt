@file:Suppress("DEPRECATION")

package com.example.utrack.model

import com.google.android.gms.fitness.data.BleDevice

class Sensor {
    private var bluetoothDevice : BleDevice? = null

    fun getABluetoothDevice() : BleDevice? {
        return this.bluetoothDevice
    }

    fun setABluetoothDevice(_device : BleDevice) {
        this.bluetoothDevice = _device
    }


}