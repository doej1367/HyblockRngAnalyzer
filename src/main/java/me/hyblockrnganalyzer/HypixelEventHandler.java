package me.hyblockrnganalyzer;

import me.hyblockrnganalyzer.event.GiftOpenedEvent;
import me.hyblockrnganalyzer.event.JerryBoxOpenedEvent;
import me.hyblockrnganalyzer.event.NucleusLootEvent;
import me.hyblockrnganalyzer.event.OpenCustomChestEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HypixelEventHandler {
	private Main main;

	public HypixelEventHandler(Main main) {
		this.main = main;
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
		if (plainText.trim().matches("Sending to server (mini|mega)[0-9]+[A-Z]+\\.\\.\\."))
			main.getLobbyStatus().setServer(plainText.trim().replaceAll("\\.", "").split(" ")[3]);
		else if (plainText.trim().matches("Something went wrong trying to send you to that server!"
				+ " If this keeps happening please report it! \\((mini|mega)[0-9]+[A-Z]\\)"))
			main.getLobbyStatus().setServerChangeFailed(plainText.trim().replaceAll("\\)", "").split("\\(")[1]);
		if (plainText.substring(1).startsWith(" ") && ((plainText.contains(" found ") && plainText
				.contains(Minecraft.getMinecraft().thePlayer.getDisplayNameString().replaceAll("\\u00a7.", "")))
				|| plainText.contains(" claimed ")) && plainText.endsWith(" Jerry Box!"))
			MinecraftForge.EVENT_BUS.post(new JerryBoxOpenedEvent(plainText.trim()));
		else if (plainText.startsWith("The Catacombs - Floor"))
			main.getDungeonChestStatus().resetDungeonChestStatus(plainText.split("Floor ")[1], false);
		else if (plainText.startsWith("Master Mode Catacombs - Floor "))
			main.getDungeonChestStatus().resetDungeonChestStatus(plainText.split("Floor ")[1], true);
		else if (plainText.trim().matches("Team Score: [0-9]+ [\\(][SABCD][+]?[\\)].*"))
			main.getDungeonChestStatus().setScore(Integer.parseInt(plainText.trim().split(" ")[2]));
		else if (plainText.startsWith("[NPC] Elle: Okay adventurers, I will go and fish up Kuudra!"))
			main.getKuudraChestStatus().resetKuudraChestStatus();
		else if (plainText.startsWith("[BOSS] Kuudra"))
			main.getKuudraChestStatus().setHotKuudraTier();
		else if (plainText.trim().startsWith("Percentage Complete: "))
			main.getKuudraChestStatus().setPercentage(Integer.parseInt(plainText.trim().split(": ")[1].split("%")[0]));
		else if (plainText.trim().startsWith("Tokens Earned: "))
			main.getKuudraChestStatus().setTokens(Integer.parseInt(plainText.trim().split(": ")[1]));
		else if (plainText.trim().startsWith("Damage To Kuudra: "))
			main.getKuudraChestStatus().setDamage(plainText.trim().split(": ")[1]);
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
	public void onSoundPlayed(PlaySoundEvent event) {
		if (event.name.equalsIgnoreCase("mob.zombie.unfect"))
			main.getDungeonChestStatus().setRerolled();
		if ((event.name.equalsIgnoreCase("random.successful_hit") && event.sound.getPitch() > 0.8f)
				|| (event.name.equalsIgnoreCase("random.explode") && event.sound.getPitch() > 1.0f))
			MinecraftForge.EVENT_BUS.post(new GiftOpenedEvent(event));
	}

}
