package me.hyblockrnganalyzer.command;

import me.hyblockrnganalyzer.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CsvFileCreationCommand extends CommandBase {
	private Main main;

	public CsvFileCreationCommand(Main main) {
		this.main = main;
	}

	@Override
	public String getCommandName() {
		return "csv";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "creates csv files from all the data collected";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		main.getTxtDatabase().allToCsv();
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Created csv files!"));
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
