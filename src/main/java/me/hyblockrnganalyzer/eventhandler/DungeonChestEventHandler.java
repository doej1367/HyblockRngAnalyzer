package me.hyblockrnganalyzer.eventhandler;

import java.util.Map.Entry;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DungeonChestEventHandler {
	private String fileName = "databaseDungeons.txt";
	private Main main;

	public DungeonChestEventHandler(Main main) {
		this.main = main;
		main.getTxtDatabase().addFileName(fileName);
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
		main.getDungeonChestStatus().setDungeonChestLastOpened(chestType);
		if (main.getDungeonChestStatus().isStatusSaved(chestType))
			return;
		StringBuilder sb = new StringBuilder();
		int floorType = main.getDungeonChestStatus().getFloor();
		int score = main.getDungeonChestStatus().getScore();
		sb.append("_chestType:" + chestType + ",_floorType:" + floorType + ",_isRerolled:"
				+ main.getDungeonChestStatus().isRerolled(chestType) + ",_score:" + score + ",");
		for (Entry e : event.getChestContentsSummary().entrySet())
			sb.append(e.getKey() + ":" + e.getValue() + ",");
		main.getDungeonChestStatus().setStatusSaved(chestType);
		main.getTxtDatabase().addDataset(sb.toString() + "\n", fileName);
	}
}
