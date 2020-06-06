package com.example.utrack.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.example.utrack.R
import com.example.utrack.mc.SecondViewClass
import com.example.utrack.model.services.LocationService
import com.example.utrack.presenters.PresenterTraining
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.trainingpage.*
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Mapping
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType


class ViewTraining : SecondViewClass() {

    private val TAG = "MainActivity"
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99

    private var locationManager: LocationManager? = null
    private var bluetoothDevice : BluetoothDevice? = null
    private lateinit var anyChartView : AnyChartView
    private lateinit var seriesData : ArrayList<DataEntry>

    private var isWorking = false
    private var isPaused = false

    private lateinit var dataSet: Set
    private lateinit var series1: Line
    private lateinit var series2: Line
    private lateinit var series3: Line
    private lateinit var series1Mapping: Mapping
    private lateinit var series2Mapping: Mapping
    private lateinit var series3Mapping: Mapping

    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable


    @SuppressLint("SourceLockedOrientationActivity", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        // hide navigation bar
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onCreateHideNavBar()
        super.onCreate(savedInstanceState)
        // start activity
        setContentView(R.layout.trainingpage)
        // ini
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
            clearDataTraining()
            locationManager = this.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            assert(locationManager != null)
            if (canGetLocation()) {
                startService()
            } else {
                showSettingsAlert()
            }
        }
        mHandler = Handler()
        PresenterTraining.getInstance(this@ViewTraining)
        val myBluetoothFragment: FragmentBluetooth = FragmentBluetooth()
        // check bluetooth connection
        myBluetoothFragment.show(supportFragmentManager, getString(R.string.notefication))

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
        var pauseOffset: Long = 0

        anyChartView = findViewById(R.id.any_chart_view)
        anyChartView.setProgressBar(findViewById(R.id.progress_bar))
        anyChartView.setBackgroundColor(R.color.rosauserColor)

        val cartesian : Cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.padding(10, 20, 5, 20)
        cartesian.crosshair().enabled(true)
        cartesian.crosshair().yLabel(true).yStroke(null as Stroke?, null, null, null as String?, null as String?)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.xAxis(0).title("time")
        cartesian.xAxis(0).labels().padding(1,1,1,1)
        cartesian.yAxis(0).labels().padding(1,1,1,1)
        seriesData = ArrayList<DataEntry>()
        seriesData.add(CustomDataEntry("0",0,0,0))

        dataSet = Set.instantiate()
        dataSet.data(seriesData)
        series1Mapping = dataSet.mapAs("{ x: 'x', value: 'value' }")
        series2Mapping = dataSet.mapAs("{ x: 'x', value: 'value2' }")
        series3Mapping= dataSet.mapAs("{ x: 'x', value: 'value3' }")

        series1 = cartesian.line(series1Mapping)
        series1.name("speed")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)
            .fontColor("black")

        series2 = cartesian.line(series2Mapping)
        series2.name("acceleration")
        series2.hovered().markers().enabled(true)
        series2.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series2.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)
            .fontColor("black")

        series3 = cartesian.line(series3Mapping)
        series3.name("distance")
        series3.hovered().markers().enabled(true)
        series3.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series3.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)
            .fontColor("black")

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(7.0)
        cartesian.legend().padding(0.0, 0.0, 5.0, 0.0)
        anyChartView.setChart(cartesian)

        // set layout visibility
        buttonStart.visibility = View.VISIBLE
        buttonStop.visibility = View.INVISIBLE
        buttonPause.visibility = View.INVISIBLE
        buttonResume.visibility = View.INVISIBLE
        textViewstartresume.visibility = View.VISIBLE
        textViewpause.visibility = View.INVISIBLE
        textViewstop.visibility = View.INVISIBLE

        PresenterTraining.getInstance(this).createNewSession()

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
                isPaused = false
                PresenterTraining.getInstance(this@ViewTraining).onStartTrainingButtonPressed()
                mRunnable = Runnable {
                    addPoint()
                    mHandler.postDelayed(mRunnable, 1000)
                }
                mHandler.postDelayed(mRunnable, 1000)
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
                isPaused = true
                PresenterTraining.getInstance(this@ViewTraining).onPauseTrainingButtonPressed()
                mHandler.removeCallbacks(mRunnable)
            }
        }

        buttonStop.setOnClickListener {
            if(isWorking || isPaused) {
                buttonPause.callOnClick()
                if(PresenterTraining.getInstance(this).isDoingRecommendedExercise()){
                    val mySaveFragment = FragmentSaveData()
                    mySaveFragment.show(supportFragmentManager,getString(R.string.notefication))
                }else{
                    val myExerciseFragment = FragmentShowExercise()
                    myExerciseFragment.show(supportFragmentManager, getString(R.string.notefication))
                }
                PresenterTraining.getInstance(this@ViewTraining).onStopTrainingButtonPressed()
            } else {
                // impossible so go out of activity
                onBackPressed()
            }
        }

        backButtonTrainingPage.setOnClickListener {
            //PresenterTraining.getInstance(this@ViewTraining).onBackTrainingButtonPressed()
            if(isWorking || isPaused) {
                buttonPause.callOnClick()
                if(!PresenterTraining.getInstance(this).isDoingRecommendedExercise()){
                    PresenterTraining.getInstance(this).onNegShowExerciseButtonPressed()
                }
                val mySaveFragment = FragmentSaveData()
                mySaveFragment.show(supportFragmentManager,getString(R.string.notefication))
            } else {
                onBackDataButtonPressed()
            }
        }
        //
        gestionBluetoothDevice()
        // exit on create
    }

    private fun gestionBluetoothDevice() {
        this.bluetoothDevice = PresenterTraining.getInstance(this).getDeviceCadence()
        if (bluetoothDevice == null){
            Log.d("Bluetooth","okey no bluetooth device chosen")
        } else {
            Log.d("Bluetoot","try to connect device")

        }
    }

    private fun onBackDataButtonPressed(){
        val intent = Intent(application, ViewMainPage().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private var locationUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val newLocation = intent.getParcelableExtra<Location>("location")
            val latLng = LatLng(newLocation?.latitude!!, newLocation.longitude)
            if(isWorking && !isPaused) {
                val formatTemplate = "%.2f%3s"
                val aux1 = PresenterTraining.getInstance(this@ViewTraining).getSpeedGPS()    // kph
                findViewById<TextView>(R.id.speedratetext).text =
                    formatTemplate.format(aux1, "kph")
                val aux2 =
                    PresenterTraining.getInstance(this@ViewTraining).getSpeedGPSAVG()    // kph
                findViewById<TextView>(R.id.agspeedratetext).text =
                    formatTemplate.format(aux2, "kph")
                PresenterTraining.getInstance(this@ViewTraining).onReceiveLocation(latLng)
            }
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
         *  interact with the service.
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
        /*try {
            unregisterReceiver(locationUpdateReceiver)
            unregisterReceiver(predictedLocationReceiver)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }*/
        super.onDestroy()
    }

    override fun onResume() {
        Log.d("onResume Training view","it works yay")
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
        try {
            unregisterReceiver(locationUpdateReceiver)
            unregisterReceiver(predictedLocationReceiver)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
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
                    Log.d(TAG, "Permission was granted, yay")
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        clearDataTraining()
                        startService()
                    }
                } else {
                    // permission denied, boo!
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

    private fun clearDataTraining() {
        PresenterTraining.getInstance(this).clearDataTraining()
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

    private fun canGetLocation(): Boolean {
        val result : Boolean
        var gpsEnabled = false
        var networkEnabled = false
        try {
            gpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        try {
            networkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)!!
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        result = (gpsEnabled && networkEnabled)
        return result
    }

    private fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(this)
        // Setting Dialog Title
        alertDialog.setTitle("Error!")
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.activatelocation))
        // On pressing Settings button
        alertDialog.setPositiveButton(
            resources.getString(R.string.ok)
        ) { _, _ ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
            startService()
        }
        alertDialog.show()
    }

    private class CustomDataEntry internal constructor(
        var x: String,
        var value: Number,
        var value2: Number,
        var value3: Number
    ) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
            setValue("value3", value3)
        }

        override fun toString(): String {
            var s = ""
            s += "{ x: $x , value : $value , value1 : $value2 , value2 : $value3 }"
            return s
        }
    }

    private fun  addPoint() {
        // append data to  any chart
        seriesData.add(CustomDataEntry(
                "${seriesData.size}",
                PresenterTraining.getInstance(this).getSpeedGPS(),
                PresenterTraining.getInstance(this).getAcceleration(),
                PresenterTraining.getInstance(this).getDistanceGPS()
            )
        )
        dataSet.data(seriesData)
    }
}
