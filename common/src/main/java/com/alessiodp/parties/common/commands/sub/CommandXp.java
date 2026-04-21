package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandXp extends PartiesSubCommand {

	public CommandXp(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.XP,
				PartiesPermission.USER_XP,
				ConfigMain.COMMANDS_SUB_XP,
				false
		);

		syntax = String.format("%s <%s/%s>",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_ON,
				ConfigMain.COMMANDS_MISC_OFF
		);

		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_XP;
		help = Messages.HELP_ADDITIONAL_COMMANDS_XP;
	}

	@Override
	public boolean preRequisites(@NotNull CommandData commandData) {
		return handlePreRequisitesFullWithParty(commandData, true, 2, 2, null);
	}

	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();

		Boolean enabled = plugin.getCommandManager().getCommandUtils().handleOnOffCommand(
				partyPlayer.isXpContributionEnabled(),
				commandData.getArgs()
		);
		if (enabled == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return;
		}

		partyPlayer.setXpContributionEnabled(enabled);
		if (enabled) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_EXP_SHARE_TOGGLE_ON);
		} else {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_EXP_SHARE_TOGGLE_OFF);
		}
	}

	@Override
	public List<String> onTabComplete(@NotNull User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompleteOnOff(args);
	}
}

