package com.elertan.panel;

import com.elertan.AccountConfigurationService;
import com.elertan.models.AccountConfiguration;
import com.elertan.ui.Property;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class BUPanelViewModel implements AutoCloseable {

    public final Property<Screen> screen = new Property<>(Screen.WAIT_FOR_LOGIN);
    private final Consumer<AccountConfiguration> currentAccountConfigurationChangeListener = this::currentAccountConfigurationChangeListener;
    private final AccountConfigurationService accountConfigurationService;

    private BUPanelViewModel(AccountConfigurationService accountConfigurationService) {
        this.accountConfigurationService = accountConfigurationService;

        accountConfigurationService.addCurrentAccountConfigurationChangeListener(
            currentAccountConfigurationChangeListener);
    }

    @Override
    public void close() throws Exception {
        accountConfigurationService.removeCurrentAccountConfigurationChangeListener(
            currentAccountConfigurationChangeListener);
    }

    private void currentAccountConfigurationChangeListener(
        AccountConfiguration accountConfiguration) {
        if (accountConfiguration == null) {
            screen.set(Screen.SETUP);
        } else {
            screen.set(Screen.MAIN);
        }
    }

    public enum Screen {
        WAIT_FOR_LOGIN,
        SETUP,
        MAIN
    }

    @ImplementedBy(FactoryImpl.class)
    public interface Factory {

        BUPanelViewModel create();
    }

    @Singleton
    private static final class FactoryImpl implements Factory {

        @Inject
        private AccountConfigurationService accountConfigurationService;

        @Override
        public BUPanelViewModel create() {
            return new BUPanelViewModel(accountConfigurationService);
        }
    }
}
