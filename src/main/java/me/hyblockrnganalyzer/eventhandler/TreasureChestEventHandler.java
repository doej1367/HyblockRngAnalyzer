package me.hyblockrnganalyzer.eventhandler;

import java.util.Map.Entry;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TreasureChestEventHandler {
	private Main main;

	public TreasureChestEventHandler(Main main) {
		this.main = main;
	}

	@SubscribeEvent
	public void onOpenCustomChest(OpenCustomChestEvent event) {
		String chestName = event.getChestTitle().getUnformattedText().replaceAll("\\u00a7.", "");
		if (!chestName.contains("Treasure Chest") && !chestName.contains("Loot Chest"))
			return;
		StringBuilder sb = new StringBuilder();
		for (Entry e : event.getChestContentsSummary().entrySet())
			sb.append("," + e.getKey() + ":" + e.getValue());
		main.addDataset(chestName + sb.toString() + "\n", 0);
	}
}
