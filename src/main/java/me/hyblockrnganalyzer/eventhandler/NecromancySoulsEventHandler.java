package me.hyblockrnganalyzer.eventhandler;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NecromancySoulsEventHandler {
	private String fileName = "databaseNecromancySouls.txt";
	private Main main;

	public NecromancySoulsEventHandler(Main main) {
		this.main = main;
		main.getTxtDatabase().addFileName(fileName);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onOpenCustomChest(OpenCustomChestEvent event) {
		String chestName = event.getChestTitle().getUnformattedText().replaceAll("\\u00a7.", "");
		if (!chestName.contains("Soul Menu"))
			return;
		for (String e : event.getNecromancySouls())
			if (!main.getTxtDatabase().lookupDataset(e.toString(), fileName))
				main.getTxtDatabase().addDataset(e.toString() + "\n", fileName);
	}
}
