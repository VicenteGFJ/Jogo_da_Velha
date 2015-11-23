package vicentegfj.jogodavelha.factory;

import android.app.Activity;
import android.content.Intent;

import vicentegfj.jogodavelha.MainActivity;

/**
 * Created by guilherme on 23/11/15.
 */
public abstract class Game {

    private Activity activity;

    public Game(MainActivity activity) {
        this.activity = activity;
    }

    public abstract void start();

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
