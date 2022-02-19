package me.hyblockrnganalyzer.eventhandler;

import java.util.Map.Entry;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import me.hyblockrnganalyzer.util.DiscordWebhook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DungeonChestEventHandler {
	private String fileName = "databaseDungeons.txt";
	private DiscordWebhook hook = new DiscordWebhook("944613109585301585");
	private byte[] token = new byte[] { 105, 119, 109, 45, 77, 89, 89, 120, 83, 70, 98, 104, 104, 118, 95, 112, 48, 117,
			76, 99, 86, 107, 48, 48, 83, 89, 79, 111, 81, 88, 54, 84, 100, 76, 67, 111, 87, 106, 45, 102, 45, 81, 48,
			76, 49, 103, 87, 66, 84, 116, 118, 109, 76, 122, 55, 56, 79, 113, 120, 73, 90, 67, 74, 52, 80, 89, 99, 49 };
	private Main main;

	public DungeonChestEventHandler(Main main) {
		this.main = main;
		hook.setToken(token);
		main.getTxtDatabase().addFileNameWithWebhook(fileName, hook);
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
