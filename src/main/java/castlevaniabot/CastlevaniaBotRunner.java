package castlevaniabot;

import castlevaniabot.module.CastlevaniaBotModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import nintaco.api.API;

public class CastlevaniaBotRunner {



    public static void main(final String... args) {

        Injector injector = Guice.createInjector(new CastlevaniaBotModule());


        CastlevaniaBot castlevaniaBot = injector.getInstance(CastlevaniaBot.class);
        API api = injector.getInstance(API.class);

        api.addFrameListener(castlevaniaBot::renderFinished);
        api.addStatusListener(castlevaniaBot::statusChanged);
        api.addActivateListener(castlevaniaBot::apiEnabled);
        api.addDeactivateListener(castlevaniaBot::apiDisabled);
        api.addStopListener(castlevaniaBot::dispose);
        api.run();
    }
}
