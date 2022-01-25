package me.hyblockrnganalyzer.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import me.hyblockrnganalyzer.event.NucleusLootEvent;
import me.hyblockrnganalyzer.util.ArmorStandItemDrop;
import me.hyblockrnganalyzer.util.HypixelEntityExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;

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
		// TODO
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("TestCommand"));
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
