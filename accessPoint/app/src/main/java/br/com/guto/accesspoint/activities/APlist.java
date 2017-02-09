package br.com.guto.accesspoint.activities;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import br.com.guto.accesspoint.R;
import br.com.guto.accesspoint.support.ClientScanResult;
import br.com.guto.accesspoint.support.WifiApManager;

public class APlist extends AppCompatActivity {

    //WifiManager wifiManager;
    WifiApManager wifiApManager;
    ListView lvDevices;
    TextView tvDevices;
    Boolean apSwitch = false;

    List disp;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplist);

        //wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        lvDevices = (ListView)findViewById(R.id.lvDevices);
        tvDevices = (TextView)findViewById(R.id.tvDevices);
        disp = new ArrayList<String>();

        wifiApManager = new WifiApManager(this);

        //Apenas a criação do AccessPoint está funcional
        carregaConnectedDevices();
    }

    private void carregaConnectedDevices() {
        final Handler handler = new Handler(Looper.getMainLooper());
        final int tempoDeEspera = 2000;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    SystemClock.sleep(tempoDeEspera);
                    if (apSwitch) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                            /*WifiInfo info = wifiManager.getConnectionInfo();
                            String address = info.getMacAddress();
                            Toast.makeText(APlist.this, address, Toast.LENGTH_SHORT).show();
                            //if(!disp.contains(address)) {
                                adapter = new ArrayAdapter<>(APlist.this, android.R.layout.simple_list_item_1, disp);
                                lvDevices.setAdapter(adapter);
                            //}*/
                                scan();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private void scan() {
        ArrayList<ClientScanResult> clients = wifiApManager.getClientList(false);

        tvDevices.append("Clients: \n");
        for (ClientScanResult clientScanResult : clients) {
            tvDevices.append("####################\n");
            tvDevices.append("IpAddr: " + clientScanResult.getIpAddr() + "\n");
            tvDevices.append("Device: " + clientScanResult.getDevice() + "\n");
            tvDevices.append("HWAddr: " + clientScanResult.getHWAddr() + "\n");
            tvDevices.append("isReachable: " + clientScanResult.isReachable() + "\n");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aplist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        apSwitch = false;
        wifiApManager.setWifiEnabled(false);

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ap_onoff:
                if(!apSwitch){
                    apSwitch = true;
                    wifiApManager.createWifiAccessPoint(this);
                    //carregaConnectedDevices();
                }else {
                    apSwitch = false;
                    Toast.makeText(this, "AP desabilitado", Toast.LENGTH_SHORT).show();
                    wifiApManager.setWifiEnabled(false);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}