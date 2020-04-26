package com.example.utrack.views

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import kotlinx.android.synthetic.main.activity_bluetooth_pairing.*


class ViewBluetoothPairing : SecondViewClass() {
    private val REQUEST_CODE_ENABLE_BLUETOOTH:Int = 1
    private val REQUEST_CODE_DISCOVERABLE_BLUETOOTH:Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_pairing)
        //init bluetooth adapter
        val bAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        //check if blutooth is on/off
        if(bAdapter==null){ bluetoothStatusTv.text = getText(R.string.blutooth_not_available)
        } else { bluetoothStatusTv.text = getText(R.string.bluetooth_available)
            // set image according to bluetooth status
            if(bAdapter.isEnabled){ bluetoothIv.setImageResource(R.drawable.icon_bluetooth_on) //Bluetooth is on
            } else{ bluetoothIv.setImageResource(R.drawable.icon_bluetooth_off) //Bluetooth is off
                turnBluetoothOn(bAdapter) //turn on bluetooth request
            }
        }
        // paired devices
        pairedBtn.setOnClickListener {
            if(bAdapter != null){
               if(!bAdapter.isEnabled) {
                   Toast.makeText(this, "Turn on bluetooth first", Toast.LENGTH_LONG).show()
               }
            }
            getPairedDevices(bAdapter)
        }
        // back button
        backButtonBluetoothPage.setOnClickListener{
            turnBluetoothOff(bAdapter) //turn off blutooth if user press back button
            onBackBluetoothButtonPressed() // go back to exercise
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

            REQUEST_CODE_DISCOVERABLE_BLUETOOTH ->
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

    private fun getPairedDevices(bAdapter :  BluetoothAdapter?){
        turnBluetoothOn(bAdapter)
        if (bAdapter != null){
            if (bAdapter.isEnabled) {
                val pairedDevices: Set<BluetoothDevice>? = bAdapter.bondedDevices // get list of paired Devices
                //val notpairedDevices: Set<BluetoothDevice>?
                val devices : Array<String> = databaseList()
                var device : BluetoothDevice
                devices[0] = getString(R.string.paired_devices)
                if (pairedDevices != null) {
                    for (i:Int in pairedDevices.indices){
                        device = pairedDevices.elementAt(i)
                        val deviceName = device.name
                        //val deviceAddress = device.address
                        devices[i+1] = "\nDevice: $deviceName"
                    }
                }
                // Create an array adapter
                val adapter: ArrayAdapter<String?> =
                    ArrayAdapter<String?>(
                        this,
                        android.R.layout.simple_list_item_1,
                        devices
                    )
                pairedTv.adapter = adapter
                // Set item click listener
                pairedTv.onItemClickListener = OnItemClickListener { parent, view, position, id ->
                    val deviceName: String = devices[position]
                    Toast.makeText(this@ViewBluetoothPairing, deviceName, Toast.LENGTH_SHORT).show()
                    // TODO after item selected pair device and go back to training page
                }
            }
        }
    }

    private fun turnBluetoothOn(bAdapter :  BluetoothAdapter? ){
        //check if blutooth is on/off
       if (bAdapter!=null){
           if(!bAdapter.isEnabled){
               //turn on bluetooth request
               val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
               startActivityForResult(intent, REQUEST_CODE_ENABLE_BLUETOOTH)
           }
       } else {
           Toast.makeText(this,"Bluetooth is not supported in this device", Toast.LENGTH_LONG).show()
       }
    }
    // de momento no es necesaria
    private fun makeBluetoothDiscoverable(bAdapter :  BluetoothAdapter? ){
        //make bluetooth discoverable
        if (bAdapter != null){
            if(!bAdapter.isDiscovering){
                // turn on bluetooth and make it discoverable
                val intent = Intent(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BLUETOOTH)
            }
        } else {
            Toast.makeText(this,"Bluetooth is not supported in this device", Toast.LENGTH_LONG).show()
        }
    }

    private fun turnBluetoothOff(bAdapter: BluetoothAdapter?){
        if (bAdapter != null) {
            if(bAdapter.isEnabled){
                // turn off
                bAdapter.disable()
                bluetoothIv.setImageResource(R.drawable.icon_bluetooth_off)
            }
        }
    }
}
