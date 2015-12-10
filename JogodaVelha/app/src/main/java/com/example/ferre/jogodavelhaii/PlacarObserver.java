package com.example.ferre.jogodavelhaii;

import java.util.ArrayList;

/**
 * Created by ferre on 12/8/2015.
 */
public class PlacarObserver implements Subject {

    private ArrayList<Observer> observers;

    public PlacarObserver() {
        observers = new ArrayList<>();
    }

    @Override
    public void subscribe(NewTextView observer) {
        observers.add(observer);
    }

    @Override
    public void update(int player) {
        for (Observer observer : observers) {
            observer.update(player);
        }
    }

}
