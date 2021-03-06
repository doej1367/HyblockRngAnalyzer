package me.hyblockrnganalyzer.eventhandler;

import java.util.Map.Entry;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Note: Loot display was moved from chest GUI to chat
 *
 */
@Deprecated
public class TreasureChestEventHandler {
	private String fileName0 = "databaseTreasureChest.txt";
	private String fileName1 = "databaseLootChest.txt";
	private Main main;

	public TreasureChestEventHandler(Main main) {
		this.main = main;
		main.getTxtDatabase().addFileName(fileName0);
		main.getTxtDatabase().addFileName(fileName1);
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
			main.getTxtDatabase()
					.addDataset("_lobbyAge:" + Minecraft.getMinecraft().theWorld.getTotalWorldTime() + ",_x:"
							+ Minecraft.getMinecraft().thePlayer.getPosition().getX() + ",_y:"
							+ Minecraft.getMinecraft().thePlayer.getPosition().getY() + ",_z:"
							+ Minecraft.getMinecraft().thePlayer.getPosition().getZ() + "," + sb.toString() + "\n",
							fileName1);
		}
	}
}
