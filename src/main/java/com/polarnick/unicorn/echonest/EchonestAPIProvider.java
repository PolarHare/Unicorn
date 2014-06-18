package com.polarnick.unicorn.echonest;

import com.echonest.api.v4.EchoNestAPI;

/**
 * @author Nickolay Polyarnyi
 */
public class EchonestAPIProvider {

    private final String apiKey;

    public EchonestAPIProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    public EchoNestAPI getEchoNestAPI() {
        return new EchoNestAPI(apiKey);
    }

}
