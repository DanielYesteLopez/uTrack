package com.example.utrack.model.services

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.pow
import kotlin.math.sqrt

class SensorListenerAccelerometer(context: Context) : SensorEventListener {

    private var mSensorManager: SensorManager
    private var mLinerAcceleration: Sensor? = null
    private var mGyroScope: Sensor? = null
    private var resume = false
    private var counterdatareaded: Int = 0
    private var accelerityAct: Float = 0.0F
    private var accelerityList: ArrayList<Float>? = null
    private var velocityActt: Float = 0.0F
    private var positionActt: Float = 0.0F
    private var velocityListt: ArrayList<Float>? = null
    private var positionListt: ArrayList<Float>? = null
    private var empsilon: Float = 0.1F

    init {
        mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        checkSensor()
        mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)?.let {
            mLinerAcceleration = it
        }
        mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let {
            mGyroScope = it
        }
        this.setAccelerateAct(0.0F)
        this.setPositionAct(0.0F)
        this.setVelocityAct(0.0F)
        counterdatareaded = 0
        accelerityList = ArrayList()
        velocityListt = ArrayList()
        positionListt = ArrayList()
        accelerityList?.add(accelerityAct)
        velocityListt?.add(velocityActt)
        positionListt?.add(positionActt)
        empsilon = 0.01F
    }

    fun resumeReading() {
        this.resume = true
    }

    fun pauseReading() {
        this.resume = false
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
    }

    private fun computeSumxyz(a: Float, b: Float, c: Float): Float {
        val a_2 = a.pow(2)
        val b_2 = b.pow(2)
        val c_2 = c.pow(2)
        var res = (a_2 + b_2 + c_2)
        res = sqrt(res)
        return res
    }

    fun doubleIntegration(
        _acc_act: Float,
        _acc_ant: Float,
        _velocity: Float,
        _position: Float,
        _delta_t: Int
    ): ArrayList<Float> {
        val count = counterdatareaded
        val acc_act = _acc_act
        val acc_ant = _acc_ant

        var velocity = _velocity
        var position = _position
        if (count == 1) {
            velocity = _velocity
            position = _position
        }
        if (count >= 2) {
            if (acc_act < acc_ant) {
                velocity = (integrationTrapeze(acc_ant, acc_act, _delta_t))
            } else {
                velocity = (integrationTrapeze(acc_ant, acc_act, _delta_t))
            }
            position = (integrationTrapeze(_velocity, velocity, _delta_t))
        }
        val res: ArrayList<Float> = ArrayList()
        res.add(velocity)
        res.add(position)
        return res
    }

    /**
     * h = 1 estamos usando dos puntos a y b sin tomar puntos en medio
     *
     */
    fun integrationTrapeze(_acc_ant: Float, _acc_act: Float, _delta_t: Int): Float {
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
                var accelerateActual =
                    computeSumxyz(event.values[0], event.values[1], event.values[2])
                val accelerateAnterior = this.getAccelerateActual()
                var velocityActual = this.getVelocityActual()
                var positionActual = this.getPositionActual()
                if (accelerateActual < empsilon) {
                    accelerateActual = 0.0F
                }

                counterdatareaded += 1
                // delta_t change to real value
                val valuesT =
                    doubleIntegration(
                        accelerateActual,
                        accelerateAnterior,
                        velocityActual,
                        positionActual,
                        1000
                    )
                velocityActual = valuesT[0]
                positionActual = valuesT[1]
                accelerityList?.add(accelerateActual)
                velocityListt?.add(velocityActual)
                positionListt?.add(positionActual)
                this.setAccelerateAct(accelerateActual)
                this.setVelocityAct(velocityActual / 10)
                this.setPositionAct(positionActual)
            }
        }
    }

    fun getPositionActual(): Float {
        return this.positionActt
    }

    fun getVelocityActual(): Float {
        return this.velocityActt
    }

    fun getAccelerateActual(): Float {
        return this.accelerityAct
    }

    private fun setPositionAct(positionact: Float) {
        this.positionActt = positionact
    }

    private fun setVelocityAct(velocity: Float) {
        this.velocityActt = velocity
    }

    private fun setAccelerateAct(accelerity: Float) {
        this.accelerityAct = accelerity
    }

    fun getAccelerationAVG() : Double {
        var totalAcceleration = 0f
        for (i in 0 until accelerityList?.size!! - 1) {
            totalAcceleration += accelerityList!![i]
        }
        return (totalAcceleration/ accelerityList?.size!!).toDouble()
    }


    fun getAcceleracionInfo() : ArrayList<Double> {
        var totalAcceleration = 0f
        var acceleracionMin = 0f
        var acceleracionMax = 0f

        for (i in 0 until accelerityList?.size!! - 1) {
            totalAcceleration += accelerityList!![i]
            if (i == 0) {
                acceleracionMin = accelerityList!![i]
                acceleracionMax = accelerityList!![i]
            } else {
                if (acceleracionMin > accelerityList!![i]) {
                    acceleracionMin = accelerityList!![i]
                }
                if (acceleracionMax < accelerityList!![i]) {
                    acceleracionMax = accelerityList!![i]
                }
            }
        }
        val accelerationAVG = (totalAcceleration/ accelerityList?.size!!)
        val ret : ArrayList<Double> = ArrayList()
        ret.add(acceleracionMin.toDouble())
        ret.add(accelerationAVG.toDouble())
        ret.add(acceleracionMax.toDouble())
        return ret
    }

    fun registerListener() {
        mSensorManager.registerListener(this, mLinerAcceleration, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener() {
        mSensorManager.unregisterListener(this)
    }
}

