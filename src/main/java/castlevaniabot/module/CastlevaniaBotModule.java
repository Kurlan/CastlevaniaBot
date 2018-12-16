package castlevaniabot.module;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.maps.MapLoader;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.GameState;
import castlevaniabot.model.gameelements.MapRoutes;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import nintaco.api.API;
import nintaco.api.ApiSource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CastlevaniaBotModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public API getAPI() {
        ApiSource.initRemoteAPI("localhost", 9999);
        return ApiSource.getAPI();
    }

    @Provides
    @Singleton
    public CastlevaniaBot getCastlevaniaBot(API api, Map<String, MapRoutes> allRoutes, GameObject[] gameObjects) {
        return new CastlevaniaBot(api, allRoutes, gameObjects);
    }

    @Provides
    @Singleton
    public Map<String, MapRoutes> getAllRoutes(MapLoader mapLoader) throws Exception {
        Map<String, MapRoutes> routes = new HashMap<>();

            try(final BufferedReader br = new BufferedReader(new InputStreamReader(
                    CastlevaniaBot.class.getClassLoader().getResourceAsStream("maps/files.txt")))) {
                String name;
                while((name = br.readLine()) != null) {
                    name = name.trim();
                    if (name.length() > 0) {
                        try(final DataInputStream in = new DataInputStream(
                                new BufferedInputStream(CastlevaniaBot.class.getClassLoader().getResourceAsStream("maps/" + name)))) {
                            MapRoutes mapRoutes = mapLoader.loadMap(name, in);
                            routes.put(name, mapRoutes);
                        }
                    }
                }
            }

        return routes;
    }

    @Provides
    @Singleton
    public GameObject[] getGameObjects() {
        GameObject[] gameObjects = new GameObject[128];
        for(int i = gameObjects.length - 1; i >= 0; --i) {
            gameObjects[i] = new GameObject();
        }
        return gameObjects;
    }

    @Provides
    @Singleton
    public GameState getGameState() {
        return new GameState();
    }
}
