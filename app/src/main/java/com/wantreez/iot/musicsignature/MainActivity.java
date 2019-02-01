package com.wantreez.iot.musicsignature;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wantreez.iot.musicsignature.bt.BluetoothSPP;
import com.wantreez.iot.musicsignature.bt.BluetoothState;
import com.wantreez.iot.musicsignature.bt.BluetoothList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static BluetoothSPP bt;
    public static TextView textStatus, textRead;
    private static BluetoothSPP.OnDataReceivedListener mDataReceivedListener = null;
    SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPref =  getPreferences(Context.MODE_PRIVATE);

        bt = new BluetoothSPP(this);

        textStatus = (TextView) findViewById(R.id.txt_bt);
        textRead = (TextView) findViewById(R.id.txt_wp);

        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {

                Log.d("DATA", message);
                if(mDataReceivedListener != null)
                    mDataReceivedListener.onDataReceived(data, message);

            }
        });//bt.setBluetoothStateListener();

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                textStatus.setText("BT : Not connect");
            }

            public void onDeviceConnectionFailed() {
                textStatus.setText("BT : Connection failed");
            }

            public void onDeviceConnected(String name, String address) {
                textStatus.setText("BT : " + name);
                findViewById(R.id.btn_wp).setEnabled(true);
                bt.send("wifi_status",true);
            }
        });


        findViewById(R.id.btn_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BluetoothList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

            }
        });
        findViewById(R.id.btn_wp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,WifiActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


            }
        });
        findViewById(R.id.btn_wp).setEnabled(false);

        findViewById(R.id.btn_wp_ip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt.send("wifi_status",true);

            }
        });
        findViewById(R.id.btn_reboot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt.send("reboot",true);
            }
        });

    }


    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                setup();
            }
        }
    }
    //
    public void setup() {

        bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String address = sharedPref.getString("BT_ADDRESS", "");
                if(address != null && address.length() > 0)
                    bt.connect(address);

            }
        },1000);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {

                String address = data.getExtras().getString(BluetoothState.EXTRA_DEVICE_ADDRESS);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("BT_ADDRESS", address);
                editor.commit();
                bt.connect(data);

            }
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public static void setOnDataReceivedListener (BluetoothSPP.OnDataReceivedListener listener) {
        mDataReceivedListener = listener;
    }
}
