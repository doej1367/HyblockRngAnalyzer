package me.hyblockrnganalyzer;

import me.hyblockrnganalyzer.event.JerryBoxOpenedEvent;
import me.hyblockrnganalyzer.event.NucleusLootEvent;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.event.ClickEvent;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HypixelEventHandler {
	private Main main;
	private String jerryBoxType;

	public HypixelEventHandler(Main main) {
		this.main = main;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void onInteract(MouseEvent e) {
		if (e.button < 0 || e.button > 1 || e.buttonstate)
			return;
		ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
		// JerryBoxOpenEvent
		if (heldItem != null && heldItem.getDisplayName().replaceAll("\\u00a7.", "").endsWith(" Jerry Box"))
			jerryBoxType = heldItem.getDisplayName().replaceAll("\\u00a7.", "").split(" ")[0];
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onChatMessageReceived(ClientChatReceivedEvent event) {
		// 0 = chat, 2 = overHotbar
		byte type = event.type;
		if (type == 2)
			return;
		String text = event.message.getFormattedText();
		String plainText = text.replaceAll("\\u00a7.", "").trim();
		if (plainText.length() < 3)
			return;
		if (plainText.substring(1).startsWith(" ") && ((plainText.contains(" found ") && plainText
				.contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString().replaceAll("\\u00a7.", "")))
				|| plainText.contains(" claimed ")) && plainText.endsWith(" Jerry Box!"))
			MinecraftForge.EVENT_BUS.post(new JerryBoxOpenedEvent(jerryBoxType, plainText.trim()));
		else if (plainText.startsWith("The Catacombs - Floor"))
			main.resetDungeonChestStatus(plainText.split("Floor ")[1], false);
		else if (plainText.startsWith("Master Mode Catacombs - Floor "))
			main.resetDungeonChestStatus(plainText.split("Floor ")[1], true);
		else if (plainText.startsWith("You've earned a Crystal Loot Bundle!"))
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(400);
						MinecraftForge.EVENT_BUS.post(new NucleusLootEvent());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
	}

	@SideOnly(Side.CLIENT)
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

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiClick(final GuiScreenEvent.MouseInputEvent.Pre event) {
		if (event.gui instanceof GuiChest) {
			Slot slot = ((GuiChest) event.gui).getSlotUnderMouse();
			if (slot != null) {
				ItemStack item = slot.getStack();
				if (item != null)
					main.setRecentChestInventoryItem(item.getDisplayName().replaceAll("\\u00a7.", ""));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiClick(PlaySoundEvent event) {
		if (event.name.equalsIgnoreCase("mob.zombie.unfect"))
			main.setRerolled();
	}

}
