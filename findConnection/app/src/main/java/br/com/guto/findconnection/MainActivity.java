package br.com.guto.findconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Boolean apSwitch = false;
    ImageView ivConnected, ivPing;
    ProgressBar pbChecking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivConnected = (ImageView)findViewById(R.id.ivConnection);
        ivPing = (ImageView)findViewById(R.id.ivPing);
        pbChecking = (ProgressBar)findViewById(R.id.pbChecking);

        checkForConnection();
    }

    public void checkForConnection(){
        final Handler handler = new Handler(Looper.getMainLooper());
        final int tempoDeEspera = 3000;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    SystemClock.sleep(tempoDeEspera);
                    if (apSwitch) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(isNetworkAvailable())
                                    ivConnected.setEnabled(false);
                                else
                                    ivConnected.setEnabled(true);

                                if(isOnline())
                                    ivPing.setEnabled(false);
                                else {
                                    Toast.makeText(MainActivity.this, "Connection not Active", Toast.LENGTH_SHORT).show();
                                    ivPing.setEnabled(true);
                                    connectToCustomWifi();
                                    SystemClock.sleep(tempoDeEspera);

                                }

                            }
                        });
                    }
                }
            }
        }).start();
    }

    private void connectToCustomWifi() {
        String networkSSID = "Pastaroperacao";
        String networkPass = "cElt@0102";

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
        conf.preSharedKey = "\""+ networkPass +"\"";

        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        int netId = wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }
    }

    private boolean isOnline()
    {
        Runtime runtime = Runtime.getRuntime();

        try
        {
            Process process = runtime.exec("/system/bin/ping -c 1 -W 3 8.8.8.8");
            int exitValue = process.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ap_onoff:
                if(!apSwitch){
                    Toast.makeText(this, "Check habilitado", Toast.LENGTH_SHORT).show();
                    apSwitch = true;
                    pbChecking.setVisibility(View.VISIBLE);
                }else {
                    apSwitch = false;
                    pbChecking.setVisibility(View.INVISIBLE);
                    ivConnected.setEnabled(true);
                    ivPing.setEnabled(true);
                    Toast.makeText(this, "Check desabilitado", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
