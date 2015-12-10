package com.example.ferre.jogodavelhaii;

/**
 * Created by ferre on 12/8/2015.
 */
public class IUseSingletonDespiteMyTeacherWill {
    private static final int MESSAGE_READ = 0;
    private static IUseSingletonDespiteMyTeacherWill instance = null;
    private BluetoothMediator.ConnectedThread connection;


    private IUseSingletonDespiteMyTeacherWill() {
    }


    public static IUseSingletonDespiteMyTeacherWill getInstance() {
        if (instance == null)
            instance = new IUseSingletonDespiteMyTeacherWill();

        return instance;
    }

    public void holdConnection(BluetoothMediator.ConnectedThread connection) {
        instance = IUseSingletonDespiteMyTeacherWill.getInstance();
        instance.connection = connection;
    }

    public BluetoothMediator.ConnectedThread getConnection() {
        return connection;
    }
}
