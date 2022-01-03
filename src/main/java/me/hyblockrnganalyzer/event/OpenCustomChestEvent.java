package me.hyblockrnganalyzer.event;

import java.util.ArrayList;
import java.util.TreeMap;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.Event;
import scala.collection.immutable.List;

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
			int count = item.stackSize;
			if (key.contains("Mithril Powder") || key.contains("Gemstone Powder")) {
				String tmp = key.contains("Mithril Powder") ? "Mithril Powder" : "Gemstone Powder";
				count = Integer.parseInt(key.replaceAll(tmp, "").trim());
				key = tmp;
			}
			contents.put(key, contents.getOrDefault(key, 0) + count);
		}
		return contents;
	}

}
