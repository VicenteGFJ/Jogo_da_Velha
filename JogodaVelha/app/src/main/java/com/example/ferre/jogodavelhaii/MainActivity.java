package com.example.ferre.jogodavelhaii;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button servidor, cliente, local;
    private BluetoothMediator bluetoothMediator;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothMediator = new BluetoothMediator();

        if (!bluetoothMediator.deviceHasBluetooth()) {
            intent = new Intent(this, LocalMatchActivity.class);
            startActivity(intent);
            finish();
        } else {

            servidor = (Button) findViewById(R.id.buttonservidor);
            cliente = (Button) findViewById(R.id.buttoncliente);
            local = (Button) findViewById(R.id.buttonlocal);

            servidor.setOnClickListener(this);
            cliente.setOnClickListener(this);
            local.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == local.getId()) {
            intent = new Intent(this, LocalMatchActivity.class);
            startActivity(intent);
        } else {

            if (!bluetoothMediator.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            } else {
                if (v.getId() == servidor.getId())
                    intent = new Intent(this, WaitingActivity.class);
                if (v.getId() == cliente.getId())
                    intent = new Intent(this, ListingActivity.class);

                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(), "Colabore conosco, liga esse bluetooth !!!", Toast.LENGTH_LONG).show();
        }
    }
}
