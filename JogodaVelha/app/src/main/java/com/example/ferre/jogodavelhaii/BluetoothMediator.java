package com.example.ferre.jogodavelhaii;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothMediator {

    private ConnectedThread connectedThread;
    private Activity activity;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean enabled;

    public boolean deviceHasBluetooth() {
        return (BluetoothAdapter.getDefaultAdapter() != null);
    }

    public ArrayList<BluetoothDevice> getPairedDevices() {
        return new ArrayList<BluetoothDevice>(BluetoothAdapter.getDefaultAdapter().getBondedDevices());
    }

    public void connectTo(BluetoothDevice device, Activity activity) {
        ConnectThread connectThread = new ConnectThread(device);
        this.activity = activity;
        connectThread.start();
    }

    public void waitConnection(Activity activity) {
        //this.run();
        AcceptThread acceptThread = new AcceptThread();
        this.activity = activity;
        acceptThread.start();
    }

    public ConnectedThread getConnection() {
        return connectedThread;
    }

    public boolean isEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private BluetoothAdapter mBluetoothAdapter;

        public ConnectThread(BluetoothDevice device) {

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            try {
                mBluetoothAdapter.cancelDiscovery();

                mmSocket.connect();

                manageConnectedSocket(mmSocket);

            } catch (Exception e) {
                try {
                    mmSocket.close();
                } catch (IOException e1) {
                    Toast.makeText(activity, "Error: " + e1.getMessage(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        private void manageConnectedSocket(BluetoothSocket mmSocket) {
            connectedThread = new ConnectedThread(mmSocket);
            ((ListingActivity) activity).callBack();
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private final BluetoothAdapter mBluetoothAdapter;

        public AcceptThread() {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("JOGO_DA_VELHA", MY_UUID);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {

            try {
                BluetoothSocket socket = null;
                // Keep listening until exception occurs or a socket is returned
                while (true) {
                    socket = mmServerSocket.accept();

                    if (socket != null) {
                        manageConnectedSocket(socket);
                        break;
                    }
                }

            } catch (Exception e) {
                try {
                    mmServerSocket.close();
                } catch (IOException e1) {
                    Toast.makeText(activity, "Error: " + e1.getMessage(), Toast.LENGTH_LONG).show();
                }

                Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        private void manageConnectedSocket(BluetoothSocket socket) {
            connectedThread = new ConnectedThread(socket);
            ((WaitingActivity) activity).callBack();
        }

        /**
         * Will cancel the listening socket, and cause the thread to finish
         */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }

    protected class MyHandler extends Handler {

        private RemoteMatchActivity remoteMatchActivity;

        public MyHandler(RemoteMatchActivity remoteMatchActivity) {
            this.remoteMatchActivity = remoteMatchActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    remoteMatchActivity.readCallBack(msg.arg1, msg.arg2);
                    break;
            }
        }
    }

    protected class ConnectedThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private RemoteMatchActivity remoteMatchActivity;
        private MyHandler mHandler;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void setRemoteMatchActivity(RemoteMatchActivity remoteMatchActivity) {
            this.remoteMatchActivity = remoteMatchActivity;
            mHandler = new MyHandler(remoteMatchActivity);
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    mHandler.obtainMessage(0, buffer[0], buffer[1], null).sendToTarget();
                } catch (IOException e) {
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                remoteMatchActivity.writeCallBack();
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}