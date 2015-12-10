package com.example.ferre.jogodavelhaii;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RemoteMatchActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons;
    private boolean canPlay;
    private String as;
    private int m[][] = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    private BluetoothMediator.ConnectedThread connection;
    private byte[] bytes = new byte[2];
    private int selectedRow;
    private int selectedCol;
    private PlacarObserver subject;
    private TextView textView, textView2;
    private NewTextView newTextView, newTextView2;
    private int numberOfmoves;
    private RemoteMatchActivity activity;

    public void setItUp(String msg) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg + "\nDeseja Continuar")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.doCoolStuff();
                    }
                })
                .setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();


    }

    private void doCoolStuff() {
        if (as.equals("SERVER")) {
            canPlay = true;
        } else {
            canPlay = false;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] = 0;
                buttons[i][j].setText(" ");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_match);

        activity = this;

        connection = IUseSingletonDespiteMyTeacherWill.getInstance().getConnection();
        connection.setRemoteMatchActivity(this);
        try {
            subject = new PlacarObserver();

            textView = (TextView) findViewById(R.id.localvoce);
            textView2 = (TextView) findViewById(R.id.localoponente);

            textView.setText("0");
            textView2.setText("0");

            newTextView = new NewTextView(textView);
            newTextView2 = new NewTextView(textView2);

            subject.subscribe(newTextView);
            subject.subscribe(newTextView2);

        } catch (Exception e) {
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        as = getIntent().getStringExtra("as");

        buttons = new Button[3][3];

        buttons[0][0] = (Button) findViewById(R.id.button00);
        buttons[0][1] = (Button) findViewById(R.id.button01);
        buttons[0][2] = (Button) findViewById(R.id.button02);

        buttons[1][0] = (Button) findViewById(R.id.button10);
        buttons[1][1] = (Button) findViewById(R.id.button11);
        buttons[1][2] = (Button) findViewById(R.id.button12);

        buttons[2][0] = (Button) findViewById(R.id.button20);
        buttons[2][1] = (Button) findViewById(R.id.button21);
        buttons[2][2] = (Button) findViewById(R.id.button22);

        numberOfmoves = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] = 0;
                buttons[i][j].setText(" ");
                buttons[i][j].setOnClickListener(this);
            }
        }

        connection.start();
    }

    @Override
    public void onClick(View v) {

        try {
            if (!canPlay)
                return;

            canPlay = false;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (v.getId() == buttons[i][j].getId()) {
                        bytes[0] = Byte.valueOf(String.valueOf(i));
                        bytes[1] = Byte.valueOf(String.valueOf(j));
                        selectedRow = i;
                        selectedCol = j;
                    }
                }
            }

            connection.write(bytes);

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void writeCallBack() {
        if (m[selectedRow][selectedCol] == 0) {
            m[selectedRow][selectedCol] = 1;

            numberOfmoves++;

            if (checkIfThereIsAWinner()) {
                setItUp("Voce venceu");
                subject.update(1);
                return;
            } else {
                if (numberOfmoves == 9) {
                    setItUp("Deu velha");
                    numberOfmoves = 0;
                    return;
                }
            }

            if (as.equals("SERVER"))
                buttons[selectedRow][selectedCol].setText("X");
            else
                buttons[selectedRow][selectedCol].setText("O");

        } else {
            canPlay = true;
        }
    }


    public void readCallBack(int row, int column) {
        m[row][column] = 2;

        numberOfmoves++;

        if (checkIfThereIsAWinner()) {
            setItUp("Voce perdeu");
            subject.update(2);
            return;
        } else {
            if (numberOfmoves == 9) {
                setItUp("Deu velha");
                numberOfmoves = 0;
                return;
            }
        }

        if (as.equals("SERVER"))
            buttons[row][column].setText("O");
        else
            buttons[row][column].setText("X");

        canPlay = true;
    }


    private boolean checkIfThereIsAWinner() {

        boolean test1, test2;
        test1 = test2 = true;

        for (int i = 0; i < 3; i++) {
            test1 = test1 && (m[i][i] == 1);
            test2 = test2 && (m[i][i] == 2);
        }

        if (test1 || test2)
            return true;

        for (int i = 0; i < 3; i++) {
            test1 = test2 = true;

            for (int j = 0; j < 3; j++) {
                test1 = test1 && (m[i][j] == 1);
                test2 = test2 && (m[i][j] == 2);
            }

            if (test1 || test2)
                return true;

            test1 = test2 = true;

            for (int j = 0; j < 3; j++) {
                test1 = test1 && (m[j][i] == 1);
                test2 = test2 && (m[j][i] == 2);
            }

            if (test1 || test2)
                return true;
        }

        return false;

    }
}

