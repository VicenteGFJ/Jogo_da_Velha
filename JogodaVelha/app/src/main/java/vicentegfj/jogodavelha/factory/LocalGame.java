package vicentegfj.jogodavelha.factory;

import android.content.Intent;

import vicentegfj.jogodavelha.LocalMatchActivity;
import vicentegfj.jogodavelha.MainActivity;

/**
 * Created by guilherme on 23/11/15.
 */
public class LocalGame extends Game {

    public LocalGame(MainActivity activity) {
        super(activity);
    }

    @Override
    public void start() {
        Intent intent = new Intent(getActivity(), LocalMatchActivity.class);
        getActivity().startActivity(intent);
    }
}
