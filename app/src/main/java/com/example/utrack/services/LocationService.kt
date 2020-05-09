package com.example.utrack.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.*
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.utrack.R
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class LocationService: Service(), LocationListener, GpsStatus.Listener {

    private val LOG_TAG: String = LocationService::class.java.simpleName

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
    private val gpsFreqInMillis = 5000
    private val gpsFreqInDistance = 2  // in meters
    var isLogging: Boolean = false

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
        Log.d(LOG_TAG, "onRebind ")
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(LOG_TAG, "onUnbind ")

        return true
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy ")
    }

    /**
     * when the process stop, stop the service.
     */
    override fun onTaskRemoved(rootIntent: Intent) {
        Log.d(LOG_TAG, "onTaskRemoved ")
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
            Log.d(LOG_TAG, "Location -> (" + it.latitude + "," + it.longitude + ")")
            gpsCount++
            if (isLogging) {
                Log.d(LOG_TAG, "going to filter this location")
                // TODO come see me
                val bool = filterAndAddLocation(it)
                Log.d(LOG_TAG, "this location got $bool")
            }
            Log.d(LOG_TAG, "Broadcast for the new location")
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
            if (status == LocationProvider.OUT_OF_SERVICE) {
                notifyLocationProviderStatusUpdated(false)
            } else {
                notifyLocationProviderStatusUpdated(true)
            }
        }
    }

    /**
     *
     */
    override fun onProviderEnabled(provider: String?) {
        if (provider == LocationManager.GPS_PROVIDER) {
            notifyLocationProviderStatusUpdated(true)
        }
    }

    /**
     *
     */
    override fun onProviderDisabled(provider: String?) {
        if (provider == LocationManager.GPS_PROVIDER) {
            notifyLocationProviderStatusUpdated(false)
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
    private fun notifyLocationProviderStatusUpdated(isLocationProviderAvailable: Boolean) {
        //Broadcast location provider status change here
    }

    /**
     *
     */
    fun startLogging() {
        isLogging = true
    }

    /**
     *
     */
    fun stopLogging() {
        if (locationList.size > 1 /*&& batteryLevelArray.size > 1*/) {
            val currentTimeInMillis = SystemClock.elapsedRealtimeNanos() / 1000000
            val elapsedTimeInSeconds = (currentTimeInMillis - runStartTimeInMillis) / 1000
            var totalDistanceInMeters = 0f
            for (i in 0 until locationList.size - 1) {
                totalDistanceInMeters += locationList[i].distanceTo(locationList[i + 1])
            }
            Log.d(LOG_TAG,"saving log $elapsedTimeInSeconds $totalDistanceInMeters")
            saveLog(elapsedTimeInSeconds, totalDistanceInMeters.toDouble(), gpsCount)
        }
        isLogging = false
    }

    /**
     *
     */
    fun startUpdatingLocation() {
        Log.d(LOG_TAG,"Start updating location")
        if (!this.isLocationManagerUpdatingLocation) {
            Log.d(LOG_TAG,"clearing data ---->>>>")
            isLocationManagerUpdatingLocation = true
            runStartTimeInMillis = SystemClock.elapsedRealtimeNanos() / 1000000
            locationList.clear()
            oldLocationList.clear()
            noAccuracyLocationList.clear()
            inaccurateLocationList.clear()
            kalmanNGLocationList.clear()

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
                Log.d(LOG_TAG, "requesting location update")
                locationManager.requestLocationUpdates(
                    gpsFreqInMillis.toLong(),
                    gpsFreqInDistance.toFloat(),
                    criteria,
                    this,
                    null
                )
                gpsCount = 0
            } catch (e: IllegalArgumentException) {
                Log.e(LOG_TAG, e.localizedMessage)
            } catch (e: SecurityException) {
                Log.e(LOG_TAG, e.localizedMessage)
            } catch (e: RuntimeException) {
                Log.e(LOG_TAG, e.localizedMessage)
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
            Log.d(LOG_TAG, "Location is old")
            oldLocationList.add(location)
            return false
        }
        if (location.accuracy <= 0) {
            Log.d(LOG_TAG, "Latitude and longitude values are invalid.")
            noAccuracyLocationList.add(location)
            return false
        }
        //setAccuracy(newLocation.getAccuracy());
        val horizontalAccuracy = location.accuracy
        if (horizontalAccuracy > 1000) { //10meter filter
            Log.d(LOG_TAG, "Accuracy is too low.")
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
            Log.d(LOG_TAG, "Kalman Filter detects mal GPS")
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
        Log.d(LOG_TAG, "Location quality is good enough.")
        currentSpeed = location.speed
        locationList.add(location)
        return true
    }

    /* Data Logging */
    @Synchronized
    fun saveLog(
        timeInSeconds: Long,
        distanceInMeters: Double,
        gpsCount: Int
    ) {
        val fileNameDateTimeFormat = SimpleDateFormat("yyyy_MMdd_HHmm")
        val filePath = (this.getExternalFilesDir(null)!!.absolutePath + "/"
                + fileNameDateTimeFormat.format(Date()) + "_battery" + ".csv")

        Log.d(LOG_TAG, "saving to $filePath")

        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter(filePath, false)
            Log.d(LOG_TAG, "Time: $timeInSeconds ,Distance: $distanceInMeters ,GPSCount: $gpsCount \n")
            fileWriter.append("Time: $timeInSeconds ,Distance: $distanceInMeters ,GPSCount: $gpsCount \n")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close()
                } catch (ioe: IOException) {
                    ioe.printStackTrace()
                }
            }
        }
    }

    /**
     *
     */
    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
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