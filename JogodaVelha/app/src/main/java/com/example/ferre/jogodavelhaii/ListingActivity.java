package com.example.ferre.jogodavelhaii;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private BluetoothMediator bluetoothMediator;
    private ArrayList<BluetoothDevice> devices;
    private ArrayAdapter arrayAdapter;
    private ListView pairedDevicesList;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        intent = new Intent(this, RemoteMatchActivity.class);

        bluetoothMediator = new BluetoothMediator();
        devices = bluetoothMediator.getPairedDevices();

        pairedDevicesList = (ListView) findViewById(R.id.paireddeviceslist);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, devices);

        pairedDevicesList.setAdapter(arrayAdapter);

        pairedDevicesList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        bluetoothMediator.connectTo(devices.get(position), this);
    }

    public void callBack() {
        IUseSingletonDespiteMyTeacherWill.getInstance().holdConnection(bluetoothMediator.getConnection());
        intent.putExtra("as", "CLIENT");
        startActivity(intent);
    }
}
