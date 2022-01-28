package me.hyblockrnganalyzer.eventhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.GiftOpenedEvent;
import me.hyblockrnganalyzer.event.JerryBoxOpenedEvent;
import me.hyblockrnganalyzer.util.GiftLocation;
import me.hyblockrnganalyzer.util.GiftLocationList;
import me.hyblockrnganalyzer.util.HypixelEntityExtractor;
import me.hyblockrnganalyzer.util.StackedArmorStand;
import me.hyblockrnganalyzer.util.Vec2;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GiftEventHandler {
	private Main main;
	private static GiftLocationList giftTypeAtPosition = new GiftLocationList();

	public GiftEventHandler(Main main) {
		this.main = main;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGiftOpened(GiftOpenedEvent event) {
		final Vec3 v = new Vec3(event.getSoundEvent().sound.getXPosF(), event.getSoundEvent().sound.getYPosF(),
				event.getSoundEvent().sound.getZPosF());
		StackedArmorStand closestArmorStand = getClosestArmorStand(v);
		if (event.getSoundEvent().name.equalsIgnoreCase("random.successful_hit")) {
			if (hasGiftType(closestArmorStand)) {
				giftTypeAtPosition
						.add(new GiftLocation(v, getGiftType(closestArmorStand.getInv().get(0).getDisplayName())));
			}
			if (hasGiftReward(closestArmorStand)) {
				giftTypeAtPosition.add(new GiftLocation(v,
						closestArmorStand.getName().replaceAll("\\u00a7.", "").split("!")[1].trim()));
			}
		} else if (event.getSoundEvent().name.equalsIgnoreCase("random.explode")) {
			GiftLocation closestGiftLocation = null;
			for (GiftLocation g : giftTypeAtPosition)
				if (g != null && Vec2.distanceBetween(g.getPos(), v) < 0.1)
					closestGiftLocation = g;
			final int giftType = closestGiftLocation != null ? closestGiftLocation.getGiftType() : -1;
			final String tmp_loot = closestGiftLocation != null ? closestGiftLocation.getGiftReward() : null;
			final StackedArmorStand tmp = closestArmorStand;
			new Thread() {
				public void run() {
					String loot = tmp_loot != null ? tmp_loot : "";
					StackedArmorStand closestArmorStand = tmp;
					for (int i = 0; i < 100; i++) {
						closestArmorStand = getClosestArmorStand(v);
						if (hasGiftReward(closestArmorStand)) {
							loot = closestArmorStand.getName().replaceAll("\\u00a7.", "").split("!")[1].trim();
							break;
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException ignored) {
						}
					}
					String name;
					int count = 1;
					if (loot.matches("\\+[0-9,]+ .*")) {
						name = loot.split(" ", 2)[1];
						count = Integer.parseInt(loot.split(" ", 2)[0].replaceAll("\\+", "").replaceAll(",", ""));
					} else
						name = loot;
					if (name != null && !name.isEmpty() && giftType >= 0)
						main.addDataset("giftType:" + giftType + "," + name + ":" + count + "\n", 8);
					giftTypeAtPosition.cleanup();
				}
			}.start();
		}
	}

	private StackedArmorStand getClosestArmorStand(Vec3 position) {
		for (StackedArmorStand s : HypixelEntityExtractor.extractStackedArmorStands(position, 3.0d, false))
			if (s != null && Vec2.distanceBetween(s.getPos(), position) < 0.1 && (hasGiftReward(s) || hasGiftType(s)))
				return s;
		return null;
	}

	private boolean hasGiftType(StackedArmorStand closestArmorStand) {
		return closestArmorStand != null && closestArmorStand.getInv().size() > 0
				&& closestArmorStand.getInv().get(0) != null
				&& closestArmorStand.getInv().get(0).getDisplayName().endsWith(" Gift");
	}

	private boolean hasGiftReward(StackedArmorStand closestArmorStand) {
		return closestArmorStand != null
				&& closestArmorStand.getName().replaceAll("\\u00a7.", "").matches("[A-Z]+! .+!");
	}

	private int getGiftType(String giftType) {
		if (giftType.contains("White"))
			return 0;
		if (giftType.contains("Green"))
			return 1;
		if (giftType.contains("Red"))
			return 2;
		return -1;
	}
}
