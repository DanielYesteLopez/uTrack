package com.example.utrack.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.presenters.PresenterTraining
import com.example.utrack.model.services.LocationService
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.trainingpage.*

class ViewTraining : SecondViewClass() {

    private val TAG = "MainActivity"
    val MY_PERMISSIONS_REQUEST_LOCATION = 99
    //private var presenterTraining : PresenterTraining? = null
    private var myBluetoothFragment: FragmentBluetooth? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        // hide navigation bar
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onCreateHideNavBar()
        super.onCreate(savedInstanceState)
        // start activity
        setContentView(R.layout.trainingpage)
        // ini
        PresenterTraining.getInstance(this@ViewTraining)
        myBluetoothFragment = FragmentBluetooth()
        // check bluetooth connection
        myBluetoothFragment.let { it?.show(supportFragmentManager, getString(R.string.notefication)) }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED*/
        ) {
            if (checkLocationPermission()){
                Log.d(TAG,"permission is been granted")
            }
        } else {
            startService()
        }
        PresenterTraining.getInstance(this@ViewTraining).registerSensorListenerAccelerate()

        locationUpdateReceiver.let{
            LocalBroadcastManager.getInstance(this
            ).registerReceiver(it, IntentFilter("LocationUpdated"))
        }

        predictedLocationReceiver.let{
            LocalBroadcastManager.getInstance(this
            ).registerReceiver(it, IntentFilter("PredictLocation"))
        }

        c_meter.base = SystemClock.elapsedRealtime()

        var isWorking = false
        var ispaused = true
        var pauseOffset: Long = 0

        // set layout visibility
        buttonStart.visibility = View.VISIBLE
        buttonStop.visibility = View.INVISIBLE
        buttonPause.visibility = View.INVISIBLE
        buttonResume.visibility = View.INVISIBLE
        textViewstartresume.visibility = View.VISIBLE
        textViewpause.visibility = View.INVISIBLE
        textViewstop.visibility = View.INVISIBLE

        // button llisteners
        buttonStart.setOnClickListener {
            if (!isWorking) {
                buttonStart.visibility = View.INVISIBLE
                buttonStop.visibility = View.VISIBLE
                buttonPause.visibility = View.VISIBLE
                buttonResume.visibility = View.INVISIBLE
                textViewstartresume.visibility = View.INVISIBLE
                textViewpause.visibility = View.VISIBLE
                textViewstop.visibility = View.VISIBLE
                textViewpause.setText(R.string.trainingpouse)
                // start timer (crono metro)
                c_meter.base = SystemClock.elapsedRealtime() - pauseOffset
                c_meter.start()
                isWorking = true
                ispaused = false
                PresenterTraining.getInstance(this@ViewTraining).onStartTrainingButtonPressed()
            }
        }

        buttonResume.setOnClickListener {
            if (!isWorking) {
                buttonStart.callOnClick()
                PresenterTraining.getInstance(this@ViewTraining).onResumeTrainingButtonPressed()
            }
        }

        buttonPause.setOnClickListener {
            if (isWorking) {
                buttonStart.visibility = View.INVISIBLE
                buttonStop.visibility = View.VISIBLE
                buttonPause.visibility = View.INVISIBLE
                buttonResume.visibility = View.VISIBLE
                textViewstartresume.visibility = View.INVISIBLE
                textViewpause.visibility = View.VISIBLE
                textViewstop.visibility = View.VISIBLE
                textViewpause.setText(R.string.trainingresume)
                c_meter.stop()
                pauseOffset = SystemClock.elapsedRealtime() - c_meter.base
                isWorking = false
                ispaused = true
                PresenterTraining.getInstance(this@ViewTraining).onPauseTrainingButtonPressed()
            }
        }

        buttonStop.setOnClickListener {
            if(isWorking || ispaused) {
                buttonPause.callOnClick()
                val myExerciseFragment = FragmentShowExercise()
                myExerciseFragment.show(supportFragmentManager, getString(R.string.notefication))
                PresenterTraining.getInstance(this@ViewTraining).onStopTrainingButtonPressed()
            }
        }
        backButtonTrainingPage.setOnClickListener {
            PresenterTraining.getInstance(this@ViewTraining).onBackTrainingButtonPressed()
        }
        // exit on create
    }


    private var locationUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val newLocation = intent.getParcelableExtra<Location>("location")
            val latLng = LatLng(newLocation?.latitude!!, newLocation.longitude)
//            findViewById<TextView>(R.id.location).text = latLng.toString()
//            findViewById<TextView>(R.id.liner).text = (presenter.let { it?.getAcceleration() }).toString()
//            findViewById<TextView>(R.id.speed_trapezi).text = (presenter.let { it?.getSpeedTrapezi() }).toString()
//            findViewById<TextView>(R.id.distance_trapezi).text = (presenter.let { it?.getPositionTrapeze() }).toString()
//            findViewById<TextView>(R.id.speed_gps).text = (presenter.let { it?.getSpeedGPS() }).toString()
//            findViewById<TextView>(R.id.distance_gps).text = (presenter.let { it?.getDistanceGPS() }).toString()
            val formatTemplate = "%2f%3s"
            findViewById<TextView>(R.id.cadenceratetext).text =
                formatTemplate.format(
                    PresenterTraining.getInstance(this@ViewTraining).getAcceleration(),"rpm")
            findViewById<TextView>(R.id.speedratetext).text =
                formatTemplate.format(
                    PresenterTraining.getInstance(this@ViewTraining).getSpeedGPS(),"m/s")
            //findViewById<>(R.id.).text = (presenterTraining.let { it?.getDistanceGPS() }).toString()
            PresenterTraining.getInstance(this@ViewTraining).onReceiveLocation(latLng)
        }
    }

    private var predictedLocationReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val predictedLocation = intent.getParcelableExtra<Location>("location")
            val latLng = LatLng(predictedLocation?.latitude!!, predictedLocation.longitude)
            PresenterTraining.getInstance(this@ViewTraining).onReceivePredictedLocation(latLng)
        }
    }

    private val serviceConnection = object : ServiceConnection {
        /**
         *  This is called when the connection with the service has been
         *  established, giving us the service object we can use to
         * interact with the service.  Because we have bound to a explicit
         * service that we know is running in our own process, we can
         * cast its IBinder to a concrete class and directly access it.
         */
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val name = className.className
            Log.d(TAG,"connection established")
            if (name.endsWith("LocationService")) {
                PresenterTraining.getInstance(this@ViewTraining).onServiceConnected(service)
            }
        }

        /**
         * This is called when the connection with the service has been
         * unexpectedly disconnected -- that is, its process crashed.
         * Because it is running in our same process, we should never
         * see this happen.
         */
        override fun onServiceDisconnected(className: ComponentName) {
            if (className.className == "LocationService") {
                PresenterTraining.getInstance(this@ViewTraining).onServiceDisconnected()
            }
        }
    }

    public override fun onDestroy() {
        try {
            unregisterReceiver(locationUpdateReceiver)
            unregisterReceiver(predictedLocationReceiver)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        super.onDestroy()
    }

    override fun onResume() {
        PresenterTraining.getInstance(this@ViewTraining).registerSensorListenerAccelerate()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED*/
        ) {
            if (checkLocationPermission()){
                Log.d(TAG,"permission is been granted")
            }
        } else {
            Log.d(TAG,"service is being foreground so no need to resume")
            // service is being foreground so no need to resume
            //startService()

        }
        super.onResume()
    }

    override fun onPause() {
        PresenterTraining.getInstance(this@ViewTraining).unRegisterSensorListenerAccelerate()
        super.onPause()
    }

    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.text_location_permission)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        requestPermissionFineLocationAccess()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestPermissionFineLocationAccess()
            }
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    Log.d(TAG, "Permission was granted, yay")
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        startService()
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // TODO tell user is being an as%#$ll
                    Log.d(TAG,"permission denied go to hell")
                }
                return
            }
        }
    }

    private fun requestPermissionFineLocationAccess() {
        //Prompt the user once explanation has been shown
        ActivityCompat.requestPermissions(
            this@ViewTraining,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    private fun startService() {
        Log.d(TAG,"service will start")
        val locationServiceStart = Intent(this.application, LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.application.startForegroundService(locationServiceStart)
        } else {
            this.application.startService(locationServiceStart)
        }
        this.application.bindService(locationServiceStart, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}
