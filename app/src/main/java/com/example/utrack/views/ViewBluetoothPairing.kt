package com.example.utrack.views

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.presenters.PresenterTraining
import kotlinx.android.synthetic.main.activity_bluetooth_pairing.*
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.Thread.sleep
import java.util.*
import kotlin.collections.ArrayList


class ViewBluetoothPairing : SecondViewClass() {
    private val REQUESTCODEENABLEBLUETOOTH: Int = 1
    private val REQUESTCODEDISCOVERABLEBLUETOOTH: Int = 2
//    private val SELECT_DEVICE_REQUEST_CODE = 42
//
//    private val deviceManager: CompanionDeviceManager by lazy(LazyThreadSafetyMode.NONE) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            getSystemService(CompanionDeviceManager::class.java)
//        } else {
//        }
//    }


    //init bluetooth adapter
    private var bAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var devicesList: ArrayList<BluetoothDevice> = ArrayList()
    private lateinit var arrayAdapter: ArrayAdapter<String>

    companion object {
        var deviceUuid : UUID? = null
        var bluetoothSocket : BluetoothSocket? = null
        lateinit var bluetoothAdapter : BluetoothAdapter
        var isConnected : Boolean = false
        lateinit var address : String
        var device : BluetoothDevice? = null
        var hasCadenceDevice : String = "0"
    }
    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     * 2) for ACTION_FOUND Bluetooth Device Discovery.
     *
     */
    private val receiver2: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
                val mode =
                    intent.getIntExtra(
                        BluetoothAdapter.EXTRA_SCAN_MODE,
                        BluetoothAdapter.ERROR
                    )
                when (mode)
                {
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE ->
                    {
                        Log.d(
                            "Bluetooth",
                            "receiver2: Discoverability Enabled."
                        )
                    }

                    BluetoothAdapter.SCAN_MODE_CONNECTABLE ->
                    {
                        Log.d("Bluetooth",
                            "receiver2: Discoverability Disabled. Able to receive connections."
                        )
                    }

                    BluetoothAdapter.SCAN_MODE_NONE ->
                    {
                        Log.d("Bluetooth",
                            "receiver2: Discoverability Disabled. Not able to receive connections."
                        )
                    }

                    BluetoothAdapter.STATE_CONNECTING ->
                    {
                        Log.d("Bluetooth",
                            "receiver2: Connecting...."
                        )
                    }

                    BluetoothAdapter.STATE_CONNECTED ->
                    {
                        Log.d("Bluetooth",
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
    private val receiver1 = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            Log.d("Bluetooth", "onReceive: ACTION FOUND.")
            when (action)
            {
                BluetoothDevice.ACTION_FOUND ->
                {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val rssi: Int =
                        intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE).toInt()
                    /* log is for debug */
                    Log.d("Bluetooth1", "onReceive: " + device?.name + ": " + device?.address)
                    Log.d("Bluetooth2", "onReceive: $rssi")

                    if (device != null)
                    {
                        if (!(devicesList.contains(device)) &&
                            !(device.name.isNullOrEmpty()) &&
                            (rssi >= -90))
                        {
                            /* rssi for testing only user do not need to see it */
                            devicesList.add(device)
                            arrayAdapter.add("${device.name} \n ${device.address} - $rssi")
                            arrayAdapter.notifyDataSetChanged()
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED ->
                {
                    Log.d("ACTIONDISCOVERYFINISHED","HELLO FROM THE OTHER SIDE ")
                }
                BluetoothDevice.ACTION_UUID ->
                {
                    val extradevice : BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val uuidExtra : Array<Parcelable>? = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID)
                    Log.d("DeviceExtra address", "${extradevice?.address} ${extradevice?.name} -- ")
                    if (uuidExtra != null) {
                        for (p : Parcelable  in uuidExtra) {
                            Log.d("uuidExtra","$p ----\n")
                        }
                    } else {
                        Log.d("fetching new uuid","uuidExtra is still null")
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_pairing)

        bAdapter = BluetoothAdapter.getDefaultAdapter()
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
        turnBluetoothOff()
        super.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
//        if(!this.bAdapterIsDiscovering()){
//            getDiscoverableDevices()
//        }
        super.onResume()
    }

    override fun onDestroy() {
        try {
            // unregister the ACTION_FOUND receiver.
            unregisterReceiver(receiver1)
            // unregister the Discoverability receiver.
            unregisterReceiver(receiver2)

            //disconnect()
        }catch (e : IllegalArgumentException){
            Log.d("Bluetooth", "exception unregister receivers" )
        }

        turnBluetoothOff()
        super.onDestroy()
    }

    override fun onStop() {
        try {
            // unregister the ACTION_FOUND receiver.
            unregisterReceiver(receiver1)
            // unregister the Discoverability receiver.
            unregisterReceiver(receiver2)

            //disconnect()
        }catch (e : IllegalArgumentException){
            Log.d("Bluetooth", "exception unregister receivers" )
        }
        super.onStop()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUESTCODEENABLEBLUETOOTH ->
            {
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getDiscoverableDevices() {
        if (bAdapterIsEnabled()) {
            // create array list for new devices
            devicesList = ArrayList()
            // Create an array adapter
            arrayAdapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1)
            // 1 query paired devices
            // queryPairedDevices()
            // 2 discover new devices
            discoverBluetoothDevice()
            // 3 add array adapter to pairedTv (which is a list view)
            pairedTv.adapter = arrayAdapter
            // 4 Set item click listener
            pairedTv.onItemClickListener = OnItemClickListener { _, _, position, _ ->
                bAdapterCancelDiscovery()
                val device: BluetoothDevice = devicesList[position]
                Log.d("bluetooth device",">>>>>>>>>>>>>>>>   ${device.uuids}>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                //deviceUuid = UUID.randomUUID()
                //address = device.address!!
                //Log.d("going to connect","if you get an error go to fit")
                //ConnectToDevice(this).execute()
                //sleep(1000)
                PresenterTraining.getInstance(this).onBluetoothDeviceChosen(device)
                onBackPressed()
            }
        }
    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun pairDevice(_device : BluetoothDevice?) {
//        // To skip filtering based on name and supported feature flags (UUIDs),
//        // don't include calls to setNamePattern() and addServiceUuid(),
//        // respectively. This example uses Bluetooth.
//        val deviceFilter: BluetoothDeviceFilter = BluetoothDeviceFilter.Builder()
//            .setNamePattern(Pattern.compile(_device?.name!!))
//            .addServiceUuid(_device.uuids[0],null)
//            .build()
//
//        // The argument provided in setSingleDevice() determines whether a single
//        // device name or a list of device names is presented to the user as
//        // pairing options.
//        val pairingRequest: AssociationRequest = AssociationRequest.Builder()
//            .addDeviceFilter(deviceFilter)
//            .setSingleDevice(true)
//            .build()
//
//        // When the app tries to pair with the Bluetooth device, show the
//        // appropriate pairing request dialog to the user.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            deviceManager.associate(pairingRequest,
//                object : CompanionDeviceManager.Callback() {
//
//                    override fun onDeviceFound(chooserLauncher: IntentSender) {
//                        startIntentSenderForResult(chooserLauncher,
//                            SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0)
//                    }
//
//                    override fun onFailure(error: CharSequence?) {
//                        // Handle failure
//                    }
//                }, null)
//        }
//
//    }

    private fun queryPairedDevices(){
        bAdapterCancelDiscovery()
        val pairedDevices: Set<BluetoothDevice>? = bAdapter!!.bondedDevices // get list of paired Devices
        if (pairedDevices != null) {
            if (pairedDevices.isNotEmpty()) {
                pairedDevices.forEach { device : BluetoothDevice ->
                    val result = device.fetchUuidsWithSdp()
                    Log.d("paired Devices","fetching uuids<  $result << < <  < < < < < < < < <")
                    Log.d("paired Devices","fetching uuids<  ${device.address} -- ${device.name} \n")
                    devicesList.add(device)
                    arrayAdapter.add("${device.name} \n ${device.address}")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun discoverBluetoothDevice() {
        //make bluetooth discoverable
        if (bAdapterIsNotNull()) {
            bAdapterCancelDiscovery()
            Log.d("Bluetooth", "checking permissions")
            checkBTPermissions()
            bAdapterStartDiscovery()
            // Register for broadcasts when a device is discovered.
            registerReceiver1BroadCasts()
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

    private fun registerReceiver1BroadCasts(){
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver1, filter)
    }

    private fun registerReceiver2BroadCasts(){
        val filter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        registerReceiver(receiver2, filter)
    }

    private fun bAdapterIsNull() : Boolean{
        return (bAdapter == null)
    }

    private fun bAdapterIsNotNull() : Boolean{
        return (bAdapter != null)
    }

    private fun bAdapterIsEnabled() : Boolean{
        if (bAdapterIsNotNull()){
            return (bAdapter!!.isEnabled)
        }
        return false
    }

    private fun bAdapterIsNotEnabled() : Boolean{
        return !(bAdapterIsEnabled())
    }

    private fun bAdapterIsDiscovering() : Boolean{
        if (bAdapterIsNotNull()){
            return (bAdapter!!.isDiscovering)
        }
        return false
    }

    private fun bAdapterIsNotDiscovering() : Boolean{
        return !(bAdapterIsDiscovering())
    }

    private fun bAdapterStartDiscovery(){
        if (bAdapterIsNotNull()) {
            Log.d("Bluetooth", "asking for discoverable")
            makeBluetoothDiscoverable()
            Log.d("Bluetooth", "start discovery")
            bAdapter!!.startDiscovery()
        }
    }

    private fun bAdapterCancelDiscovery(){
        if (bAdapterIsDiscovering()){
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
        }
        bluetoothIv.setImageResource(R.drawable.icon_bluetooth_off)
    }

    /**
     * once permission is asked
     * this method replace turn on bluetooth after permission is asked
     */
    private fun bAdapterEnable(){
        if(bAdapterIsNotEnabled()){
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
}