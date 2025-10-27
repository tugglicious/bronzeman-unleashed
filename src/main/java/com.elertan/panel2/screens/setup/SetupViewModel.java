package com.elertan.panel2.screens.setup;

import com.elertan.ui.Property;

public final class SetupViewModel implements AutoCloseable {
    public enum Step {
        REMOTE,
        GAME_RULES,
    }

    public final Property<Step> step = new Property<>(Step.REMOTE);

    @Override
    public void close() throws Exception {

    }
}
