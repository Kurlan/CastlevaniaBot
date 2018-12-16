package castlevaniabot.module;

import castlevaniabot.CastlevaniaBot;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import nintaco.api.API;
import nintaco.api.ApiSource;

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
    public CastlevaniaBot getCastlevaniaBot(API api) {
        return new CastlevaniaBot(api);
    }
}
