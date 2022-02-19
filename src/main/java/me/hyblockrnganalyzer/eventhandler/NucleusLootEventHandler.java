package me.hyblockrnganalyzer.eventhandler;

import java.util.Map.Entry;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.NucleusLootEvent;
import me.hyblockrnganalyzer.util.DiscordWebhook;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NucleusLootEventHandler {
	private String fileName = "databaseNucleusLoot.txt";
	private DiscordWebhook hook = new DiscordWebhook("944621932110807130");
	private byte[] token = new byte[] { 117, 73, 83, 45, 78, 95, 101, 65, 48, 86, 75, 72, 87, 90, 72, 90, 82, 65, 101,
			106, 121, 120, 52, 55, 69, 116, 81, 104, 78, 111, 76, 77, 89, 122, 54, 103, 80, 78, 110, 112, 120, 51, 113,
			120, 121, 49, 52, 113, 76, 83, 72, 57, 49, 80, 76, 79, 82, 104, 77, 49, 106, 51, 66, 71, 75, 65, 56, 102 };
	private Main main;

	public NucleusLootEventHandler(Main main) {
		this.main = main;
		hook.setToken(token);
		main.getTxtDatabase().addFileNameWithWebhook(fileName, hook);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onNucleusLoot(final NucleusLootEvent event) {
		// start thread to watch player movement
		new Thread() {
			@Override
			public void run() {
				long timestamp = System.currentTimeMillis();
				Vec3 v = Minecraft.getMinecraft().thePlayer.getPositionVector();
				while (v.xCoord > 522 || v.xCoord < 504 || v.yCoord > 114 || v.yCoord < 96 || v.zCoord > 560
						|| v.zCoord < 540) {
					if ((System.currentTimeMillis() - timestamp) > (1000 * 60 * 3)) {
						// cancel after 3 minutes
						Minecraft.getMinecraft().thePlayer
								.addChatMessage(new ChatComponentText("RNG Analyzer > Loot recording canceled!"));
						return;
					}
					try {
						Thread.sleep(50);
						v = Minecraft.getMinecraft().thePlayer.getPositionVector();
					} catch (InterruptedException ignored) {
					}
				}
				// execute on moving into loot zone
				StringBuilder sb = new StringBuilder();
				for (Entry<String, Integer> e : event.getArmorStandContentsSummary().entrySet())
					sb.append(e.getKey() + ":" + e.getValue() + ",");
				main.getTxtDatabase().addDataset(sb.toString() + "\n", fileName);
				Minecraft.getMinecraft().thePlayer
						.addChatMessage(new ChatComponentText("RNG Analyzer > Loot is recorded!"));
			}
		}.start();
	}
}
