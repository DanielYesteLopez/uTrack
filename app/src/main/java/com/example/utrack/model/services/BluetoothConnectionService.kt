package com.example.utrack.model.services

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*
import java.util.logging.Handler


class BluetoothConnectionService constructor(context: Context) {
    private val TAG = "BluetoothConnectService"

    private val appName = "MYAPP"

    // Name for the SDP record when creating server socket
    private val NAME_SECURE = "BluetoothChatSecure"
    private val NAME_INSECURE = "BluetoothChatInsecure"

    // Unique UUID for this application
    private val MY_UUID_SECURE: UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")
    private val MY_UUID_INSECURE: UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")

    private var mBluetoothAdapter: BluetoothAdapter? = null
    var mContext: Context? = null

    private var mmDevice: BluetoothDevice? = null
    private var deviceUUID: UUID? = null
    var mProgressDialog: ProgressDialog? = null

    // Member fields
    private val mAdapter: BluetoothAdapter? = null
    private val mHandler: Handler? = null
    private var mSecureAcceptThread: AcceptThread? = null
    private var mInsecureAcceptThread: AcceptThread? = null
    private var mConnectThread: ConnectThread? = null
    private var mConnectedThread: ConnectedThread? = null
    private var mState = 0
    private val mNewState = 0

    // Constants that indicate the current connection state
    val STATE_NONE = 0 // we're doing nothing

    val STATE_LISTEN = 1 // now listening for incoming connections

    val STATE_CONNECTING = 2 // now initiating an outgoing connection

    val STATE_CONNECTED = 3 // now connected to a remote device


    init {
        mContext = context
        mState = STATE_NONE
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        start()
    }

    /**
     * Return the current connection state.
     */
    @Synchronized
    fun getState(): Int {
        return mState
    }


    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private inner class AcceptThread constructor(secure : Boolean) : Thread() {

        // The local server socket
        private var mmServerSocket : BluetoothServerSocket? = null
        private var mSocketType: String? = null

        init {
            mSocketType = if (secure) "Secure" else "Insecure"
            var tmp: BluetoothServerSocket? = null


            // Create a new listening server socket
            try {
                if (secure) {
                    tmp = mAdapter?.listenUsingRfcommWithServiceRecord(
                        NAME_SECURE,
                        MY_UUID_SECURE
                    )
                    Log.d(TAG, "AcceptThread: Setting up Server using: $MY_UUID_SECURE")
                } else {
                    tmp = mAdapter?.listenUsingInsecureRfcommWithServiceRecord(
                        NAME_INSECURE,
                        MY_UUID_INSECURE
                    )
                    Log.d(TAG, "AcceptThread: Setting up Server using: $MY_UUID_INSECURE")
                }
            } catch (e: IOException) {
                Log.e(TAG, "AcceptThread: IOException: " + e.message)
            }
            mmServerSocket = tmp
            mState = STATE_LISTEN
        }

        override fun run() {
            Log.d(TAG, "run: AcceptThread Running. $mSocketType")
            name = "AcceptThread$mSocketType"

            var socket: BluetoothSocket? = null

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Log.d(TAG, "run: RFCOM server socket start.....")
                socket = mmServerSocket!!.accept()
                Log.d(TAG, "run: RFCOM server socket accepted connection.")
            } catch (e: IOException) {
                Log.e(TAG, "AcceptThread: IOException: " + e.message)
            }

            // If a connection was accepted

            // If a connection was accepted
            if (socket != null) {
                synchronized(this@BluetoothConnectionService) {
                    when (mState) {
                        STATE_LISTEN, STATE_CONNECTING -> {                                // Situation normal. Start the connected thread.
                            mmDevice?.let { connected(socket, it, mSocketType!!) }
                        }
                        STATE_NONE, STATE_CONNECTED -> {                                 // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close()
                            } catch (e: IOException) {
                                Log.e(TAG, "Could not close unwanted socket", e)
                            }
                        }
                        else -> {
                            // nothing
                        }
                    }
                }
            }
            Log.i(TAG, "END mAcceptThread ")
        }

        fun cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread.")
            try {
                mmServerSocket!!.close()
            } catch (e: IOException) {
                Log.e(
                    TAG,
                    "cancel: Close of AcceptThread ServerSocket failed. " + e.message
                )
            }
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private inner class ConnectThread(device: BluetoothDevice, secure : Boolean) :
        Thread() {
        private var mmSocket: BluetoothSocket? = null
        private var mSocketType: String? = null

        init {
            Log.d(TAG, "ConnectThread: started.")
            mmDevice = device
            mSocketType = if (secure) "Secure" else "Insecure"
            var tmp: BluetoothSocket? = null

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(
                        MY_UUID_SECURE
                    )
                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(
                        MY_UUID_INSECURE
                    )
                }
            } catch (e: IOException) {
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e)
            }
            mmSocket = tmp
            mState = STATE_CONNECTING
        }

        override fun run() {
            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            var tmp: BluetoothSocket? = null
            Log.i(TAG, "RUN mConnectThread ")
            name = "ConnectThread$mSocketType"

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter?.cancelDiscovery()

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket!!.connect()
            } catch (e: IOException) {
                // Close the socket
                try {
                    mmSocket!!.close()
                } catch (e2: IOException) {
                    Log.e(
                        TAG, "unable to close() " + mSocketType +
                                " socket during connection failure", e2
                    )
                }
                connectionFailed()
                return
            }

            // Reset the ConnectThread because we're done
            synchronized(this@BluetoothConnectionService) { mConnectThread = null }

            // Start the connected thread
            connected(mmSocket!!, mmDevice!!, mSocketType!!)
        }

        fun cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.")
                mmSocket!!.close()
            } catch (e: IOException) {
                Log.e(
                    TAG,
                    "cancel: close() of mmSocket in Connectthread failed. " + e.message
                )
            }
        }
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    @Synchronized
    fun start() {
        Log.d(TAG, "start")

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread!!.cancel()
            mConnectThread = null
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }

//        // Start the thread to listen on a BluetoothServerSocket
//        if (mSecureAcceptThread == null) {
//            mSecureAcceptThread = AcceptThread(true)
//            mSecureAcceptThread!!.start()
//        }

        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = AcceptThread(false)
            mInsecureAcceptThread!!.start()
        }
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    @Synchronized
    fun connect(device: BluetoothDevice, secure: Boolean) {
        Log.d(TAG, "connect to: $device")

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread!!.cancel()
                mConnectThread = null
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }

        // Start the thread to connect with the given device
        mConnectThread = ConnectThread(device, secure)
        mConnectThread!!.start()
    }


    /**
     * AcceptThread starts and sits waiting for a connection.
     * Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
     */
    fun startClient(device: BluetoothDevice?, secure: Boolean) {
        Log.d(TAG, "startClient: Started.")

        //initprogress dialog
        mProgressDialog = ProgressDialog.show(
            mContext, "Connecting Bluetooth"
            , "Please Wait...", true
        )
        mConnectThread = ConnectThread(device!!, secure)
        mConnectThread!!.start()
    }


    /**
     * Finally the ConnectedThread which is responsible for maintaining the BTConnection, Sending the data, and
     * receiving incoming data through input/output streams respectively.
     */
    private inner class ConnectedThread(socket: BluetoothSocket) : Thread() {
        private val mmSocket: BluetoothSocket
        private val mmInStream: InputStream?
        private val mmOutStream: OutputStream?

        init {
            Log.d(TAG, "ConnectedThread: Starting.")
            mmSocket = socket
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null

            //dismiss the progressdialog when connection is established
            try {
                mProgressDialog?.dismiss()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            try {
                tmpIn = mmSocket.inputStream
                tmpOut = mmSocket.outputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mmInStream = tmpIn
            mmOutStream = tmpOut
            mState = STATE_CONNECTED
        }

        override fun run() {
            Log.i(TAG, "BEGIN mConnectedThread")
            val buffer = ByteArray(1024) // buffer store for the stream
            var bytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (mState == STATE_CONNECTED) {
                // Read from the InputStream
                try {
                    bytes = mmInStream?.read(buffer)!!
                    val incomingMessage = String(buffer, 0, bytes)
                    Log.d(TAG, "InputStream: $incomingMessage")
                } catch (e: IOException) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.message)
                    break
                }
            }
        }

        //Call this from the main activity to send data to the remote device
        fun write(bytes: ByteArray?) {
            val text = String(bytes!!, Charset.defaultCharset())
            Log.d(TAG, "write: Writing to outputstream: $text")
            try {
                mmOutStream?.write(bytes)
            } catch (e: IOException) {
                Log.e(TAG, "write: Error writing to output stream. " + e.message)
            }
        }

        /* Call this from the main activity to shutdown the connection */
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
            }
        }


    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    @Synchronized
    fun connected(
        socket: BluetoothSocket,
        device: BluetoothDevice,
        socketType: String
    ) {
        Log.d(TAG, "connected, Socket Type:$socketType")

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread!!.cancel()
            mConnectThread = null
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }

        // Cancel the accept thread because we only want to connect to one device
        if (mSecureAcceptThread != null) {
            mSecureAcceptThread!!.cancel()
            mSecureAcceptThread = null
        }
        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread!!.cancel()
            mInsecureAcceptThread = null
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = ConnectedThread(socket)
        mConnectedThread!!.start()

        Log.d(TAG, "connected, device name:${device.name}")
    }

    /**
     * Stop all threads
     */
    @Synchronized
    fun stop() {
        Log.d(TAG, "stop")
        if (mConnectThread != null) {
            mConnectThread!!.cancel()
            mConnectThread = null
        }
        if (mConnectedThread != null) {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }
        if (mSecureAcceptThread != null) {
            mSecureAcceptThread!!.cancel()
            mSecureAcceptThread = null
        }
        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread!!.cancel()
            mInsecureAcceptThread = null
        }
        mState = STATE_NONE
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread.write
     */
    fun write(out: ByteArray?) {
        // Create temporary object
        var r: ConnectedThread
        // Synchronize a copy of the ConnectedThread
        synchronized(this) {
            if (mState != STATE_CONNECTED) return
            r = mConnectedThread!!
        }
        // Perform the write unsynchronized
        r.write(out)
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private fun connectionFailed() {
        Log.d(TAG,"Unable to connect device")
        mState = STATE_NONE
        // Start the service over to restart listening mode
        this@BluetoothConnectionService.start()
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private fun connectionLost() {
        Log.d(TAG,"Device connection was lost")
        mState = STATE_NONE
        // Start the service over to restart listening mode
        this@BluetoothConnectionService.start()
    }

}
