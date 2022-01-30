package me.hyblockrnganalyzer.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class HypixelEntityExtractor {
	public static ArrayList<StackedArmorStand> extractAllStackedArmorStands() {
		return extractStackedArmorStands(null, 0, false);
	}

	public static ArrayList<StackedArmorStand> extractStackedArmorStands(Vec3 position, double d,
			boolean horizontalDistance) {
		// INFO EntityTypes: EntityWither, EntityPlayerSP, EntityOtherPlayerMP,
		// EntityItem, EntityArmorStand
		try {
			// step 1: filter for armor stands at and around given position
			List<Entity> entities = Minecraft.getMinecraft().theWorld.getLoadedEntityList();

			ArrayList<StackedArmorStand> drops = new ArrayList<StackedArmorStand>();
			for (Entity e : entities) {
				if (e instanceof EntityArmorStand) {
					Vec3 v = e.getPositionVector();
					if (position != null && (horizontalDistance ? HorizontalPlane.distanceBetween(position, v) > d
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
}
