package com.example.utrack.views

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.presenters.PresenterTraining
import kotlinx.android.synthetic.main.activity_bluetooth_pairing.*


class ViewBluetoothPairing : SecondViewClass() {

    private var presenterTraining = PresenterTraining()
    private val REQUESTCODEENABLEBLUETOOTH: Int = 1
    private val REQUESTCODEDISCOVERABLEBLUETOOTH: Int = 2

    //init bluetooth adapter
    private var bAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var devicesList: ArrayList<BluetoothDevice> = ArrayList()

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private val receiver2: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
                val mode =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR)
                when (mode) {
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE ->
                        Log.d("Bluetooth",
                            "receiver2: Discoverability Enabled."
                        )
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE ->
                        Log.d("Bluetooth",
                            "receiver2: Discoverability Disabled. Able to receive connections."
                        )
                    BluetoothAdapter.SCAN_MODE_NONE ->
                        Log.d("Bluetooth",
                            "receiver2: Discoverability Disabled. Not able to receive connections."
                        )
                    BluetoothAdapter.STATE_CONNECTING ->
                        Log.d("Bluetooth",
                            "receiver2: Connecting...."
                        )
                    BluetoothAdapter.STATE_CONNECTED ->
                        Log.d("Bluetooth",
                            "Receiver2: Connected."
                        )
                }
            }
        }
    }


    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver1 = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            Log.d("Bluetooth", "onReceive: ACTION FOUND.");
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.d("Bluetooth",
                        "onReceive: " + device.name + ": " + device.address)
                    devicesList.add(device)
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_pairing)
        bAdapter = BluetoothAdapter.getDefaultAdapter()
        //check if blutooth is on/off
        if (bAdapter == null) {
            bluetoothStatusTv.text = getText(R.string.blutooth_not_available)
        } else {
            bluetoothStatusTv.text = getText(R.string.bluetooth_available)
            // set image according to bluetooth status
            if (bAdapter!!.isEnabled) {
                bluetoothIv.setImageResource(R.drawable.icon_bluetooth_on) //Bluetooth is on
            } else {
                bluetoothIv.setImageResource(R.drawable.icon_bluetooth_off) //Bluetooth is off
                turnBluetoothOn() //turn on bluetooth request
            }
        }

        discoverBtn.setOnClickListener {
            discoverBtn.setText(R.string.refreshDiscovering)
            if (bAdapter != null) {
                if (!bAdapter!!.isEnabled) {
                    Toast.makeText(
                        this,
                        "Turn on bluetooth first",
                        Toast.LENGTH_LONG
                    ).show()
                }
                getDiscoverableDevices()
            }
        }
        // back button
        backButtonBluetoothPage.setOnClickListener {
            turnBluetoothOff() //turn off bluetooth if user press back button
            onBackBluetoothButtonPressed() // go back to exercise
        }
    }

    override fun onBackPressed() {
        turnBluetoothOff()
        super.onBackPressed()
    }


    override fun onDestroy() {
        super.onDestroy()
        // unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver1)
        unregisterReceiver(receiver2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUESTCODEENABLEBLUETOOTH ->
                if (resultCode == Activity.RESULT_OK) {
                    if (bAdapter!!.isEnabled) {
                        bluetoothIv.setImageResource(R.drawable.icon_bluetooth_on)
                        Toast.makeText(
                            this,
                            "Bluetooth is on",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Could not enable bluetooth",
                        Toast.LENGTH_LONG
                    ).show()
                }
            REQUESTCODEDISCOVERABLEBLUETOOTH ->
                if (resultCode == Activity.RESULT_OK) {
                    if (bAdapter!!.isEnabled) {
                        bluetoothIv.setImageResource(R.drawable.icon_bluetooth_on)
                        Toast.makeText(
                            this,
                            "Bluetooth is on",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Could not enable discovering",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onBackBluetoothButtonPressed() {
        presenterTraining.onBackBluetoothButtonPressed(applicationContext)
    }

    private fun turnBluetoothOn() {
        //check if blutooth is on/off
        if (bAdapter != null) {
            if (!bAdapter!!.isEnabled) {
                //turn on bluetooth request
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUESTCODEENABLEBLUETOOTH)
            }
        } else {
            Toast.makeText(
                this,
                "Bluetooth is not supported in this device",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun turnBluetoothOff() {
        if (bAdapter != null) {
            if (bAdapter!!.isEnabled) {
                // turn off
                bAdapter!!.disable()
                bluetoothIv.setImageResource(R.drawable.icon_bluetooth_off)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun getDiscoverableDevices() {
        if (bAdapter != null) {
            if (bAdapter!!.isEnabled) {
                // query paired devices
                val pairedDevices: Set<BluetoothDevice>? =
                    bAdapter!!.bondedDevices // get list of paired Devices
                devicesList = ArrayList()
                if (pairedDevices != null) {
                    if (pairedDevices.isNotEmpty()) {
                        pairedDevices.forEach { device ->
                            devicesList.add(device)
                        }
                    }
                }
                // discover devices
                discoverBluetoothDevice()
                // Create an array adapter
                val adapter: ArrayAdapter<BluetoothDevice> =
                    ArrayAdapter<BluetoothDevice>(
                        this,
                        android.R.layout.simple_list_item_1,
                        devicesList
                    )
                pairedTv.adapter = adapter

                // Set item click listener
                pairedTv.onItemClickListener = OnItemClickListener { _, _, position, _ ->
                    //val device: BluetoothDevice = devicesList[position]
                    //val deviceName = device.name
                    //val deviceHardwareAddress = device.address // MAC address
                    // TODO after item selected pair device and go back to training page
                    Toast.makeText(
                        this@ViewBluetoothPairing,
                        "NOT IMPLEMENTED YET",
                        Toast.LENGTH_SHORT
                    ).show()
                    // take user back to training page
                }
            }
        }
    }

    private fun makeBluetoothDiscoverable() {
        //make bluetooth discoverable
        if (bAdapter != null) {
            if (!bAdapter!!.isDiscovering) {
                // turn on bluetooth and make it discoverable
                val intent = Intent(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                startActivityForResult(intent, REQUESTCODEDISCOVERABLEBLUETOOTH)

                val filter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
                registerReceiver(receiver2, filter)
            }
        } else {
            Toast.makeText(
                this,
                "Bluetooth is not supported in this device",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun discoverBluetoothDevice() {
        //make bluetooth discoverable
        if (bAdapter != null) {
            if (bAdapter!!.isDiscovering) {
                bAdapter!!.cancelDiscovery()
            }
            checkBTPermissions()
            makeBluetoothDiscoverable()
            bAdapter!!.startDiscovery()
            // Register for broadcasts when a device is discovered.
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver1, filter)
        } else {
            Toast.makeText(
                this,
                "Bluetooth is not supported in this device",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            var permissionCheck =
                checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
            permissionCheck += checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")
            if (permissionCheck != 0) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1001
                ) //Any number
            }
        }
    }
}