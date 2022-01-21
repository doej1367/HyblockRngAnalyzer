package me.hyblockrnganalyzer.eventhandler;

import java.util.List;
import java.util.Map.Entry;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.NucleusLootEvent;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NucleusLootEventHandler {
	private Main main;

	public NucleusLootEventHandler(Main main) {
		this.main = main;
	}

	@SubscribeEvent
	public void onNucleusLoot(NucleusLootEvent event) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Integer> e : event.getArmorStandContentsSummary().entrySet())
			sb.append(e.getKey() + ":" + e.getValue() + ",");
		main.addDataset(sb.toString() + "\n", 1);
	}
}
