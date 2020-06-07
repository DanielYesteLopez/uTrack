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
import android.os.Parcelable
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.presenters.PresenterTraining
import kotlinx.android.synthetic.main.activity_bluetooth_pairing.*
import kotlin.collections.ArrayList


class ViewBluetoothPairing : SecondViewClass() {
    private val REQUESTCODEENABLEBLUETOOTH: Int = 1
    private val REQUESTCODEDISCOVERABLEBLUETOOTH: Int = 2
    //var mBluetoothConnection: BluetoothConnectionService? = null

    //init bluetooth adapter
    private var bAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var devicesList: ArrayList<BluetoothDevice> = ArrayList()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    // testing the connection
    //var bluetoothDevice : BluetoothDevice? = null


/*    companion object {
        var deviceUuid : UUID? = null
        var bluetoothSocket : BluetoothSocket? = null
        lateinit var bluetoothAdapter : BluetoothAdapter
        var isConnected : Boolean = false
        lateinit var address : String
        var device : BluetoothDevice? = null
        var hasCadenceDevice : String = "0"
    }*/


    // Create a BroadcastReceiver for ACTION_FOUND
/*    private val _broadcast_receiver1: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            // When discovery finds a device
            if (action == bAdapter.ACTION_STATE_CHANGED) {
                val state =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, bAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF ->
                        Log.d("Bluetooth", "onReceive: STATE OFF")
                    BluetoothAdapter.STATE_TURNING_OFF ->
                        Log.d("Bluetooth", "mBroadcastReceiver1: STATE TURNING OFF")
                    BluetoothAdapter.STATE_ON ->
                        Log.d("Bluetooth", "mBroadcastReceiver1: STATE ON")
                    BluetoothAdapter.STATE_TURNING_ON ->
                        Log.d("Bluetooth", "mBroadcastReceiver1: STATE TURNING ON")
                }
            }
        }
    }*/

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     * 2) for ACTION_FOUND Bluetooth Device Discovery.
     *
     */
    private val _broadcast_receiver1: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
                val mode =
                    intent.getIntExtra(
                        BluetoothAdapter.EXTRA_SCAN_MODE,
                        BluetoothAdapter.ERROR
                    )
                when (mode) {
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> {
                        Log.d(
                            "Bluetooth",
                            "receiver2: Discoverability Enabled."
                        )
                    }

                    BluetoothAdapter.SCAN_MODE_CONNECTABLE -> {
                        Log.d(
                            "Bluetooth",
                            "receiver2: Discoverability Disabled. Able to receive connections."
                        )
                    }
                    BluetoothAdapter.SCAN_MODE_NONE -> {
                        Log.d(
                            "Bluetooth",
                            "receiver2: Discoverability Disabled. Not able to receive connections."
                        )
                    }
                    BluetoothAdapter.STATE_CONNECTING -> {
                        Log.d(
                            "Bluetooth",
                            "receiver2: Connecting...."
                        )
                    }
                    BluetoothAdapter.STATE_CONNECTED -> {
                        Log.d(
                            "Bluetooth",
                            "Receiver2: Connected."
                        )
                    }
                }
            }
        }
    }

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) for ACTION_FOUND Bluetooth Device Discovery.
     *
     */
    private val _broadcast_receiver2 = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            Log.d("Bluetooth", "onReceive: ACTION FOUND.")
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val rssi: Int =
                        intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE).toInt()
                    /* log is for debug */
                    Log.d("Bluetooth1", "onReceive: " + device?.name + ": " + device?.address)
                    Log.d("Bluetooth2", "onReceive: $rssi")

                    if (device != null) {
                        if (!(devicesList.contains(device)) &&
                            !(device.name.isNullOrEmpty()) &&
                            (rssi >= -90)
                        ) {
                            /* rssi for testing only user do not need to see it */
                            devicesList.add(device)
                            arrayAdapter.add("${device.name} \n ${device.address} - $rssi")
                            arrayAdapter.notifyDataSetChanged()
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d("ACTIONDISCOVERYFINISHED", "HELLO FROM THE OTHER SIDE ")
                }
                BluetoothDevice.ACTION_UUID -> {
                    val extradevice: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val uuidExtra: Array<Parcelable>? =
                        intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID)
                    Log.d("DeviceExtra address", "${extradevice?.address} ${extradevice?.name} -- ")
                    if (uuidExtra != null) {
                        for (p: Parcelable in uuidExtra) {
                            Log.d("uuidExtra", "$p ----\n")
                        }
                    } else {
                        Log.d("fetching new uuid", "uuidExtra is still null")
                    }
                }
            }
        }
    }

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private val _broadcast_receiver3: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                val _device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                //3 cases:
                when (_device.bondState) {
                    //case1: bonded already
                    BluetoothDevice.BOND_BONDED -> {
                        Log.d("Bluetooth", "BroadcastReceiver: BOND_BONDED.")

                    }
                    //case2: creating a bone
                    BluetoothDevice.BOND_BONDING -> {
                        Log.d("Bluetooth", "BroadcastReceiver: BOND_BONDING.")
                    }
                    //case3: breaking a bond
                    BluetoothDevice.BOND_NONE -> {
                        Log.d("Bluetooth", "BroadcastReceiver: BOND_NONE.")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_pairing)

        bAdapter = BluetoothAdapter.getDefaultAdapter()
        //Broadcasts when bond state changes
        registerReceiver4BroadCasts()

        devicesList = ArrayList()
        arrayAdapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1)
        //turn on bluetooth and ask for permission
        turnBluetoothOn()
        getDiscoverableDevices()
        discoverBtn.setOnClickListener {
            discoverBtn.setText(R.string.refresh_discovering)
            getDiscoverableDevices()
        }
        // back button
        backButtonBluetoothPage.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        //turnBluetoothOff()
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        try {
            // unregister the ACTION_STATE receiver.
            // unregisterReceiver(_broadcast_receiver1)
            // unregister the SCAN_MODE receiver.
            unregisterReceiver(_broadcast_receiver1)
            // unregister the ACTION_FOUND receiver.
            unregisterReceiver(_broadcast_receiver2)
            // unregister the BOND_STATE receiver.
            unregisterReceiver(_broadcast_receiver3)
            //disconnect()
        } catch (e: IllegalArgumentException) {
            Log.d("Bluetooth", "exception unregister receivers")
        }
        //turnBluetoothOff()
        super.onDestroy()
    }

    override fun onStop() {
/*        try {
            // unregister the ACTION_STATE receiver.
            // unregisterReceiver(_broadcast_receiver1)
            // unregister the SCAN_MODE receiver.
            unregisterReceiver(_broadcast_receiver1)
            // unregister the ACTION_FOUND receiver.
            unregisterReceiver(_broadcast_receiver2)
            // unregister the BOND_STATE receiver.
            unregisterReceiver(_broadcast_receiver3)
            //disconnect()
        }catch (e : IllegalArgumentException){
            Log.d("Bluetooth", "exception unregister receivers" )
        }*/
        super.onStop()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUESTCODEENABLEBLUETOOTH -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (bAdapterIsEnabled()) {
                        bluetoothIv.setImageResource(R.drawable.icon_bluetooth_on) //Bluetooth is on
                        bAdapterEnable()
                    }
                } else {
                    bluetoothIv.setImageResource(R.drawable.icon_bluetooth_off) //Bluetooth is off
                }
            }
            REQUESTCODEDISCOVERABLEBLUETOOTH -> {
                // result is equal to time duration
                if (resultCode == 200) {
                    if (bAdapterIsEnabled()) {
                        bluetoothIv.setImageResource(R.drawable.icon_bluetooth_on) //Bluetooth is on
                    }
                } else {
                    bluetoothIv.setImageResource(R.drawable.icon_bluetooth_off) //Bluetooth is off
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun getDiscoverableDevices() {
        if (bAdapterIsEnabled()) {
            // create array list for new devices
            devicesList = ArrayList()
            // Create an array adapter
            arrayAdapter =
                ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1)
            // 1 query paired devices
            // queryPairedDevices()
            // 2 discover new devices
            discoverBluetoothDevice()
            // 3 add array adapter to pairedTv (which is a list view)
            pairedTv.adapter = arrayAdapter
            // 4 Set item click listener
            pairedTv.onItemClickListener = OnItemClickListener { _, _, position, _ ->
                bAdapterCancelDiscovery()
                val _device: BluetoothDevice = devicesList[position]
                //Log.d("bluetooth device",">>>>>>>>>>>>>>>>   ${device.uuids[0]} == / ${device.uuids.size}  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                //deviceUuid = MY_UUID_INSECURE
                //address = _device.address
                Log.d("Bluetooth connect", "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm")
                pairDevice(_device)
                //hasCadenceDevice = "1"
                //device = _device
                PresenterTraining.getInstance(this).onBluetoothDeviceChosen(_device)
                //ConnectToDevice(this).execute()
                onBackPressed()
                //bluetoothDevice = _device
                //mBluetoothConnection = BluetoothConnectionService(this@ViewBluetoothPairing)
                //startConnection()
            }
        }
    }

    fun pairDevice(_device: BluetoothDevice) {
        bAdapterCancelDiscovery()
        /*// To skip filtering based on name and supported feature flags (UUIDs),
        // don't include calls to setNamePattern() and addServiceUuid(),
        // respectively. This example uses Bluetooth.
        val deviceFilter: BluetoothDeviceFilter = BluetoothDeviceFilter.Builder()
            *//*.setNamePattern(Pattern.compile(_device?.name!!))*//*
            *//*.addServiceUuid(_device.uuids[0],null)*//*
            .build()

        // The argument provided in setSingleDevice() determines whether a single
        // device name or a list of device names is presented to the user as
        // pairing options.
        val pairingRequest: AssociationRequest = AssociationRequest.Builder()
            .addDeviceFilter(deviceFilter)
            .setSingleDevice(true)
            .build()

        // When the app tries to pair with the Bluetooth device, show the
        // appropriate pairing request dialog to the user.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            deviceManager.associate(pairingRequest,
                object : CompanionDeviceManager.Callback() {
                    override fun onDeviceFound(chooserLauncher: IntentSender) {
                        startIntentSenderForResult(chooserLauncher,
                            SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0)
                   }
                    override fun onFailure(error: CharSequence?) {
                        // Handle failure
                    }
                }, null)
        }*/
        Log.d("Bluetooth", "onItemClick: You Clicked on a device.")
        val deviceName: String = _device.name
        val deviceAddress: String = _device.address

        Log.d("Bluetooth", "onItemClick: deviceName = $deviceName")
        Log.d("Bluetooth", "onItemClick: deviceAddress = $deviceAddress")

        //create the bond.
        Log.d("Bluetooth", "Trying to pair with $deviceName")
        _device.createBond()
        val result = _device.fetchUuidsWithSdp()
        Log.d("paired Devices", "fetching uuids<  $result << < <  < < < < < < < < <")
        Log.d("paired Devices", "fetching uuids<  ${_device.address} -- ${_device.name} \n")
    }

    private fun queryPairedDevices() {
        bAdapterCancelDiscovery()
        val pairedDevices: Set<BluetoothDevice>? =
            bAdapter!!.bondedDevices // get list of paired Devices
        if (pairedDevices != null) {
            if (pairedDevices.isNotEmpty()) {
                pairedDevices.forEach { device: BluetoothDevice ->
                    val result = device.fetchUuidsWithSdp()
                    Log.d("paired Devices", "fetching uuids<  $result << < <  < < < < < < < < <")
                    Log.d(
                        "paired Devices",
                        "fetching uuids<  ${device.address} -- ${device.name} \n"
                    )
                    devicesList.add(device)
                    arrayAdapter.add("${device.name} \n ${device.address}")
                }
            }
        }
    }

    private fun discoverBluetoothDevice() {
        //make bluetooth discoverable
        if (bAdapterIsNotNull()) {
            if (bAdapterIsDiscovering()) {
                bAdapterCancelDiscovery()
                Log.d("Bluetooth", "checking permissions")
                checkBTPermissions()
                bAdapterStartDiscovery()
                // Register for broadcasts when a device is discovered.
                registerReceiver3BroadCasts()
            } else if (bAdapterIsNotDiscovering()) {
                Log.d("Bluetooth", "checking permissions")
                checkBTPermissions()
                bAdapterStartDiscovery()
                // Register for broadcasts when a device is discovered.
                registerReceiver3BroadCasts()
            }
        } else {
            Toast.makeText(
                this,
                "Bluetooth is not supported in this device2",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun makeBluetoothDiscoverable() {
        //make bluetooth discoverable
        if (bAdapterIsNotNull()) {
            if (bAdapterIsNotDiscovering()) {
                // turn on bluetooth and make it discoverable
                val intent = Intent(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200)
                startActivityForResult(intent, REQUESTCODEDISCOVERABLEBLUETOOTH)
                registerReceiver2BroadCasts()
            }
        } else {
            Toast.makeText(
                this,
                "Bluetooth is not supported in this device",
                Toast.LENGTH_LONG
            ).show()
        }
    }

//    private fun registerReceiver1BroadCasts(){
//        val filter = IntentFilter(BluetoothDevice.ACTION_STATE_CHANGED)
//        registerReceiver(_broadcast_receiver1, filter)
//    }

    private fun registerReceiver2BroadCasts() {
        val filter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        registerReceiver(_broadcast_receiver1, filter)
    }

    private fun registerReceiver3BroadCasts() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(_broadcast_receiver2, filter)
    }

    private fun registerReceiver4BroadCasts() {
        val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(_broadcast_receiver3, filter)
    }

    private fun bAdapterIsNull(): Boolean {
        return (bAdapter == null)
    }

    private fun bAdapterIsNotNull(): Boolean {
        return (bAdapter != null)
    }

    private fun bAdapterIsEnabled(): Boolean {
        if (bAdapterIsNotNull()) {
            return (bAdapter!!.isEnabled)
        }
        return false
    }

    private fun bAdapterIsNotEnabled(): Boolean {
        return !(bAdapterIsEnabled())
    }

    private fun bAdapterIsDiscovering(): Boolean {
        if (bAdapterIsNotNull()) {
            return (bAdapter!!.isDiscovering)
        }
        return false
    }

    private fun bAdapterIsNotDiscovering(): Boolean {
        return !(bAdapterIsDiscovering())
    }

    private fun bAdapterStartDiscovery() {
        if (bAdapterIsNotNull()) {
            Log.d("Bluetooth", "asking for discoverable")
            makeBluetoothDiscoverable()
            Log.d("Bluetooth", "start discovery")
            bAdapter!!.startDiscovery()
        }
    }

    private fun bAdapterCancelDiscovery() {
        if (bAdapterIsDiscovering()) {
            bAdapter!!.cancelDiscovery()
        }
    }

    /**
     * Enable Bluetooth by asking permission
     */
    private fun turnBluetoothOn() {
        //check if blutooth is on/off
        if (bAdapterIsNull()) {
            bluetoothStatusTv.text = getText(R.string.blutooth_not_available)
        } else {
            bluetoothStatusTv.text = getText(R.string.bluetooth_available)
            // set image according to bluetooth status
            if (bAdapterIsEnabled()) {
                bluetoothIv.setImageResource(R.drawable.icon_bluetooth_on) //Bluetooth is on
            } else {
                bluetoothIv.setImageResource(R.drawable.icon_bluetooth_off) //Bluetooth is off
                Log.d("Bluetooth", "turning on bluetooth")
                //turn on bluetooth request
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUESTCODEENABLEBLUETOOTH)
                Log.d("Bluetooth", "bluetooth turned on")

                //registerReceiver1BroadCasts()
            }
        }
    }

    /**
     * to turn off bluetooth no need for permission
     */
    private fun turnBluetoothOff() {
        if (bAdapterIsEnabled()) {
            // turn off
            bAdapter!!.disable()

            //registerReceiver1BroadCasts()
        }
        bluetoothIv.setImageResource(R.drawable.icon_bluetooth_off)
    }

    /**
     * once permission is asked
     * this method replace turn on bluetooth after permission is asked
     */
    private fun bAdapterEnable() {
        if (bAdapterIsNotEnabled()) {
            bAdapter!!.enable()
        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    private fun checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        } else {
            Log.d("Bluetooth", "checkBTPermissions: No need to check permissions.")
        }
    }
}

/*

    //create method for starting connection
    //***remember the conncction will fail and app will crash if you haven't paired first
    fun startConnection() {
        startBTConnection(bluetoothDevice, false)
    }

    */
/**
     * starting chat service method
     */
/*
    fun startBTConnection(device: BluetoothDevice?, secure : Boolean) {
        Log.d(
            "Bluetooth",
            "startBTConnection: Initializing RFCOM Bluetooth Connection."
        )
        mBluetoothConnection?.startClient(device, secure)
    }
*/

/*
    private fun disconnect() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket!!.close()
                bluetoothSocket = null
                isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            Log.d("preExecute","you should make a progress bar please")
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                Log.d("socket Training view", "connecting in background")
                if (bluetoothSocket == null || !isConnected) {

                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

                    val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(address)

                    bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(deviceUuid)

                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {

            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.d("data", "couldn't connect")
            } else {
                isConnected = true
            }
            Log.d("PosExecute","dissmis the progress bar")
        }
    }*/