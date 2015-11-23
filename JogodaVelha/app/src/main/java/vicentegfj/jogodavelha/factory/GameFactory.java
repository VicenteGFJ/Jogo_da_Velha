package vicentegfj.jogodavelha.factory;

import vicentegfj.jogodavelha.MainActivity;

/**
 * Created by guilherme on 23/11/15.
 */
public class GameFactory {
    private final MainActivity activity;

    public GameFactory(MainActivity mainActivity) {
        activity = mainActivity;
    }

    public Game makeGame(String type) {
        if (type.equals("local")){
            return new LocalGame(activity);
        }else if (type.equals("local")){
            return new RemoteGame(activity);
        }else{
            return null;
        }

    }
}
