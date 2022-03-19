package me.hyblockrnganalyzer.event;

import java.util.ArrayList;
import java.util.TreeMap;

import me.hyblockrnganalyzer.util.HypixelEntityExtractor;
import me.hyblockrnganalyzer.wrapper.StackedEntity;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.Event;

public class NucleusLootEvent extends Event {
	private static String[] nucleusWhitelistExact = { "Oil Barrel", "Bob-omb", "Pickonimbus 2000", "Treasurite",
			"Prehistoric Egg", "Helix", "Divan Fragment", "Recall Potion", "Jaderald", "Divan's Alloy",
			"Enchanted Book", "Quick Claw", "Gemstone Mixture", "800 HotM Experience", "Wishing Compass" };
	private static String[] nucleusWhitelistEndsWith = { " Gemstone", " Crystal" };

	public TreeMap<String, Integer> extractNucleusDrops() {
		// analyze and summarize drops
		ArrayList<StackedEntity> stackedArmorStands = HypixelEntityExtractor
				.extractStackedEntities(new Vec3(513, 105, 550), 8.0, false);
		TreeMap<String, Integer> contents = new TreeMap();
		for (StackedEntity drop : stackedArmorStands) {
			String key = drop.getName().replaceAll("\\u00a7.", "");
			int count = key.trim().isEmpty() ? (drop.getInv().size() > 0 ? drop.getInv().get(0).stackSize : 0)
					: (key.contains(" x") ? Integer.parseInt(key.split(" x")[1]) : 1);
			key = (key.trim().isEmpty() || key.trim().equalsIgnoreCase("Armor Stand"))
					? (drop.getInv().size() > 0 ? drop.getInv().get(0).getDisplayName().replaceAll("\\u00a7.", "") : "")
					: (key.contains(" x") ? key.split(" x")[0] : key);
			if (isWhitelisted(key))
				contents.put(key,
						contents.getOrDefault(key, 0) + (count * (key.equalsIgnoreCase("Treasurite") ? 5 : 1)));
		}
		return contents;
	}

	private boolean isWhitelisted(String key) {
		for (String w : nucleusWhitelistExact)
			if (key.equalsIgnoreCase(w))
				return true;
		for (String w : nucleusWhitelistEndsWith)
			if (key.endsWith(w))
				return true;
		return false;
	}
}
