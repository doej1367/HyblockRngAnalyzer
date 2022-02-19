package me.hyblockrnganalyzer.eventhandler;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.JerryBoxOpenedEvent;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import me.hyblockrnganalyzer.util.DiscordWebhook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class JerryBoxEventHandler {
	private String fileName = "databaseJerryBoxes.txt";
	private DiscordWebhook hook = new DiscordWebhook("944620852509544489");
	private byte[] token = new byte[] { 54, 78, 67, 90, 49, 89, 81, 55, 75, 95, 67, 118, 49, 55, 116, 104, 70, 90, 119,
			88, 120, 112, 121, 81, 67, 100, 85, 84, 115, 82, 87, 113, 53, 105, 77, 45, 114, 56, 71, 111, 98, 88, 73, 99,
			99, 89, 67, 65, 106, 116, 75, 101, 98, 79, 97, 115, 106, 98, 73, 106, 55, 79, 110, 95, 95, 87, 83, 110 };
	private Main main;
	private String jerryBoxType;

	public JerryBoxEventHandler(Main main) {
		this.main = main;
		hook.setToken(token);
		main.getTxtDatabase().addFileNameWithWebhook(fileName, hook);
	}

	@SubscribeEvent
	public void onOpenCustomChest(OpenCustomChestEvent event) {
		String chestName = event.getChestTitle().getUnformattedText().replaceAll("\\u00a7.", "");
		if (!chestName.contains("Open a Jerry Box"))
			return;
		for (String itemName : event.getChestContentsSummary().keySet()) {
			itemName = itemName.replaceAll("\\u00a7.", "");
			if (itemName.endsWith("Jerry Box")) {
				main.getJerryBoxStatus().setBoxType(itemName.split(" ")[0]);
				break;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onJerryBox(JerryBoxOpenedEvent event) {
		main.getTxtDatabase().addDataset("_boxType:" + main.getJerryBoxStatus().getBoxTypeNumber() + ","
				+ event.getItemName() + ":" + event.getItemCount() + "\n", fileName);
	}

}
