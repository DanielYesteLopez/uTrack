package com.example.utrack.model.services

import kotlin.math.sqrt

class KalmanLatLong(Q_metres_per_second: Float) {
    private val _minAccuracy = 1f

    private var qMetresPerSecond: Float = 0.toFloat()
    private var timeStampMilliseconds: Long = 0
    private var lat: Double = 0.toDouble()
    private var lng: Double = 0.toDouble()
    private var variance: Float = -1f // Negative means object uninitialised.
    var consecutiveRejectCount: Int = 0

    init {
        this.qMetresPerSecond = Q_metres_per_second
    }

    fun getTimestamp(): Long {
        return timeStampMilliseconds
    }

    fun getLat(): Double {
        return lat
    }

    fun getLng(): Double {
        return lng
    }

    fun get_accuracy(): Float {
        return sqrt(variance.toDouble()).toFloat()
    }

    fun SetState(
        _lat: Double, _lng: Double, _accuracy: Float,
        _timeStampMilliseconds: Long
    ) {
        this.lat = _lat
        this.lng = _lng
        variance = _accuracy * _accuracy
        this.timeStampMilliseconds = _timeStampMilliseconds
    }

    /**
     * <summary> Kalman filter processing for latitude and longitude </summary>
     * <param name="lat_measurement_degrees"> new measurement of latitude </param>
     * <param name="lng_measurement"> new measurement of longitude </param>
     * <param name="accuracy"> measurement of 1 standard deviation error in metres </param>
     * <param name="TimeStamp_milliseconds"> time of measurement </param>
     * <returns> new state </returns>
     */
    fun processKalman(
        _lat_measurement: Double, _lng_measurement: Double,
        _accuracy: Float, _timeStampMilliseconds: Long, _qMetresPerSecond: Float
    ) {
        var accuracy = _accuracy
        this.qMetresPerSecond = _qMetresPerSecond
        if (accuracy < _minAccuracy) {
            accuracy = _minAccuracy
        }
        // if variance < 0, object is uninitialised, so initialise with
        if (variance < 0) {
            // current values
            this.timeStampMilliseconds = _timeStampMilliseconds
            lat = _lat_measurement
            lng = _lng_measurement
            variance = accuracy * accuracy
        }
        // apply Kalman filter methodology
        else {
            val timeIncMilliseconds = _timeStampMilliseconds - this.timeStampMilliseconds
            if (timeIncMilliseconds > 0) {
                // time has moved on, so the uncertainty in the current position increases
                variance += (timeIncMilliseconds.toFloat() *
                        _qMetresPerSecond *
                        _qMetresPerSecond) / 1000
                this.timeStampMilliseconds = _timeStampMilliseconds
                // TODO: USE VELOCITY INFORMATION HERE TO GET A BETTER ESTIMATE OF CURRENT POSITION
            }
            // Kalman gain matrix K = Covariance * Inverse(Covariance + MeasurementVariance)
            // NB: because K is dimensionless, it doesn't matter that variance
            // has different units to lat and lng
            val kK = variance / (variance + accuracy * accuracy)
            lat += kK * (_lat_measurement - lat)
            lng += kK * (_lng_measurement - lng)
            // new Covariance matrix is (IdentityMatrix - K) * Covariance
            variance *= (1 - kK)
        }
    }
}