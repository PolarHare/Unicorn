package com.polarnick.unicorn.echonest;

import com.echonest.api.v4.EchoNestAPI;

/**
 * @author Nickolay Polyarnyi
 */
public class EchonestAPIProvider {

    private final String apiKey;
    private final ThreadLocal<EchoNestAPI> api = new ThreadLocal<EchoNestAPI>() {
        @Override
        protected EchoNestAPI initialValue() {
            return new EchoNestAPI(apiKey);
        }
    };

    public EchonestAPIProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    public EchoNestAPI get() {
        return api.get();
    }

}
