package me.hyblockrnganalyzer.eventhandler;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.JerryBoxOpenedEvent;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public class JerryBoxEventHandler {
	private String fileName = "databaseJerryBoxes.txt";
	private Main main;
	private String jerryBoxType;

	public JerryBoxEventHandler(Main main) {
		this.main = main;
		main.getTxtDatabase().addFileName(fileName);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onChatMessageReceived(ClientChatReceivedEvent event) {
		if (event.type == 2) {
			return;
		}

		String plainText = StringUtils.stripControlCodes(event.message.getFormattedText());
		if (plainText.length() < 3) {
			return;
		}
		if (plainText.substring(1).startsWith(" ") && ((plainText.contains(" found ") && plainText
				.contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString().replaceAll("\\u00a7.", "")))
				|| plainText.contains(" claimed ")) && plainText.endsWith(" Jerry Box!"))
			MinecraftForge.EVENT_BUS.post(new JerryBoxOpenedEvent(jerryBoxType, plainText.trim()));
	}

	@SubscribeEvent
	public void onOpenCustomChest(OpenCustomChestEvent event) {
		String chestName = event.getChestTitle().getUnformattedText().replaceAll("\\u00a7.", "");
		if (!chestName.contains("Open a Jerry Box")) {
			return;
		}

		for (String itemName : event.getChestContentsSummary().keySet()) {
			itemName = itemName.replaceAll("\\u00a7.", "");
			if (itemName.endsWith("Jerry Box")) {
				jerryBoxType = itemName.split(" ")[0];
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onJerryBox(JerryBoxOpenedEvent event) {
		main.getTxtDatabase().addDataset(
				"_boxType:" + event.getBoxTypeNumber() + "," + event.getName() + ":" + event.getCount() + "\n",
				fileName);
	}

	public String getJerryBoxType() {
		return jerryBoxType;
	}
}
