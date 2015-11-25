package vicentegfj.jogodavelha;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import vicentegfj.jogodavelha.factory.Game;
import vicentegfj.jogodavelha.factory.GameFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BluetoothAdapter bluetoothAdapter;
    private GameFactory gameFactory;
    private Game game;

    private Button playLocal;
    private Button playRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameFactory = new GameFactory(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        playLocal =  (Button)findViewById(R.id.playlocal);
        playRemote =  (Button)findViewById(R.id.playremote);

        playLocal.setOnClickListener(this);
        playRemote.setOnClickListener(this);

        if ( bluetoothAdapter == null ){
            playRemote.setEnabled(false);
            game = gameFactory.makeGame("local");
            game.start();
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.playlocal ){
            game = gameFactory.makeGame("local");
            game.start();
        }else{
            if (!bluetoothAdapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent,1);
            }else{
                Intent intent = new Intent(getApplicationContext(), Desafio.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(),"Liga o bluetooth porra !!!", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(getApplicationContext(),Desafio.class);
            startActivity(intent);
        }
    }

}
