package me.hyblockrnganalyzer.eventhandler;

import java.util.Map.Entry;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import me.hyblockrnganalyzer.util.DiscordWebhook;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TreasureChestEventHandler {
	private String fileName0 = "databaseTreasureChest.txt";
	private DiscordWebhook hook0 = new DiscordWebhook("944622071579803648");
	private byte[] token0 = new byte[] { 80, 86, 74, 112, 83, 54, 89, 77, 52, 68, 85, 82, 81, 50, 54, 73, 77, 88, 50,
			110, 110, 81, 97, 70, 99, 54, 98, 103, 57, 50, 109, 109, 98, 117, 82, 52, 117, 78, 122, 118, 45, 66, 70, 78,
			90, 68, 100, 102, 122, 55, 65, 107, 105, 103, 52, 119, 86, 69, 107, 102, 48, 79, 79, 112, 109, 52, 76,
			119 };
	private String fileName1 = "databaseLootChest.txt";
	private DiscordWebhook hook1 = new DiscordWebhook("944622196570075256");
	private byte[] token1 = new byte[] { 114, 65, 106, 113, 90, 71, 98, 115, 56, 90, 121, 68, 105, 113, 107, 116, 70,
			104, 81, 48, 101, 86, 55, 56, 48, 75, 103, 49, 83, 105, 54, 56, 110, 107, 79, 87, 113, 69, 68, 108, 53, 52,
			55, 48, 77, 90, 69, 72, 65, 110, 66, 101, 101, 70, 65, 45, 53, 119, 105, 87, 100, 79, 77, 99, 100, 77, 48,
			45 };
	private Main main;

	public TreasureChestEventHandler(Main main) {
		this.main = main;
		hook0.setToken(token0);
		hook1.setToken(token1);
		main.getTxtDatabase().addFileNameWithWebhook(fileName0, hook0);
		main.getTxtDatabase().addFileNameWithWebhook(fileName1, hook1);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onOpenCustomChest(OpenCustomChestEvent event) {
		String chestName = event.getChestTitle().getUnformattedText().replaceAll("\\u00a7.", "");
		if (!chestName.contains("Treasure Chest") && !chestName.contains("Loot Chest"))
			return;
		StringBuilder sb = new StringBuilder();
		for (Entry e : event.getChestContentsSummary().entrySet())
			sb.append(e.getKey() + ":" + e.getValue() + ",");
		if (chestName.contains("Treasure Chest"))
			main.getTxtDatabase().addDataset(sb.toString() + "\n", fileName0);
		else if (chestName.contains("Loot Chest")) {
			main.getTxtDatabase().addDataset("_lobbyAge:" + Minecraft.getMinecraft().theWorld.getTotalWorldTime()
					+ ",_x:" + (int) Minecraft.getMinecraft().thePlayer.getPosition().getX() + ",_y:"
					+ (int) Minecraft.getMinecraft().thePlayer.getPosition().getY() + ",_z:"
					+ (int) Minecraft.getMinecraft().thePlayer.getPosition().getZ() + "," + sb.toString() + "\n",
					fileName1);
		}
	}
}
