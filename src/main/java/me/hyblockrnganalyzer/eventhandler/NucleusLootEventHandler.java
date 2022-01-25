package me.hyblockrnganalyzer.eventhandler;

import java.util.List;
import java.util.Map.Entry;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.NucleusLootEvent;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NucleusLootEventHandler {
	private Main main;

	public NucleusLootEventHandler(Main main) {
		this.main = main;
	}

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
				main.addDataset(sb.toString() + "\n", 1);
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Loot is recorded!"));
			};
		}.start();
	}
}
