package com.elertan;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;

@Singleton
public class BUSoundHelper {

    private static final int DISABLED_SOUND_EFFECT_ID = 2277;

    @Inject
    private Client client;

    public void playDisabledSound() {
        client.playSoundEffect(DISABLED_SOUND_EFFECT_ID);
    }
}
