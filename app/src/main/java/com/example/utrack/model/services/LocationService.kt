package com.example.utrack.model.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.*
import android.location.LocationProvider.OUT_OF_SERVICE
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.utrack.R
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class LocationService: Service(), LocationListener, GpsStatus.Listener {

    private val _lTAG: String = LocationService::class.java.simpleName
    private val binder = LocationServiceBinder()
    private var isLocationManagerUpdatingLocation: Boolean = false

    private var locationList: ArrayList<Location>
    private var oldLocationList: ArrayList<Location>
    private var noAccuracyLocationList: ArrayList<Location>
    private var inaccurateLocationList: ArrayList<Location>
    private var kalmanNGLocationList: ArrayList<Location>

    private var currentSpeed = 0.0f // meters/second
    private var kalmanFilter: KalmanLatLong
    private var runStartTimeInMillis: Long = 0

    private var gpsCount: Int = 0
    private val gpsFreqInMillis = 1000
    private val gpsFreqInDistance = 1  // in meters
    var isLogging: Boolean = false

    private var currentTimeInMillis : Long = 0L
    private var elapsedTimeInSeconds : Long = 0L
    private var totalDistanceInMeters : Float = 0f
    private var totalSpeedInkph = 0f
    private var maxSpeedInkph = 0f
    private var minSpeedInkph = 0f

    init {
        isLocationManagerUpdatingLocation = false
        locationList = ArrayList()
        noAccuracyLocationList = ArrayList()
        oldLocationList = ArrayList()
        inaccurateLocationList = ArrayList()
        kalmanNGLocationList = ArrayList()
        kalmanFilter = KalmanLatLong(3f)
        isLogging = false
    }


    override fun onStartCommand(i: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(i, flags, startId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground()
        }
        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onRebind(intent: Intent) {
        Log.d(_lTAG, "onRebind ")
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(_lTAG, "onUnbind ")

        return true
    }

    override fun onDestroy() {
        Log.d(_lTAG, "onDestroy ")
    }

    /**
     * when the process stop, stop the service.
     */
    override fun onTaskRemoved(rootIntent: Intent) {
        //Log.d(LOG_TAG, "onTaskRemoved ")
        this.stopUpdatingLocation()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true)
        } else {
            stopSelf()
        }
    }

    inner class LocationServiceBinder : Binder() {
        val service: LocationService
            get() = this@LocationService
    }

    /**
     *
     */
    override fun onLocationChanged(newLocation: Location?) {
        newLocation?.let{
            gpsCount++
            if (isLogging) {
                Log.d(_lTAG, "going to filter this location")

                if (filterAndAddLocation(it)){
                    Log.d(_lTAG, "Location -> (" + it.latitude + "," + it.longitude + ")")
                }
            }
            Log.d(_lTAG, "Broadcast for the new location")
            val intent = Intent("LocationUpdated")
            intent.putExtra("location", it)
            LocalBroadcastManager.getInstance(this.application).sendBroadcast(intent)
        }
    }

    /**
     *
     */
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        if (provider == LocationManager.GPS_PROVIDER) {
            if (status == OUT_OF_SERVICE) {
                notifyLocationProviderStatusUpdated()
            } else {
                notifyLocationProviderStatusUpdated()
            }
        }
    }

    /**
     *
     */
    override fun onProviderEnabled(provider: String?) {
        if (provider == LocationManager.GPS_PROVIDER) {
            notifyLocationProviderStatusUpdated()
        }
    }

    /**
     *
     */
    override fun onProviderDisabled(provider: String?) {
        if (provider == LocationManager.GPS_PROVIDER) {
            notifyLocationProviderStatusUpdated()
        }

    }

    /**
     *
     */
    override fun onGpsStatusChanged(event: Int) {

    }

    /**
     *
     */
    private fun notifyLocationProviderStatusUpdated() {
        //Broadcast location provider status change here
    }

    /**
     *
     */
    fun startLogging() {
        isLogging = true
    }

    fun pouseLogging() {
        isLogging = false
    }

    /**
     *
     */
    fun stopLogging() {
        if (locationList.size > 1 /*&& batteryLevelArray.size > 1*/) {
            currentTimeInMillis = SystemClock.elapsedRealtimeNanos() / 1000000
            elapsedTimeInSeconds = (currentTimeInMillis - runStartTimeInMillis) / 1000
            totalDistanceInMeters = 0f
            totalSpeedInkph = 0f
            var speed: Float
            for (i in 0 until locationList.size - 1) {
                totalDistanceInMeters += locationList[i].distanceTo(locationList[i + 1])
                speed = getLocationSpeed()
                totalSpeedInkph += speed
                if (i == 0) {
                    minSpeedInkph = speed
                    maxSpeedInkph = speed
                } else {
                    if (minSpeedInkph > speed) {
                        minSpeedInkph = speed
                    }
                    if (maxSpeedInkph < speed) {
                        maxSpeedInkph = speed
                    }
                }
            }
            totalDistanceInMeters /= 1000 // to km
            Log.d(_lTAG,"saving log $elapsedTimeInSeconds $totalDistanceInMeters")
        }
        isLogging = false
    }
    fun getLocationSpeedAVG() : Float {
        val speedAVG : Float
        var totalSpeed = 0.0f
        var speed : Float
        if(locationList.size > 0) {
            for (i in 0 until locationList.size - 1) {
                speed = getLocationSpeed()
                totalSpeed += speed
            }
            speedAVG = totalSpeed/locationList.size
        }else {
            speedAVG = 0.0f
        }
        return speedAVG
    }

    fun getTrainingLocationInfo() : ArrayList<ArrayList<Double>> {
        val ret : ArrayList<ArrayList<Double>> = ArrayList()
        val time : ArrayList<Double> = ArrayList()
        val distance : ArrayList<Double> = ArrayList()
        val speed : ArrayList<Double> = ArrayList()
        time.add(elapsedTimeInSeconds.toDouble())
        distance.add(totalDistanceInMeters.toDouble())
        val avgSpeed = (totalSpeedInkph/locationList.size).toDouble()
        speed.add(minSpeedInkph.toDouble())
        speed.add(avgSpeed)
        speed.add(maxSpeedInkph.toDouble())
        ret.add(time)
        ret.add(distance)
        ret.add(speed)
        return ret
    }

    fun getTimeInSeconds() : Double {
        elapsedTimeInSeconds = (currentTimeInMillis - runStartTimeInMillis) / 1000
        return this.elapsedTimeInSeconds.toDouble()
    }

    fun clearData(){
        runStartTimeInMillis = SystemClock.elapsedRealtimeNanos() / 1000000
        locationList.clear()
        oldLocationList.clear()
        noAccuracyLocationList.clear()
        inaccurateLocationList.clear()
        kalmanNGLocationList.clear()
    }

    /**
     *
     */
    fun startUpdatingLocation() {
        Log.d(_lTAG,"Start updating location")
        if (!this.isLocationManagerUpdatingLocation) {
            Log.d(_lTAG,"clearing data ---->>>>")
            isLocationManagerUpdatingLocation = true
            clearData()
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            /**
             * Exception thrown when GPS or Network provider were not available on the user's device.
             */
            try {
                val criteria = Criteria()
                criteria.accuracy = Criteria.ACCURACY_FINE
                criteria.powerRequirement = Criteria.POWER_HIGH
                criteria.isAltitudeRequired = false
                criteria.isSpeedRequired = true
                criteria.isCostAllowed = true
                criteria.isBearingRequired = false
                criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
                criteria.verticalAccuracy = Criteria.ACCURACY_HIGH

                locationManager.addGpsStatusListener(this)
                Log.d(_lTAG, "requesting location update")
                locationManager.requestLocationUpdates(
                    gpsFreqInMillis.toLong(),
                    gpsFreqInDistance.toFloat(),
                    criteria,
                    this,
                    null
                )
                gpsCount = 0
            } catch (e: IllegalArgumentException) {
                Log.e(_lTAG, e.localizedMessage!!)
            } catch (e: SecurityException) {
                Log.e(_lTAG, e.localizedMessage!!)
            } catch (e: RuntimeException) {
                Log.e(_lTAG, e.localizedMessage!!)
            }
        }
    }

    /**
     *
     */
    fun stopUpdatingLocation() {
        if (this.isLocationManagerUpdatingLocation) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.removeUpdates(this)
            isLocationManagerUpdatingLocation = false
        }
    }

    /**
     *
     */
    private fun getLocationAge(newLocation: Location): Long {
        val locationAge: Long
        val currentTimeInMilli = SystemClock.elapsedRealtimeNanos() / 1000000
        val locationTimeInMilli = newLocation.elapsedRealtimeNanos / 1000000
        locationAge = currentTimeInMilli - locationTimeInMilli
        return locationAge
    }

    /**
     *
     */
    private fun filterAndAddLocation(location: Location): Boolean {
        val age = getLocationAge(location)
        if (age > 5 * 1000) { //more than 5 seconds
            Log.d(_lTAG, "Location is old")
            oldLocationList.add(location)
            return false
        }
        if (location.accuracy <= 0) {
            Log.d(_lTAG, "Latitude and longitude values are invalid.")
            noAccuracyLocationList.add(location)
            return false
        }
        val horizontalAccuracy = location.accuracy
        if (horizontalAccuracy > 1000) { //10meter filter
            Log.d(_lTAG, "Accuracy is too low.")
            inaccurateLocationList.add(location)
            return false
        }
        /* Kalman Filter */
        val locationTimeInMillis = location.elapsedRealtimeNanos / 1000000
        val elapsedTimeInMillis = locationTimeInMillis - runStartTimeInMillis
        val qValue: Float = if (currentSpeed == 0.0f) {
            3.0f //3 meters per second
        } else {
            currentSpeed // meters per second
        }
        kalmanFilter.processKalman(location.latitude, location.longitude, location.accuracy, elapsedTimeInMillis, qValue)

        val predictedLat = kalmanFilter.getLat()
        val predictedLng = kalmanFilter.getLng()

        val predictedLocation = Location("")//provider name is unnecessary

        predictedLocation.latitude = predictedLat
        predictedLocation.longitude = predictedLng

        val predictedDeltaInMeters = predictedLocation.distanceTo(location)

        if (predictedDeltaInMeters > 60) {
            Log.d(_lTAG, "Kalman Filter detects mal GPS")
            kalmanFilter.consecutiveRejectCount += 1
            if (kalmanFilter.consecutiveRejectCount > 3) {
                kalmanFilter = KalmanLatLong(3f) //reset Kalman Filter if it rejects more than 3 times in raw.
            }
            kalmanNGLocationList.add(location)
            return false
        } else {
            kalmanFilter.consecutiveRejectCount = 0
        }
        /* Notify predicted location to UI */
        val intent = Intent("PredictLocation")
        intent.putExtra("location", predictedLocation)
        LocalBroadcastManager.getInstance(this.application).sendBroadcast(intent)
        Log.d(_lTAG, "Location quality is good enough.")
        currentSpeed = location.speed
        locationList.add(location)
        return true
    }

    /**
     *
     */
    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            } else {
                ""
            }
        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .build()
        startForeground(101, notification)
    }

    private fun getLastLocation() : Location {
        if(locationList.isEmpty()){
            throw java.lang.Exception("Location is empty")
        }else if (locationList.size == 1){
            throw java.lang.Exception("Location is one")
        }
        return locationList[locationList.lastIndex-1]
    }

    private fun getActualLocation() : Location {
        if(locationList.isEmpty()){
            throw java.lang.Exception("Location is empty")
        }
        return locationList.last()
    }

    fun getLocationSpeed() : Float {
        return (getActualLocation().speed/1000)*3600
    }

    fun getDistanceLocations() : Float {
        return getLastLocation().distanceTo(getActualLocation())
    }

    /**
     *
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(_channelId: String = "uTrack Service", _channelName: String = "Location Tracking Service"): String{
        val chan = NotificationChannel(_channelId, _channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return _channelId
    }
}