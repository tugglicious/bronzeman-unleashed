package com.elertan.policies;

import com.elertan.AccountConfigurationService;
import com.elertan.BUChatService;
import com.elertan.BUPluginLifecycle;
import com.elertan.BUSoundHelper;
import com.elertan.GameRulesService;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.MenuAction;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.MenuOptionClicked;

@Slf4j
public class GroundItemsPolicy extends PolicyBase implements BUPluginLifecycle {

    @Inject
    private Client client;
    @Inject
    private BUSoundHelper buSoundHelper;
    @Inject
    private BUChatService buChatService;

    @Inject
    public GroundItemsPolicy(AccountConfigurationService accountConfigurationService,
        GameRulesService gameRulesService) {
        super(accountConfigurationService, gameRulesService);
    }

    @Override
    public void startUp() throws Exception {

    }

    @Override
    public void shutDown() throws Exception {

    }

    public void onItemSpawned(ItemSpawned event) {
        if (!shouldEnforcePolicies()) {
            return;
        }

        TileItem tileItem = event.getItem();
    }

    public void onItemDespawned(ItemDespawned event) {
        if (!shouldEnforcePolicies()) {
            return;
        }

        TileItem tileItem = event.getItem();
    }

    public void onMenuOptionClicked(MenuOptionClicked event) {
        if (!shouldEnforcePolicies()) {
            return;
        }

        MenuAction menuAction = event.getMenuAction();
        boolean isGroundItemMenuAction =
            menuAction.ordinal() >= MenuAction.GROUND_ITEM_FIRST_OPTION.ordinal()
                && menuAction.ordinal() <= MenuAction.GROUND_ITEM_FIFTH_OPTION.ordinal();
        if (!isGroundItemMenuAction) {
            return;
        }

        String menuOption = event.getMenuOption();
        if (!menuOption.equals("Take")) {
            return;
        }
        int itemId = event.getId();
        if (itemId <= 1) {
            return;
        }

        TileItem tileItem = getClickedTileItem(event);
        if (tileItem == null) {
            log.warn(
                "Ground item not found at scene ({}, {}) for id {}",
                event.getParam0(),
                event.getParam1(),
                itemId
            );
            return;
        }

        ItemComposition itemComposition = client.getItemDefinition(itemId);

        int ownership = tileItem.getOwnership();
        if (ownership == TileItem.OWNERSHIP_NONE) {
            log.info("Item '{}' is not owned by anyone, allow take", itemComposition.getName());
            return;
        }
        if (ownership == TileItem.OWNERSHIP_SELF) {
            log.info("Item '{}' is owned by me, allow take", itemComposition.getName());
            return;
        }

        event.consume();
//        buSoundHelper.playDisabledSound();

        buChatService.sendMessage(
            "You're a Bronzeman with ground item restrictions, so you can't take that.");

//        log.info(
//            "Taking '{}' x{} at scene ({}, {}) plane {}",
//            itemComposition.getName(),
//            tileItem.getQuantity(),
//            event.getParam0(),
//            event.getParam1(),
//            client.getPlane()
//        );
    }

    private TileItem getClickedTileItem(MenuOptionClicked event) {
        // For ground item menu actions, param0 = scene X, param1 = scene Y, id = item ID
        final int sceneX = event.getParam0();
        final int sceneY = event.getParam1();
        final int itemId = event.getId();
        final int plane = client.getPlane();

        Scene scene = client.getScene();
        if (scene == null) {
            return null;
        }

        Tile[][][] tiles = scene.getTiles();
        if (tiles == null || plane < 0 || plane >= tiles.length) {
            return null;
        }

        if (sceneX < 0 || sceneY < 0 || sceneX >= tiles[plane].length
            || sceneY >= tiles[plane][sceneX].length) {
            return null;
        }

        Tile tile = tiles[plane][sceneX][sceneY];
        if (tile == null || tile.getGroundItems() == null) {
            return null;
        }

        for (TileItem ti : tile.getGroundItems()) {
            if (ti.getId() == itemId) {
                return ti;
            }
        }
        return null;
    }
}
