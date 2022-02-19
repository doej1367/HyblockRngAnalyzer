package me.hyblockrnganalyzer.eventhandler;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import me.hyblockrnganalyzer.util.DiscordWebhook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NecromancySoulsEventHandler {
	private String fileName = "databaseNecromancySouls.txt";
	private DiscordWebhook hook = new DiscordWebhook("944621772140077056");
	private byte[] token = new byte[] { 85, 114, 50, 102, 99, 115, 110, 98, 121, 82, 103, 57, 81, 107, 90, 99, 45, 102,
			101, 54, 56, 48, 78, 77, 57, 108, 68, 83, 78, 109, 67, 70, 98, 85, 84, 86, 49, 99, 76, 56, 50, 76, 79, 67,
			82, 76, 65, 120, 106, 65, 54, 50, 75, 53, 68, 83, 119, 95, 86, 67, 83, 121, 70, 66, 108, 122, 73, 100 };
	private Main main;

	public NecromancySoulsEventHandler(Main main) {
		this.main = main;
		hook.setToken(token);
		main.getTxtDatabase().addFileNameWithWebhook(fileName, hook);
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
