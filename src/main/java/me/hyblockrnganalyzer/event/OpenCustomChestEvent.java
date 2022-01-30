package me.hyblockrnganalyzer.event;

import java.util.ArrayList;
import java.util.TreeMap;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OpenCustomChestEvent extends Event {

	private IInventory chestInventory;

	public OpenCustomChestEvent(IInventory chestInventory) {
		this.chestInventory = chestInventory;
	}

	public IChatComponent getChestTitle() {
		return chestInventory.getDisplayName();
	}

	public TreeMap<String, Integer> getChestContentsSummary() {
		ArrayList<ItemStack> items = new ArrayList();
		for (int i = 0; i < chestInventory.getSizeInventory(); i++)
			items.add(chestInventory.getStackInSlot(i));
		TreeMap<String, Integer> contents = new TreeMap();
		for (ItemStack item : items) {
			if (item == null)
				continue;
			String key = item.getDisplayName().replaceAll("\\u00a7.", "");
			if (key.length() < 2 || key.contains("Close") || key.contains("Reroll Chest"))
				continue;
			int count = item.stackSize;
			// TODO adapt for Experiments (if contains 'Enchanting Exp' -> c=0.5)
			if (key.contains("Mithril Powder") || key.contains("Gemstone Powder")) {
				String tmp = key.contains("Mithril Powder") ? "Mithril Powder" : "Gemstone Powder";
				count = Integer.parseInt(key.replaceAll(tmp, "").trim());
				key = tmp;
			} else if (key.contains("Enchanted Book")) { // TODO adapt for Experiments (if 'Item Reward' -> [2], c=0.5)
				key = item.getTagCompound().getCompoundTag("display").getTagList("Lore", 8).get(0).toString()
						.replaceAll("\\u00a7.", "").replaceAll("\"", "");
			} else if (key.contains("Open Reward Chest")) {
				key = "_coins";
				String tmp = item.getTagCompound().getCompoundTag("display").getTagList("Lore", 8).get(6).toString()
						.replaceAll("\\u00a7.", "").replaceAll("\"", "");
				if (tmp.equalsIgnoreCase("FREE"))
					count = 0;
				else
					count = Integer.parseInt(tmp.split(" ")[0].replaceAll(",", ""));
			} else if (key.contains(" x") && key.split(" x")[1].matches("[0-9]+")) {
				String tmp = key.split(" x")[0];
				count = Integer.parseInt(key.split(" x")[1].trim());
				key = tmp;
			}
			contents.put(key, contents.getOrDefault(key, 0) + count);
		}
		return contents;
	}

}
