package me.hyblockrnganalyzer.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class HypixelEntityExtractor {
	private static String[] nucleusWhitelistExact = { "Oil Barrel", "Bob-omb", "Pickonimbus 2000", "Treasurite",
			"Prehistoric Egg", "Helix", "Divan Fragment", "Recall Potion", "Jaderald", "Divan's Alloy",
			"Enchanted Book", "Quick Claw", "Gemstone Mixture", "800 HotM Experience", "Wishing Compass" };
	private static String[] nucleusWhitelistEndsWith = { " Gemstone", " Crystal" };

	public static ArrayList<StackedArmorStand> extractAllStackedArmorStands() {
		return extractStackedArmorStands(null, 0, false);
	}

	public static TreeMap<String, Integer> extractNucleusDrops() {
		// step 3: analyze and summarize drops
		TreeMap<String, Integer> contents = new TreeMap();
		for (StackedArmorStand drop : extractStackedArmorStands(new Vec3(513, 105, 550), 8.0, false)) {
			String key = drop.getName().replaceAll("\\u00a7.", "");
			int count = key.trim().isEmpty() ? (drop.getInv().size() > 0 ? drop.getInv().get(0).stackSize : 0)
					: (key.contains(" x") ? Integer.parseInt(key.split(" x")[1]) : 1);
			key = key.trim().isEmpty()
					? (drop.getInv().size() > 0 ? drop.getInv().get(0).getDisplayName().replaceAll("\\u00a7.", "") : "")
					: (key.contains(" x") ? key.split(" x")[0] : key);
			if (isWhitelisted(key))
				contents.put(key,
						contents.getOrDefault(key, 0) + (count * (key.equalsIgnoreCase("Treasurite") ? 5 : 1)));
		}
		return contents;
	}

	public static ArrayList<StackedArmorStand> extractStackedArmorStands(Vec3 position, double d,
			boolean horizontalDistance) {
		// INFO EntityTypes: EntityWither, EntityPlayerSP, EntityOtherPlayerMP,
		// EntityItem, EntityArmorStand
		try {
			// step 1: filter armor stands at nucleus loot position
			List<Entity> entities = Minecraft.getMinecraft().theWorld.getLoadedEntityList();

			ArrayList<StackedArmorStand> drops = new ArrayList<StackedArmorStand>();
			for (Entity e : entities) {
				if (e instanceof EntityArmorStand) {
					Vec3 v = e.getPositionVector();
					if (position != null && (horizontalDistance ? Vec2.distanceBetween(position, v) > d
							: (position.distanceTo(v) > d)))
						continue;
					String displayName = ((EntityArmorStand) e).getDisplayName().getUnformattedText();
					if (displayName.equalsIgnoreCase("Armor Stand"))
						displayName = "";
					ArrayList<ItemStack> inventoryContents = new ArrayList<ItemStack>();
					ItemStack[] inv = ((EntityArmorStand) e).getInventory();
					for (ItemStack i : inv)
						if (i != null)
							inventoryContents.add(i);
					drops.add(new StackedArmorStand(displayName, v, inventoryContents));
				}
			}
			Collections.sort(drops);
			// step 2: group together multiple armor stands belonging to the same drop
			ArrayList<StackedArmorStand> mergedDrops = new ArrayList<StackedArmorStand>();
			StackedArmorStand d0 = null, d1, created = null;
			for (int i = 0; i < drops.size() - 1; i++) {
				d0 = (created == null) ? drops.get(i) : created;
				created = null;
				d1 = drops.get(i + 1);
				if (d0.compareTo(d1) == 0) {
					ArrayList tmp = new ArrayList(d0.getInv());
					tmp.addAll(d1.getInv());
					created = new StackedArmorStand((d0.getName() + " " + d1.getName()).trim(), d0.getPos(), tmp);
				} else {
					mergedDrops.add(d0);
				}
			}
			if (drops.size() > 0)
				mergedDrops.add((created == null) ? drops.get(drops.size() - 1) : created);
			return mergedDrops;
		} catch (ConcurrentModificationException e) {
			return new ArrayList<StackedArmorStand>();
		}
	}

	private static boolean isWhitelisted(String key) {
		for (String w : nucleusWhitelistExact)
			if (key.equalsIgnoreCase(w))
				return true;
		for (String w : nucleusWhitelistEndsWith)
			if (key.endsWith(w))
				return true;
		return false;
	}
}
