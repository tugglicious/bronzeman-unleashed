package com.elertan.panel2;

import com.elertan.AccountConfigurationService;
import com.elertan.models.AccountConfiguration;
import com.elertan.ui.Property;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public final class BUPanelViewModel implements AutoCloseable {
    public enum Screen {
        WAIT_FOR_LOGIN,
        SETUP,
        MAIN
    }

    public final Property<Screen> screen = new Property<>(Screen.WAIT_FOR_LOGIN);

    private final Consumer<AccountConfiguration> currentAccountConfigurationChangeListener = this::currentAccountConfigurationChangeListener;
    private final AccountConfigurationService accountConfigurationService;

    @Inject
    public BUPanelViewModel(AccountConfigurationService accountConfigurationService) {
        this.accountConfigurationService = accountConfigurationService;

        accountConfigurationService.addCurrentAccountConfigurationChangeListener(currentAccountConfigurationChangeListener);
    }

    @Override
    public void close() throws Exception {
        accountConfigurationService.removeCurrentAccountConfigurationChangeListener(currentAccountConfigurationChangeListener);
    }

    private void currentAccountConfigurationChangeListener(AccountConfiguration accountConfiguration) {
        if (accountConfiguration == null) {
            screen.set(Screen.SETUP);
        } else {
            screen.set(Screen.MAIN);
        }
    }
}
