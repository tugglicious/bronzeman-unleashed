package com.elertan;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;

@Singleton
public class PolicyService implements BUPluginLifecycle {

    @Inject
    private BUChatService buChatService;

    @Getter
    private boolean hasNotifiedGameRulesNotLoaded = false;

    @Override
    public void startUp() throws Exception {

    }

    @Override
    public void shutDown() throws Exception {

    }

    public void notifyGameRulesNotLoaded() {
        if (hasNotifiedGameRulesNotLoaded) {
            return;
        }
        hasNotifiedGameRulesNotLoaded = true;

        buChatService.sendMessage(
            "Temporarily enforcing strict game rules to ensure Bronzeman integrity whilst loading...");
    }
}
