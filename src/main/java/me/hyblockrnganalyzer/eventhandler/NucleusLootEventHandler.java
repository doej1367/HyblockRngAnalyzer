package me.hyblockrnganalyzer.eventhandler;

import java.util.Map.Entry;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.NucleusLootEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NucleusLootEventHandler {
	private Main main;

	public NucleusLootEventHandler(Main main) {
		this.main = main;
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
								.addChatMessage(new ChatComponentText("Loot recording canceled!"));
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
				main.getTxtDatabase().addDataset(sb.toString() + "\n", 2);
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Loot is recorded!"));
			};
		}.start();
	}
}
