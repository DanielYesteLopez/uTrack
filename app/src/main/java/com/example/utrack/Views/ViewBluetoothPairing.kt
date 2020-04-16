package com.example.utrack.Views

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.utrack.Model.Training
import com.example.utrack.R
import kotlinx.android.synthetic.main.activity_bluetooth_pairing.*

class ViewBluetoothPairing : AppCompatActivity() {

    private val REQUEST_CODE_ENABLE_BLUETOOTH:Int = 1
    private val REQUEST_CODE_DISCOVERABLE_BLUETOOTH:Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_pairing)
        //init bluetooth adapter
        val bAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        //cheak if blutooth is on/off
        if(bAdapter==null){
            bluetoothStatusTv.text = getText(R.string.blutooth_not_available)
        }
        else{
            bluetoothStatusTv.text = getText(R.string.bluetooth_available)
        }
        // set image according to bluetooth status
        if (bAdapter != null) {
            if(bAdapter.isEnabled){
                //Bluetooth is on
                bluetoothIv.setImageResource(R.drawable.icon_bluetooth_on)
            }
            else{
                //Bluetooth is off
                bluetoothIv.setImageResource(R.drawable.icon_bluetooth_off)
            }
        }
        //turn on blutooth
        turnOnBtn.setOnClickListener {
            if (bAdapter != null) {
                if(bAdapter.isEnabled){
                    //already enable
                    Toast.makeText(this,"Alreay on", Toast.LENGTH_LONG).show()
                } else{
                    // turn on
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(intent, REQUEST_CODE_ENABLE_BLUETOOTH)
                }
            }
        }
        //turn off blutooth
        turnOffBtn.setOnClickListener {
            if (bAdapter != null) {
                if(!bAdapter.isEnabled){
                    //already disable
                    Toast.makeText(this,"Already off", Toast.LENGTH_LONG).show()
                } else{
                    // turn off
                    bAdapter.disable()
                    bluetoothIv.setImageResource(R.drawable.icon_bluetooth_off)
                    Toast.makeText(this,"Already turned off", Toast.LENGTH_LONG).show()
                }
            }
        }
        //discoverable the bluetooth
        discoverableBtn.setOnClickListener {
            if (bAdapter != null) {
                if(!bAdapter.isDiscovering){
                    Toast.makeText(this,"Making you device discoverable", Toast.LENGTH_LONG).show()
                    val intent = Intent(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                    startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BLUETOOTH)
                }
            }
        }
        // paired devices
        pairedBtn.setOnClickListener {
            if (bAdapter != null) {
                if(bAdapter.isEnabled){
                    pairedTv.text = getString(R.string.paired_devices)
                    // get list of paired Devices
                    val pairedDevices: Set<BluetoothDevice>? = bAdapter.bondedDevices
                    pairedDevices?.forEach { device ->
                        val deviceName = device.name
                        val deviceAddress = device.address
                        val deviceclass = device.bluetoothClass
                        val deviceuuid = device.uuids
                        pairedTv.append("\nDevice: $deviceName, $deviceAddress, $deviceclass, $deviceuuid")
                    }
                    // TODO missing
                } else{
                    Toast.makeText(this,"Turn on bluetooth first", Toast.LENGTH_LONG).show()
                }
            }
        }
        backButtonBluetoothPage.setOnClickListener{
            onBackBluetoothButtonPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_CODE_ENABLE_BLUETOOTH ->
                if(resultCode == Activity.RESULT_OK){
                    bluetoothIv.setImageResource(R.drawable.icon_bluetooth_on)
                    Toast.makeText(this,"Bluetooth is on", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this,"Could not enable bluetooth", Toast.LENGTH_LONG).show()
                }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onBackBluetoothButtonPressed(){
        val intent = Intent(application, ViewTraining().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
