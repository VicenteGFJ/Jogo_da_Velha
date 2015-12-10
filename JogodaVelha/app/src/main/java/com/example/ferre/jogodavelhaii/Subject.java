package com.example.ferre.jogodavelhaii;


/**
 * Created by ferre on 12/8/2015.
 */
public interface Subject {
    void subscribe(NewTextView observer);

    void update(int player);
}
