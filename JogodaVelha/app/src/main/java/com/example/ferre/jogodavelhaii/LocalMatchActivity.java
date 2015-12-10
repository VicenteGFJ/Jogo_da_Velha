package com.example.ferre.jogodavelhaii;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocalMatchActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons;
    private int selectedRow;
    private int selectedCol;
    private int[][] m = new int[3][3];
    private String player;
    private PlacarObserver subject;
    private TextView textView2;
    private TextView textView;
    private NewTextView newTextView;
    private NewTextView newTextView2;
    private LocalMatchActivity localMatchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_match);

        localMatchActivity = this;

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

        buttons = new Button[3][3];

        buttons[0][0] = (Button) findViewById(R.id.localbutton00);
        buttons[0][1] = (Button) findViewById(R.id.localbutton01);
        buttons[0][2] = (Button) findViewById(R.id.localbutton02);

        buttons[1][0] = (Button) findViewById(R.id.localbutton10);
        buttons[1][1] = (Button) findViewById(R.id.localbutton11);
        buttons[1][2] = (Button) findViewById(R.id.localbutton12);

        buttons[2][0] = (Button) findViewById(R.id.localbutton20);
        buttons[2][1] = (Button) findViewById(R.id.localbutton21);
        buttons[2][2] = (Button) findViewById(R.id.localbutton22);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setOnClickListener(this);
                m[i][j] = 0;
            }
        }
    }


    @Override
    public void onClick(View v) {
        try {

            selectedCol = 0;
            selectedRow = 0;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (v.getId() == buttons[i][j].getId()) {
                        selectedCol = j;
                        selectedRow = i;
                    }
                }
            }

            if (m[selectedRow][selectedCol] == 0) {

                m[selectedRow][selectedCol] = 1;
                buttons[selectedRow][selectedCol].setText("0");

                if (checkIfThereIsAWinner()) {
                    setItUp("Voce venceu");
                    subject.update(1);
                    return;
                }

                // faz uma jogada qualquer
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (m[i][j] == 0) {
                            m[i][j] = 2;
                            buttons[i][j].setText("X");

                            if (checkIfThereIsAWinner()) {
                                setItUp("Voce perdeu");
                                subject.update(2);
                                return;
                            }

                            return;
                        }
                    }
                }

                setItUp("Deu velha");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Deu ruim", Toast.LENGTH_SHORT).show();
        }
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

    private void setItUp(String msg) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg + "\nDeseja Continuar")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        localMatchActivity.doCoolStuff();

                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        localMatchActivity.finish();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();


    }

    private void doCoolStuff() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] = 0;
                buttons[i][j].setText(" ");
            }
        }
    }
}
