package me.hyblockrnganalyzer.command;

import java.awt.Desktop;
import java.io.IOException;

import me.hyblockrnganalyzer.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class OpenFolderCommand extends CommandBase {
	private Main main;

	public OpenFolderCommand(Main main) {
		this.main = main;
	}

	@Override
	public String getCommandName() {
		return "open";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "opens the hyblockrnganalyzer config folder with all the special log files";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		try {
			Desktop.getDesktop().open(main.getTxtDatabase().getFolder());
		} catch (IOException ignored) {
		}
		Minecraft.getMinecraft().thePlayer
				.addChatMessage(new ChatComponentText("RNG Analyzer > Opened folder location!"));
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
