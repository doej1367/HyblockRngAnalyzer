package me.hyblockrnganalyzer.command;

import java.io.File;

import me.hyblockrnganalyzer.util.HypixelEntityExtractor;
import me.hyblockrnganalyzer.util.StackedArmorStand;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class TestCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "test";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "hyblockrnganalyzer test command";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		// TODO (placeholder for testing something)
		for (StackedArmorStand s : HypixelEntityExtractor
				.extractStackedArmorStands(Minecraft.getMinecraft().thePlayer.getPositionVector(), 5, false))
			if (s.getInv().size() > 0)
				System.out.println(s.getInv().get(0).getDisplayName() + " " + s.getInv().get(0).getTagCompound());
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("TestCommand"));
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
