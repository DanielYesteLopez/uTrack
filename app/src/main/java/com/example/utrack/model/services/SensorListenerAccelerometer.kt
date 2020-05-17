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

    private var mSensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var mLinerAcceleration: Sensor? = null
/*    private var mGyroScope: Sensor? = null*/
    private var resume = false
    private var counterDataReadied: Int = 0
    private var acceleratesAct: Float = 0.0F
    private var acceleratesList: ArrayList<Float>? = null
/*    private var velocityAct: Float = 0.0F*/
/*    private var positionAct: Float = 0.0F*/
/*    private var velocityList: ArrayList<Float>? = null
    private var positionList: ArrayList<Float>? = null*/
    private var epsilon: Float = 0.1F

    init {
        checkSensor()
        mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)?.let {
            mLinerAcceleration = it
        }
/*        mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let {
            mGyroScope = it
        }*/
        this.setAccelerationAct(0.0F)
/*        this.setPositionAct(0.0F)
        this.setVelocityAct(0.0F)*/
        counterDataReadied = 0
        acceleratesList = ArrayList()
        acceleratesList?.add(acceleratesAct)
  /*      velocityList = ArrayList()
        positionList = ArrayList()

        velocityList?.add(velocityAct)
        positionList?.add(positionAct)*/
        epsilon = 0.01F
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

    private fun computeSumXYZ(a: Float, b: Float, c: Float): Float {
        val a2 = a.pow(2)
        val b2 = b.pow(2)
        val c2 = c.pow(2)
        var res = (a2 + b2 + c2)
        res = sqrt(res)
        return res
    }
/*
    fun doubleIntegration(
        _acc_act: Float,
        _acc_ant: Float,
        _velocity: Float,
        _position: Float,
        _delta_t: Int
    ): ArrayList<Float> {
        val count = counterDataReadied
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
    }*/

    /**
     * h = 1 estamos usando dos puntos a y b sin tomar puntos en medio
     *
     */
   /* fun integrationTrapeze(_acc_ant: Float, _acc_act: Float, _delta_t: Int): Float {
        val fa = _acc_ant
        val fb = _acc_act
        var value = (fa + fb) / 2
        value *= _delta_t
        return value
    }*/

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
                    computeSumXYZ(event.values[0], event.values[1], event.values[2])
/*                val accelerateAnterior = this.getAccelerateActual()
                var velocityActual = this.getVelocityActual()
                var positionActual = this.getPositionActual()*/
                if (accelerateActual < epsilon) {
                    accelerateActual = 0.0F
                }
                counterDataReadied += 1
                // delta_t change to real value
/*                val valuesT =
                    doubleIntegration(
                        accelerateActual,
                        accelerateAnterior,
                        velocityActual,
                        positionActual,
                        1000
                    )*/
/*                velocityActual = valuesT[0]
                positionActual = valuesT[1]*/
                acceleratesList?.add(accelerateActual)
/*                velocityList?.add(velocityActual)
                positionList?.add(positionActual)*/
                this.setAccelerationAct(accelerateActual)
/*                this.setVelocityAct(velocityActual / 10)
                this.setPositionAct(positionActual)*/
            }
        }
    }

/*    fun getPositionActual(): Float {
        return this.positionAct
    }

    fun getVelocityActual(): Float {
        return this.velocityAct
    }*/

 /*   private fun getAccelerateActual(): Float {
        return this.acceleratesAct
    }*/

/*    private fun setPositionAct(positionact: Float) {
        this.positionAct = positionact
    }

    private fun setVelocityAct(velocity: Float) {
        this.velocityAct = velocity
    }*/

    private fun setAccelerationAct(acceleration: Float) {
        this.acceleratesAct = acceleration
    }


    fun getAccelerationAVG() : Double {
        var totalAcceleration = 0f
        for (i in 0 until acceleratesList?.size!! - 1) {
            totalAcceleration += acceleratesList!![i]
        }
        return (totalAcceleration/ acceleratesList?.size!!).toDouble()
    }



    fun getAccelerationInfo() : ArrayList<Double> {
        var totalAcceleration = 0f
        var accelerationMin = 0f
        var accelerationMax = 0f

        for (i in 0 until acceleratesList?.size!! - 1) {
            totalAcceleration += acceleratesList!![i]
            if (i == 0) {
                accelerationMin = acceleratesList!![i]
                accelerationMax = acceleratesList!![i]
            } else {
                if (accelerationMin > acceleratesList!![i]) {
                    accelerationMin = acceleratesList!![i]
                }
                if (accelerationMax < acceleratesList!![i]) {
                    accelerationMax = acceleratesList!![i]
                }
            }
        }
        val accelerationAVG = (totalAcceleration/ acceleratesList?.size!!)
        val ret : ArrayList<Double> = ArrayList()
        ret.add(accelerationMin.toDouble())
        ret.add(accelerationAVG.toDouble())
        ret.add(accelerationMax.toDouble())
        return ret
    }

    fun registerListener() {
        mSensorManager.registerListener(this, mLinerAcceleration, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener() {
        mSensorManager.unregisterListener(this)
    }
}

