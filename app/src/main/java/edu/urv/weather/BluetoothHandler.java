package edu.urv.weather;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.io.InputStream;
import java.util.UUID;

public class BluetoothHandler {
    private final TextView temperatureTextView;
    private final Handler handler;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private Context context;



    // UUID for Serial Port Protocol (SPP)
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public BluetoothHandler(Context context, TextView temperatureTextView) {
        this.context = context;
        this.temperatureTextView = temperatureTextView;
        this.handler = new Handler(Looper.getMainLooper()); // Handler for the main thread
    }

    public void connectToDevice(String deviceAddress) {
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            // Get the remote device
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);

            // Create a Bluetooth socket
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothSocket = device.createRfcommSocketToServiceRecord(SPP_UUID);

            // Connect to the device
            bluetoothSocket.connect();

            // Get the input stream
            inputStream = bluetoothSocket.getInputStream();
            System.out.println("Connected to " + device.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveData() {
        new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                int bytes;

                while (true) {
                    bytes = inputStream.read(buffer);
                    String receivedData = new String(buffer, 0, bytes);
                    System.out.println("Received: " + receivedData);

                    // Add logic to process or display data
                    // Update the TextView on the UI thread
                    handler.post(() -> {
                        temperatureTextView.setText(receivedData);
                    });

                    // Sleep for 1 second before the next update
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (bluetoothSocket != null) bluetoothSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
