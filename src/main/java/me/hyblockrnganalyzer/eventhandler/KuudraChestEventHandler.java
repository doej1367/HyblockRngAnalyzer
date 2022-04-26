package me.hyblockrnganalyzer.eventhandler;

import java.util.Map.Entry;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KuudraChestEventHandler {
	private String fileName = "databaseKuudra.txt";
	private Main main;

	public KuudraChestEventHandler(Main main) {
		this.main = main;
		main.getTxtDatabase().addFileName(fileName);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onOpenCustomChest(OpenCustomChestEvent event) {
		String chestName = event.getChestTitle().getUnformattedText().replaceAll("\\u00a7.", "");
		String[] chestTypes = { "Free", "Paid" };
		int chestType = -1;
		for (int i = 0; i < chestTypes.length; i++)
			chestType = chestName.contains(chestTypes[i] + " Chest") ? i : chestType;
		if (chestType < 0)
			return;
		main.getKuudraChestStatus().setKuudraChestLastOpened(chestType);
		if (main.getKuudraChestStatus().isStatusSaved(chestType))
			return;
		StringBuilder sb = new StringBuilder();
		int tierType = main.getKuudraChestStatus().getTier();
		int percentage = main.getKuudraChestStatus().getPercentage();
		int tokens = main.getKuudraChestStatus().getTokens();
		int damage = main.getKuudraChestStatus().getDamage();
		sb.append("_chestType:" + chestType + ",_tierType:" + tierType + ",_percentage:" + percentage + ",_tokens:"
				+ tokens + ",_damage:" + damage + ",");
		for (Entry e : event.getChestContentsSummary().entrySet())
			sb.append(e.getKey() + ":" + e.getValue() + ",");
		main.getKuudraChestStatus().setStatusSaved(chestType);
		main.getTxtDatabase().addDataset(sb.toString() + "\n", fileName);
	}
}
