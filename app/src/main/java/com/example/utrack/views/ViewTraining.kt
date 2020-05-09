package com.example.utrack.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.presenters.PresenterTraining
import com.example.utrack.services.LocationService
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.trainingpage.*
import kotlin.math.pow
import kotlin.math.sqrt

class ViewTraining : SecondViewClass(), SensorEventListener {

    private val TAG = "MainActivity"
    val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private var locationService: LocationService? = null
    private var locationUpdateReceiver: BroadcastReceiver? = null
    private var predictedLocationReceiver: BroadcastReceiver? = null
    private lateinit var mSensorManager: SensorManager
    private var mLinerAcceleration: Sensor? = null
    private var mGyroScope: Sensor? = null
    private var resume = false
    private var counterdatareaded : Int = 0
    private var accelerityAct : Float = 0.0F
    private var accelerityAnt : Float = 0.0F
    private var accelerityList : ArrayList<Float>? = null
    private var velocityActT : Float = 0.0F
    private var positionActT : Float = 0.0F
    private var velocityListT : ArrayList<Float>? = null
    private var positionListT : ArrayList<Float>? = null
    private var epsilon : Float = 0.1F

    private var presenterTraining = PresenterTraining()
    private var myBluetoothFragment = FragmentBluetooth()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        // hide navigation bar
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onCreateHideNavBar()
        super.onCreate(savedInstanceState)
        // start activity
        setContentView(R.layout.trainingpage)
        // check bluetooth connection
        myBluetoothFragment.show(supportFragmentManager, getString(R.string.notefication))
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

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

        locationUpdateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val newLocation :Location? = intent.getParcelableExtra("location")
                val latLng = LatLng(newLocation!!.latitude, newLocation.longitude)
                this@ViewTraining.locationService?.let{
                    if (it.isLogging) {
                        Log.d(TAG,"->> $latLng")
                        // --- >> findViewById<TextView>(R.id.location).text = latLng.toString()
                        Log.d(TAG,"is Logging")
                    }
                }
            }
        }

        predictedLocationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val predictedLocation: Location? = intent.getParcelableExtra("location")
                val latLng = LatLng(predictedLocation!!.latitude, predictedLocation.longitude)
                Log.d(TAG,"$latLng")
            }
        }

        locationUpdateReceiver?.let{
            LocalBroadcastManager.getInstance(
                this
            ).registerReceiver(
                it,
                IntentFilter("LocationUpdated"
                )
            )
        }

        predictedLocationReceiver?.let{
            LocalBroadcastManager.getInstance(
                this
            ).registerReceiver(
                it,
                IntentFilter("PredictLocation"
                )
            )
        }

        checkSensor()
        mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)?.let {
            mLinerAcceleration = it
        }
        mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let {
            mGyroScope = it
        }
        accelerityAct = 0.0F
        accelerityAnt = 0.0F
        accelerityList = ArrayList()
        accelerityList?.add(accelerityAct)
        counterdatareaded = 0
        velocityActT = 0.0F
        positionActT  = 0.0F
        velocityListT  = ArrayList()
        positionListT = ArrayList()
        velocityListT?.add(velocityActT)
        positionListT?.add(positionActT)
        epsilon = 0.01F

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
                resumeReading()
                this@ViewTraining.locationService?.startLogging()
                presenterTraining.onStartTrainingButtonPressed(this@ViewTraining)
            }
        }

        buttonResume.setOnClickListener {
            if (!isWorking) {
                buttonStart.callOnClick()
                presenterTraining.onResumeTrainingButtonPressed(this@ViewTraining)
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
                presenterTraining.onPauseTrainingButtonPressed(this@ViewTraining)
            }
        }

        buttonStop.setOnClickListener {
            if(isWorking || ispaused) {
                buttonPause.callOnClick()
                pauseReading()
                this@ViewTraining.locationService?.stopLogging()
                presenterTraining.onStopTrainingButtonPressed(this@ViewTraining)
            }
        }
        backButtonTrainingPage.setOnClickListener {
            presenterTraining.onBackTrainingButtonPressed(this@ViewTraining)
        }
        // exit on create
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
                locationService = (service as LocationService.LocationServiceBinder).service
                this@ViewTraining.locationService?.startUpdatingLocation()
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
                this@ViewTraining.locationService?.stopUpdatingLocation()
                locationService = null
            }
        }
    }

    public override fun onDestroy() {
        try {
            if (locationUpdateReceiver != null) {
                unregisterReceiver(locationUpdateReceiver)
            }

            if (predictedLocationReceiver != null) {
                unregisterReceiver(predictedLocationReceiver)
            }
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mLinerAcceleration, SensorManager.SENSOR_DELAY_NORMAL)
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
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }

    private fun resumeReading() {
        this.resume = true
    }

    private fun pauseReading() {
        this.resume = false
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

    private fun checkSensor() {
        // see if the phone has the sensor
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null) {
            // Success!
            Log.d("Sensor Manager", "yes liner accelerometer")
        } else {
            // Failure!
            Log.d("Sensor Manager", "no liner accelerometer")
        }
        // see if the phone has the sensor
        /*if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            // Success!
            Log.d("Sensor Manager", "yes gyroscope")
        } else {
            // Failure!
            Log.d("Sensor Manager", "no gyroscope")
        }*/
    }

    private fun computeSumXYZ(a: Float, b: Float, c: Float): Float {
        val a_2 = a.pow(2)
        val b_2 = b.pow(2)
        val c_2 = c.pow(2)
        var res = (a_2 + b_2 + c_2)
        res = sqrt(res)
        return res
    }

    private fun doubleIntegration(
        _acc_act : Float,
        _acc_ant : Float,
        _velocity : Float,
        _position : Float,
        _delta_t : Int
    ) : ArrayList<Float> {
        val count = counterdatareaded
        val accAct = _acc_act
        val accAnt = _acc_ant

        var velocity = _velocity
        var position = _position
        if (count == 1) {
            velocity = _velocity
            position = _position
        }
        if (count >= 2){
            if(accAct < accAnt){
                velocity = (integrationTrapeze(accAnt, accAct, _delta_t))
            }else {
                velocity = (integrationTrapeze(accAnt, accAct, _delta_t))
            }
            position = (integrationTrapeze(_velocity, velocity, _delta_t))
        }
        val res : ArrayList<Float> = ArrayList()
        res.add(velocity)
        res.add(position)
        return res
    }

    /**
     * h = 1 estamos usando dos puntos a y b sin tomar puntos en medio
     *
     */
    private fun integrationTrapeze(_acc_ant : Float, _acc_act : Float, _delta_t: Int) : Float{
        val fa = _acc_ant
        val fb = _acc_act
        var value = (fa + fb) / 2
        value *= _delta_t
        return value
    }

    /**
     * sensor listener
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("sensor:", "accuracy changed")
    }

    /**
     * sensor listener
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && resume) {
            if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                accelerityAct = computeSumXYZ(event.values[0], event.values[1], event.values[2])
                if (accelerityAct < epsilon){
                    accelerityAct = 0.0F
                }
                counterdatareaded += 1
                // delta_t change to real value
                val valuesT =
                    doubleIntegration(
                        accelerityAct,
                        accelerityAnt,
                        velocityActT,
                        positionActT,
                        100
                    )
                velocityActT = valuesT[0]
                positionActT = valuesT[1]
                accelerityList?.add(accelerityAct)
                velocityListT?.add(velocityActT)
                positionListT?.add(positionActT)
                // --- >> findViewById<TextView>(R.id.liner).text = accelerityAct.toString()
                val aux = velocityActT/10
                // --- >> findViewById<TextView>(R.id.speed_trapezi).text = aux.toString()
                // --- >> findViewById<TextView>(R.id.distance_trapezi).text = positionAct_t.toString()
                accelerityAnt = accelerityAct
            }
        }
    }
}
