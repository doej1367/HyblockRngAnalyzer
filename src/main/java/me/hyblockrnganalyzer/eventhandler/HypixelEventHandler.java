package me.hyblockrnganalyzer.eventhandler;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.NucleusLootEvent;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HypixelEventHandler {
	private Main main;

	public HypixelEventHandler(Main main) {
		this.main = main;
	}

	@SubscribeEvent
	public void onChatMessageReceived(ClientChatReceivedEvent event) {
		// 0 = chat, 2 = overHotbar
		byte type = event.type;
		if (type == 2)
			return;
		String text = event.message.getFormattedText();
		String plainText = text.replaceAll("\\u00a7.", "");
		if (plainText.trim().startsWith("You've earned a Crystal Loot Bundle!"))
			new Thread() {
				@Override
				public void run() {
					try {
						System.out.println("LootBundle");
						Thread.sleep(400);
						MinecraftForge.EVENT_BUS.post(new NucleusLootEvent());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
	}

	@SubscribeEvent
	public void onGuiOpen(final GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.gui != null && event.gui instanceof GuiChest) {
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(50);
						IInventory chestInventory = ((ContainerChest) ((GuiChest) event.gui).inventorySlots)
								.getLowerChestInventory();
						if (chestInventory.hasCustomName())
							MinecraftForge.EVENT_BUS.post(new OpenCustomChestEvent(chestInventory));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

}
