package com.example.ferre.jogodavelhaii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class WaitingActivity extends AppCompatActivity {

    private BluetoothMediator bluetoothMediator;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        intent = new Intent(this, RemoteMatchActivity.class);

        bluetoothMediator = new BluetoothMediator();
        bluetoothMediator.waitConnection(this);


    }

    public void callBack() {
        IUseSingletonDespiteMyTeacherWill.getInstance().holdConnection(bluetoothMediator.getConnection());
        intent.putExtra("as", "SERVER");
        startActivity(intent);
    }
}
