package com.example.ferre.jogodavelhaii;

import android.widget.TextView;

public class NewTextView implements Observer {

    TextView textView;

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public NewTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void update(int player) {

        int value;

        try {
            value = Integer.parseInt(textView.getText().toString()) + 1;
        } catch (Exception e) {
            value = 0;
        }

        if (player == 1 && (textView.getId() == R.id.localvoce || textView.getId() == R.id.remotevoce)) {
            textView.setText("" + value);
        }

        if (player == 2 && (textView.getId() == R.id.localoponente || textView.getId() == R.id.remoteoponente)) {
            textView.setText("" + value);
        }
    }
}
