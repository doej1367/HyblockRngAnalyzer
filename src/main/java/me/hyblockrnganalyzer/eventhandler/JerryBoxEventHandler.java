package me.hyblockrnganalyzer.eventhandler;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.JerryBoxOpenedEvent;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class JerryBoxEventHandler {
	private String fileName = "databaseJerryBoxes.txt";
	private Main main;
	private String jerryBoxType;

	public JerryBoxEventHandler(Main main) {
		this.main = main;
		main.getTxtDatabase().addFileName(fileName);
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
