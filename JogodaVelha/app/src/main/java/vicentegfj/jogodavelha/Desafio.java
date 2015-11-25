package vicentegfj.jogodavelha;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

public class Desafio extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio);

        arrayList = new ArrayList<>();
        arrayAdapter =  new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView = (ListView)findViewById(R.id.desafiolist);
        listView.setAdapter(arrayAdapter);
        sim();

        Button button = (Button)findViewById(R.id.refreshdesafio);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sim();
            }
        });


    }

    private void sim() {

        arrayList.clear();
        arrayAdapter.clear();
        arrayAdapter.notifyDataSetChanged();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                arrayList.add(device.getName() + "\n" + device.getAddress());
                arrayAdapter.notifyDataSetChanged();
            }
        }

        arrayAdapter.notifyDataSetChanged();

    }
}
