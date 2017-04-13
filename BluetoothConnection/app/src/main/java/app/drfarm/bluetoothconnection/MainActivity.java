package app.drfarm.bluetoothconnection;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private ProgressDialog mProgressDlg;
    private BluetoothAdapter myBluetooth;
    private Intent connected;

    private Button btBeacon;

    private String beaconIMEI = "B8:D9:CE:2B:2F:AD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btBeacon = (Button)findViewById(R.id.btBeacon);
        btBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothConfig();
                btBeacon.setVisibility(View.INVISIBLE);
            }
        });

        connected = new Intent(MainActivity.this, Connected.class);

        mProgressDlg = new ProgressDialog(this);

        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                myBluetooth.cancelDiscovery();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            accessPermissions();
            bluetoothConfig();
        } else {
            bluetoothConfig();
        }
    }

    public void bluetoothConfig (){
        btBeacon.setVisibility(View.INVISIBLE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager btManager;
            btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
            myBluetooth = btManager.getAdapter();
        } else {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
        }

        if(myBluetooth==null){
            Toast.makeText(this, "Device does not support bluetooth connections", Toast.LENGTH_SHORT).show();
        }else{
            if (!myBluetooth.isEnabled()) {
                Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOn, 0);
            }

            Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
            boolean status = false;

            for(BluetoothDevice device: pairedDevices){
                if (device.getAddress() == beaconIMEI){
                    Toast.makeText(this, "Beacon Encontrado", Toast.LENGTH_SHORT).show();
                    startActivity(connected);
                    status = true;
                    break;
                }
            }

            if (status == false){
                IntentFilter filter = new IntentFilter();

                filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
                filter.addAction(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

                registerReceiver(mReceiver, filter);

                myBluetooth.startDiscovery();
            }
        }
    }

    private void accessPermissions() {
        int accessCoarseLocation = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFineLocation   = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        int bluetooth   = ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH);

        List<String> listRequestPermission = new ArrayList<String>();

        if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (accessFineLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (bluetooth != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(android.Manifest.permission.BLUETOOTH);
        }

        if (!listRequestPermission.isEmpty()) {
            for(String lrPerm : listRequestPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{lrPerm},
                        1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0) {
                    for (int gr : grantResults) {
                        // Check if request is granted or not
                        if (gr != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    bluetoothConfig();
                } else {
                    Toast.makeText(this, "No Permission Granted for Bluetooth connection", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    Toast.makeText(context, "Enabled", Toast.LENGTH_SHORT).show();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(context, "Inicia Busca", Toast.LENGTH_SHORT).show();
                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(context, "Finaliza Busca", Toast.LENGTH_SHORT).show();
                mProgressDlg.dismiss();

                Toast.makeText(context, "Beacon não encontrado", Toast.LENGTH_SHORT).show();
                btBeacon.setVisibility(View.VISIBLE);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Toast.makeText(context, "Encontrou dispositivo disponível", Toast.LENGTH_SHORT).show();
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Toast.makeText(context, device.getName() + " " +device.getAddress(), Toast.LENGTH_SHORT).show();
                if (device.getAddress().equals(beaconIMEI)){
                    mProgressDlg.dismiss();
                    Toast.makeText(context, "Beacon Encontrado", Toast.LENGTH_SHORT).show();
                    startActivity(connected);
                }
            }
        }
    };
}
