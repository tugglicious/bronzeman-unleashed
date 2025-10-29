package com.elertan;

import com.elertan.resource.BUImageUtil;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.ImageIcon;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.IndexedSprite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.util.ImageUtil;

@Slf4j
@Singleton
public class BUResourceService implements BUPluginLifecycle {

    private static final String ICON_FILE_PATH = "/icon.png";
    private static final String CHECKMARK_ICON_FILE_PATH = "/checkmark-icon.png";
    private static final String CONFIGURE_ICON_FILE_PATH = "/configure-icon.png";
    private static final String LOADING_SPINNER_FILE_PATH = "/loading-spinner.gif";
    @Getter
    private final BufferedImage iconBufferedImage = ImageUtil.loadImageResource(
        BUPlugin.class,
        ICON_FILE_PATH
    );
    @Getter
    private final BufferedImage checkmarkIconBufferedImage = ImageUtil.loadImageResource(
        BUPlugin.class,
        CHECKMARK_ICON_FILE_PATH
    );
    @Getter
    private final BufferedImage configureIconBufferedImage = ImageUtil.loadImageResource(
        BUPlugin.class,
        CONFIGURE_ICON_FILE_PATH
    );
    @Getter
    private final ImageIcon loadingSpinnerImageIcon = new ImageIcon(Objects.requireNonNull(BUPlugin.class.getResource(
        LOADING_SPINNER_FILE_PATH)));
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Getter
    private BUModIcons buModIcons;

    @Override
    public void startUp() {
        this.initializeModIcons();
    }

    @Override
    public void shutDown() {

    }

    private void initializeModIcons() {
        IndexedSprite[] modIcons = client.getModIcons();
        if (modIcons == null) {
            // Retry later when is initialized
            clientThread.invokeLater(this::initializeModIcons);
            return;
        }

        // Mod icons
        BufferedImage chatIcon = BUImageUtil.resizeNearest(iconBufferedImage, 13, 13);
        IndexedSprite chatIconSprite = ImageUtil.getImageIndexedSprite(chatIcon, client);

        int chatIconId = modIcons.length;

        IndexedSprite[] newModIcons = Arrays.copyOf(modIcons, modIcons.length + 1);
        newModIcons[chatIconId] = chatIconSprite;
        client.setModIcons(newModIcons);

        this.buModIcons = new BUModIcons(chatIconId);
        log.info("BUResourceService: mod icons and sprites initialized");
    }

    public static class BUModIcons {

        @Getter
        final private int chatIconId;

        public BUModIcons(int chatIconId) {
            this.chatIconId = chatIconId;
        }
    }
}
