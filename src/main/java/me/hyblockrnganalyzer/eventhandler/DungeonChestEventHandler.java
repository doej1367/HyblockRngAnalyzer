package me.hyblockrnganalyzer.eventhandler;

import java.util.Map.Entry;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DungeonChestEventHandler {
	private Main main;

	public DungeonChestEventHandler(Main main) {
		this.main = main;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onOpenCustomChest(OpenCustomChestEvent event) {
		String chestName = event.getChestTitle().getUnformattedText().replaceAll("\\u00a7.", "");
		String[] chestTypes = { "Wood", "Gold", "Diamond", "Emerald", "Obsidian", "Bedrock" };
		int chestType = -1;
		for (int i = 0; i < chestTypes.length; i++)
			chestType = chestName.contains(chestTypes[i] + " Chest") ? i : chestType;
		if (chestType < 0)
			return;
		main.setDungeonChestLastOpened(chestType);
		if (main.isStatusSaved(chestType))
			return;
		StringBuilder sb = new StringBuilder();
		int floorType = main.getFloor();
		sb.append("chestType:" + chestType + ",floorType:" + floorType + ",isRerolled:" + main.isRerolled(chestType)
				+ ",");
		for (Entry e : event.getChestContentsSummary().entrySet())
			sb.append(e.getKey() + ":" + e.getValue() + ",");
		main.setStatusSaved(chestType);
		main.addDataset(sb.toString() + "\n", 7);
	}
}
