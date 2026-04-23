package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.common.PartiesPlugin;
import com.bgsoftware.superiorskyblock.api.events.PreIslandCreateEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class SuperiorSkyblockHandler implements Listener {
    @NotNull private final PartiesPlugin plugin;
    private static final String ADDON_NAME = "SuperiorSkyblock2";
    
    public void init() {
        if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
            ((BukkitPartiesBootstrap) plugin.getBootstrap()).getServer().getPluginManager().registerEvents(this, ((BukkitPartiesBootstrap) plugin.getBootstrap()));
            
            plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
        }
    }
    
    @EventHandler
    public void onIslandCreate(PreIslandCreateEvent event) {
        Player player = event.getPlayer().asPlayer();
        if (player == null) return;

        PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(player.getUniqueId());
        
        if (partyPlayer == null || !partyPlayer.isInParty()) {
            event.setCancelled(true);
            player.sendMessage("§cYou must be in a guild to create an island.");
            return;
        }

        Party party = Parties.getApi().getParty(partyPlayer.getPartyId());
        if (party == null || !player.getUniqueId().equals(party.getLeader())) {
            event.setCancelled(true);
            player.sendMessage("§cOnly the guild leader can create an island.");
        }
    }
}
