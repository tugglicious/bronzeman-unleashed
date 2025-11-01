package com.elertan.policies;

import com.elertan.AccountConfigurationService;
import com.elertan.BUChatService;
import com.elertan.BUPluginLifecycle;
import com.elertan.GameRulesService;
import com.elertan.PolicyService;
import com.elertan.chat.ChatMessageProvider;
import com.elertan.models.GameRules;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.game.ItemStack;

@Slf4j
@Singleton
public class PlayerVersusPlayerPolicy extends PolicyBase implements BUPluginLifecycle {

    @Inject
    private Client client;
    @Inject
    private BUChatService buChatService;
    @Inject
    private ChatMessageProvider chatMessageProvider;

    private ConcurrentHashMap<String, ConcurrentLinkedQueue<PlayerDeathLocation>> playerDeathLocations;

    @Inject
    public PlayerVersusPlayerPolicy(AccountConfigurationService accountConfigurationService,
        GameRulesService gameRulesService, PolicyService policyService) {
        super(accountConfigurationService, gameRulesService, policyService);
    }

    @Override
    public void startUp() throws Exception {
        playerDeathLocations = new ConcurrentHashMap<>();
    }

    @Override
    public void shutDown() throws Exception {
        playerDeathLocations = null;
    }

    public void onActorDeath(ActorDeath e) {
        Actor actor = e.getActor();
        boolean isPlayer = actor instanceof Player;
        if (!isPlayer) {
            log.debug("Actor is not a player");
            return;
        }
        Player otherPlayer = (Player) actor;
        Player localPlayer = client.getLocalPlayer();
        if (Objects.equals(actor, localPlayer)) {
            log.debug("we died...");
            return;
        }
        if (playerDeathLocations == null) {
            log.debug("playerDeathLocations is null");
            return;
        }
        PolicyContext policyContext = createContext();
        GameRules gameRules = policyContext.getGameRules();
        if (policyContext.isMustEnforceStrictPolicies()) {
            addPlayerDeathLocation(otherPlayer);
        }
//        if (gameRules == null || gameRules.) {
//            addPlayerDeathLocation(otherPlayer);
//        }
    }

    private void addPlayerDeathLocation(Player player) {
        long tickCount = client.getTickCount();

        String playerName = player.getName();
        WorldPoint actorLocation = player.getWorldLocation();
        WorldArea worldArea = actorLocation.toWorldArea();

        ConcurrentLinkedQueue<PlayerDeathLocation> deathLocations = playerDeathLocations.computeIfAbsent(
            playerName,
            k -> new ConcurrentLinkedQueue<>()
        );
        Point point = new Point(worldArea.getX(), worldArea.getY());
        PlayerDeathLocation playerDeathLocation = new PlayerDeathLocation(point, tickCount);
        deathLocations.add(playerDeathLocation);
        log.info(
            "Added death location for player {} at tick count {} for x: {}, y: {}",
            playerName,
            tickCount,
            point.getX(),
            point.getY()
        );
    }

    public void onPlayerLootReceived(PlayerLootReceived e) {
        // This covers PvP loot, including loot keys when opened.
        // Tag this batch as PvP using e.getPlayerName() or source info.
        Player player = e.getPlayer();
        if (player == null) {
            return;
        }
        log.info("loot received for player: {}", player.getName());

        if (playerDeathLocations == null) {
            log.info("playerDeathLocations is null");
            return;
        }

        Collection<ItemStack> itemStacks = e.getItems();
        if (itemStacks == null) {
            log.info("item stacks is null, ignoring");
            return;
        }
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                continue;
            }
            int itemId = itemStack.getId();
            log.info("item id: {} -> {}", itemId, itemStack.getQuantity());
        }
    }

    @Value
    private static class PlayerDeathLocation {

        Point location;
        long tickCount;
    }
}
