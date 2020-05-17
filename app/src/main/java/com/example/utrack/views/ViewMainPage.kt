package com.example.utrack.views


import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.example.utrack.R
import com.example.utrack.mc.MainViewClass
import com.example.utrack.presenters.PresenterMainPage
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Value
import com.google.android.gms.fitness.request.DataSourcesRequest
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import com.google.android.gms.fitness.result.DataSourcesResult
import java.util.concurrent.TimeUnit


class ViewMainPage : MainViewClass(), OnDataPointListener,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private val REQUEST_OAUTH = 1
    private val AUTH_PENDING = "auth_state_pending"
    private var authInProgress = false
    private lateinit var mApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        // hide navigation bar
        onCreateHideNavBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainpage)
        PresenterMainPage.getInstance(this)
        buttonsMainActivityManagement()
        doubleBackToExitPressedOnce = false
        if ( savedInstanceState != null ) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING)
        }
        mApiClient =
            GoogleApiClient.Builder(this)
            .addApi(Fitness.BLE_API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

    }

    fun buttonsMainActivityManagement(){
        val settingsButton = findViewById<ImageButton>(R.id.settingsButtonMainpage)
        val dataButton = findViewById<ImageButton>(R.id.dataButtonMainpage)
        val trainingButton = findViewById<ImageButton>(R.id.trainingButtonMainpage)
        val exitButton = findViewById<ImageButton>(R.id.exitButtonMainpage)
        settingsButton.setOnClickListener {
            PresenterMainPage.getInstance(this).onSettingsButtonPressed(this.applicationContext)
        }
        dataButton.setOnClickListener {
            PresenterMainPage.getInstance(this).onDataButtonPressed(this.applicationContext)
        }
        trainingButton.setOnClickListener {
            PresenterMainPage.getInstance(this).onTrainingButtonPressed(this.applicationContext)
        }
        exitButton.setOnClickListener{
            PresenterMainPage.getInstance(this).onExitButtonPressed(this)
        }
    }

    override fun onConnected(bundle: Bundle?) {
        val dataSourceRequest =
            DataSourcesRequest.Builder()
            .setDataTypes(DataType.TYPE_CYCLING_PEDALING_CADENCE)
            .setDataSourceTypes(DataSource.TYPE_RAW)
            .build()

        val dataSourcesResultCallback: ResultCallback<DataSourcesResult> =
            ResultCallback<DataSourcesResult> { dataSourcesResult ->
                for (dataSource in dataSourcesResult.dataSources) {
                    if (DataType.TYPE_CYCLING_PEDALING_CADENCE == dataSource.dataType) {
                        registerFitnessDataListener(dataSource, DataType.TYPE_CYCLING_PEDALING_CADENCE)
                    }
                }
            }
        Fitness.SensorsApi
            .findDataSources(mApiClient, dataSourceRequest)
            .setResultCallback(dataSourcesResultCallback)
    }

    private fun registerFitnessDataListener(
        dataSource: DataSource,
        dataType: DataType
    ) {
        val request = SensorRequest.Builder()
            .setDataSource(dataSource)
            .setDataType(dataType)
            .setSamplingRate(3, TimeUnit.SECONDS)
            .build()
        Fitness.SensorsApi.add(mApiClient, request, this)
            .setResultCallback { status ->
                if (status.isSuccess) {
                    Log.e("GoogleFit", "SensorApi successfully added")
                }
            }
    }

    override fun onConnectionSuspended(i: Int) {}

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if (!authInProgress) {
            try {
                authInProgress = true
                connectionResult.startResolutionForResult(this@ViewMainPage, REQUEST_OAUTH)
            } catch (e: SendIntentException) {
                Log.d("Google Fit","${e.printStackTrace()}")
            }
        } else {
            Log.d("GoogleFit", "authInProgress")
        }
    }

    override fun onDataPoint(dataPoint: DataPoint) {
        for (field in dataPoint.dataType.fields) {
            val value: Value = dataPoint.getValue(field)
            runOnUiThread {
                Toast.makeText(
                    applicationContext,
                    "Field: " + field.name.toString() + " Value: " + value,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStart(){
        super.onStart()
        mApiClient.connect()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_OAUTH) {
            authInProgress = false
            if (resultCode == Activity.RESULT_OK) {
                if (!mApiClient.isConnecting && !mApiClient.isConnected) {
                    mApiClient.connect()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("GoogleFit", "RESULT_CANCELED")
            }
        } else {
            Log.e("GoogleFit", "requestCode NOT request_oauth")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        super.onStop()
        Fitness.SensorsApi.remove(mApiClient, this)
            .setResultCallback { status ->
                if (status.isSuccess) {
                    mApiClient.disconnect()
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(AUTH_PENDING, authInProgress)
    }
}
