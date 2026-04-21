package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CommandTax extends PartiesSubCommand {
	public CommandTax(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand, CommonCommands.TAX, PartiesPermission.USER_TAX, ConfigMain.COMMANDS_SUB_TAX, false);
		syntax = String.format("%s [%s]", baseSyntax(), ConfigMain.COMMANDS_MISC_PAY);
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_TAX;
		help = Messages.HELP_ADDITIONAL_COMMANDS_TAX;
	}

	@Override
	public boolean preRequisites(@NotNull CommandData commandData) {
		return handlePreRequisitesFullWithParty(commandData, true, 1, 2, null);
	}

	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartiesCommandData partiesCommandData = (PartiesCommandData) commandData;
		PartyPlayerImpl partyPlayer = partiesCommandData.getPartyPlayer();
		PartyImpl party = partiesCommandData.getParty();
		long now = System.currentTimeMillis();

		double amount = getPlugin().getPartyManager().getGuildTaxAmount(party);
		long remainingMillis = getPlugin().getPartyManager().getGuildTaxRemainingMillis(party, now);
		long remainingDays = getRemainingDays(remainingMillis);
		String amountFormatted = formatAmount(amount);
		boolean paidForCurrentCycle = getPlugin().getPartyManager().isGuildTaxPaidForCurrentCycle(party, now);
		UUID currentPayerUuid = getPlugin().getPartyManager().getGuildTaxCurrentPayer(party, now);

		if (commandData.getArgs().length == 1) {
			String payerName = getTaxPayerName(currentPayerUuid);
			String status = paidForCurrentCycle ? getTaxStatusPaidLabel() : getTaxStatusUnpaidLabel();
			sendMessage(sender, partyPlayer, Messages.ADDCMD_TAX_INFO
					.replace("%amount%", amountFormatted)
					.replace("%time%", formatDuration(remainingMillis))
					.replace("%days%", Long.toString(remainingDays))
					.replace("%status%", status)
					.replace("%payer%", payerName));
			return;
		}

		if (!commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_PAY)) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE.replace("%syntax%", syntax));
			return;
		}

		if (!getPlugin().getEconomyManager().isEconomyAvailable()) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_TAX_NO_ECONOMY);
			return;
		}

		if (paidForCurrentCycle) {
			sendMessage(sender, partyPlayer, getTaxAlreadyPaidMessage()
					.replace("%payer%", getTaxPayerName(currentPayerUuid))
					.replace("%time%", formatDuration(remainingMillis))
					.replace("%days%", Long.toString(remainingDays)));
			return;
		}

		if (!getPlugin().getEconomyManager().withdraw(partyPlayer, amount)) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_TAX_FAILED.replace("%amount%", amountFormatted));
			return;
		}

		getPlugin().getPartyManager().markGuildTaxPaid(party, partyPlayer.getPlayerUUID(), now);
		sendMessage(sender, partyPlayer, Messages.ADDCMD_TAX_PAID.replace("%amount%", amountFormatted));
	}

	@Override
	public List<String> onTabComplete(@NotNull User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			ret.add(ConfigMain.COMMANDS_MISC_PAY);
		}
		return ret;
	}

	private String formatAmount(double amount) {
		if (Math.floor(amount) == amount) {
			return Long.toString((long) amount);
		}
		return String.format(Locale.ROOT, "%.2f", amount);
	}

	private String formatDuration(long millis) {
		if (millis <= 0) {
			return "0m";
		}
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
		if (days > 0) {
			return days + "d " + hours + "h";
		}
		if (hours > 0) {
			return hours + "h " + minutes + "m";
		}
		return minutes + "m";
	}

	private long getRemainingDays(long remainingMillis) {
		if (remainingMillis <= 0) {
			return 0;
		}
		return (remainingMillis + TimeUnit.DAYS.toMillis(1) - 1) / TimeUnit.DAYS.toMillis(1);
	}

	private String getTaxPayerName(@Nullable UUID payerUuid) {
		if (payerUuid == null) {
			return Messages.PARTIES_OPTIONS_NONE;
		}

		OfflineUser payer = getPlugin().getOfflinePlayer(payerUuid);
		if (payer != null && payer.getName() != null && !payer.getName().isEmpty()) {
			return payer.getName();
		}
		return Messages.PARTIES_LIST_UNKNOWN;
	}

	private String getTaxStatusPaidLabel() {
		return Messages.ADDCMD_TAX_STATUS_PAID != null ? Messages.ADDCMD_TAX_STATUS_PAID : Messages.PARTIES_OPTIONS_WORD_YES;
	}

	private String getTaxStatusUnpaidLabel() {
		return Messages.ADDCMD_TAX_STATUS_UNPAID != null ? Messages.ADDCMD_TAX_STATUS_UNPAID : Messages.PARTIES_OPTIONS_WORD_NO;
	}

	private String getTaxAlreadyPaidMessage() {
		return Messages.ADDCMD_TAX_ALREADY_PAID != null
				? Messages.ADDCMD_TAX_ALREADY_PAID
				: "&aThis tax cycle is already paid by &e%payer%&a. Next due in &e%time%";
	}
}

