package me.hyblockrnganalyzer.event;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.Event;

public class NucleusLootEvent extends Event {

	public TreeMap<String, Integer> getArmorStandContentsSummary() {
		TreeMap<String, Integer> contents = new TreeMap();
		// INFO EntityTypes: EntityWither, EntityPlayerSP, EntityOtherPlayerMP,
		// EntityItem, EntityArmorStand
		List<Entity> entities = Minecraft.getMinecraft().theWorld.getLoadedEntityList();
		System.out.println("Entities: " + entities.size());
		for (Entity e : entities) {
			Vec3 v = e.getPositionVector();
			double x = v.xCoord, y = v.yCoord, z = v.zCoord;
			if (x > 518 || x < 508 || y > 110 || y < 100 || z > 556 || z < 545)
				continue;
			if (e instanceof EntityArmorStand) {
				ItemStack[] inventoryContents = ((EntityArmorStand) e).getInventory();
				for (ItemStack i : inventoryContents) {
					if (i == null)
						continue;
					System.out.println(i);
					String name = i.getDisplayName().replaceAll("\\u00a7.", "");
					int count = i.stackSize;
					// TODO "Head" item -> ArmorStand title "HotM Experience"
					// TODO double check count as it seems to be always 1
					if (name.contains("HotM Experience")) {
						String tmp = "HotM Experience";
						count = Integer.parseInt(name.replaceAll(tmp, "").trim());
						name = tmp;
					}
					contents.put(name, contents.getOrDefault(name, 0) + count);
				}
			}
		}
		return contents;
	}
}
