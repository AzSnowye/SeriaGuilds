package com.alessiodp.parties.bukkit.listeners;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class BukkitExpListener implements Listener {
	private final BukkitPartiesPlugin plugin;
	private final Set<UUID> expShareGuard = ConcurrentHashMap.newKeySet();

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityDie(EntityDeathEvent event) {
		if (BukkitConfigMain.ADDITIONAL_EXP_ENABLE && BukkitConfigMain.ADDITIONAL_EXP_EARN_FROM_MOBS) {
			Entity killedEntity = event.getEntity();
			
			if (event.getEntity().getKiller() != null) {
				PartyPlayerImpl killer = plugin.getPlayerManager().getPlayer(event.getEntity().getKiller().getUniqueId());
				if (killer.isInParty()) {
					PartyImpl party = plugin.getPartyManager().getParty(killer.getPartyId());
					if (party != null) {
						party.giveExperience(event.getDroppedExp(), killer, killedEntity, true);
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerExpChange(PlayerExpChangeEvent event) {
		if (!BukkitConfigMain.ADDITIONAL_GUILD_XP_SHARING_ENABLE) {
			return;
		}

		Player contributor = event.getPlayer();
		if (expShareGuard.remove(contributor.getUniqueId())) {
			return;
		}

		int originalAmount = event.getAmount();
		if (originalAmount <= 0) {
			return;
		}

		PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(contributor.getUniqueId());
		if (partyPlayer == null || !partyPlayer.isInParty() || !partyPlayer.isXpContributionEnabled()) {
			return;
		}

		PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyId());
		if (party == null) {
			return;
		}

		double shareRatio = Math.max(0.0D, BukkitConfigMain.ADDITIONAL_GUILD_XP_SHARING_PERCENT / 100.0D);
		if (shareRatio <= 0.0D) {
			return;
		}

		int totalShared = BukkitConfigMain.ADDITIONAL_GUILD_XP_SHARING_ROUND_SHARED_XP
				? (int) Math.round(originalAmount * shareRatio)
				: (int) Math.floor(originalAmount * shareRatio);
		totalShared = Math.min(totalShared, originalAmount);

		if (totalShared <= 0) {
			return;
		}

		Set<Player> recipients = getEligibleRecipients(contributor, party);
		if (recipients.isEmpty() && !BukkitConfigMain.ADDITIONAL_GUILD_XP_SHARING_DEDUCT_WITHOUT_NEARBY) {
			return;
		}

		event.setAmount(Math.max(0, originalAmount - totalShared));
		if (recipients.isEmpty()) {
			return;
		}

		int sharePerMember = totalShared / recipients.size();
		int remainder = totalShared % recipients.size();
		for (Player receiver : recipients) {
			int receiverShare = sharePerMember + (remainder > 0 ? 1 : 0);
			if (remainder > 0) {
				remainder--;
			}
			if (receiverShare <= 0) {
				continue;
			}

			expShareGuard.add(receiver.getUniqueId());
			receiver.giveExp(receiverShare);
		}
	}

	private Set<Player> getEligibleRecipients(Player contributor, PartyImpl party) {
		Set<Player> recipients = new HashSet<>();
		for (PartyPlayer onlineMember : party.getOnlineMembers(true)) {
			if (!(onlineMember instanceof PartyPlayerImpl)) {
				continue;
			}

			PartyPlayerImpl recipientData = (PartyPlayerImpl) onlineMember;
			if (!recipientData.isXpContributionEnabled() || recipientData.getPlayerUUID().equals(contributor.getUniqueId())) {
				continue;
			}

			Player recipient = Bukkit.getPlayer(recipientData.getPlayerUUID());
			if (recipient == null || !recipient.isOnline() || !isNearby(contributor, recipient)) {
				continue;
			}

			recipients.add(recipient);
		}
		return recipients;
	}

	private boolean isNearby(Player contributor, Player recipient) {
		if (!BukkitConfigMain.ADDITIONAL_GUILD_XP_SHARING_REQUIRE_NEARBY) {
			return true;
		}
		if (!contributor.getWorld().equals(recipient.getWorld())) {
			return false;
		}

		double maxDistance = BukkitConfigMain.ADDITIONAL_GUILD_XP_SHARING_RADIUS;
		double maxDistanceSquared = maxDistance * maxDistance;
		return contributor.getLocation().distanceSquared(recipient.getLocation()) <= maxDistanceSquared;
	}
}
