package me.hyblockrnganalyzer.eventhandler;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.GiftOpenedEvent;
import me.hyblockrnganalyzer.util.GiftLocation;
import me.hyblockrnganalyzer.util.GiftLocationList;
import me.hyblockrnganalyzer.util.HorizontalPlane;
import me.hyblockrnganalyzer.util.HypixelEntityExtractor;
import me.hyblockrnganalyzer.wrapper.StackedEntity;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GiftEventHandler {
	private String fileName = "databaseGifts.txt";
	private Main main;
	private static GiftLocationList giftTypeAtPosition = new GiftLocationList();

	public GiftEventHandler(Main main) {
		this.main = main;
		main.getTxtDatabase().addFileName(fileName);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGiftOpened(GiftOpenedEvent event) {
		final Vec3 v = new Vec3(event.getSoundEvent().sound.getXPosF(), event.getSoundEvent().sound.getYPosF(),
				event.getSoundEvent().sound.getZPosF());
		StackedEntity closestArmorStand = getClosestArmorStand(v);
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
				if (g != null && HorizontalPlane.distanceBetween(g.getPos(), v) <= 0.2)
					closestGiftLocation = g;
			final int giftType = closestGiftLocation != null ? closestGiftLocation.getGiftType() : -1;
			final String tmp_loot = closestGiftLocation != null ? closestGiftLocation.getGiftReward() : null;
			final StackedEntity tmp = closestArmorStand;
			new Thread() {
				@Override
				public void run() {
					String loot = tmp_loot != null ? tmp_loot : "";
					StackedEntity closestArmorStand = tmp;
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
						main.getTxtDatabase()
								.addDataset("_giftType:" + giftType + ",_x:" + (int) closestArmorStand.getPos().xCoord
										+ ",_y:" + closestArmorStand.getPos().yCoord + ",_z:"
										+ (int) closestArmorStand.getPos().zCoord + "," + name + ":" + count + ",\n",
										fileName);
					giftTypeAtPosition.cleanup();
				}
			}.start();
		}
	}

	private StackedEntity getClosestArmorStand(Vec3 position) {
		for (StackedEntity s : HypixelEntityExtractor.extractStackedEntities(position, 4.0d, false))
			if (s != null && HorizontalPlane.distanceBetween(s.getPos(), position) <= 0.2
					&& (hasGiftReward(s) || hasGiftType(s)))
				return s;
		return null;
	}

	private boolean hasGiftType(StackedEntity s) {
		return s != null && s.getInv().size() > 0 && s.getInv().get(0) != null
				&& s.getInv().get(0).getDisplayName().endsWith(" Gift");
	}

	private boolean hasGiftReward(StackedEntity s) {
		return s != null && s.getName().replaceAll("\\u00a7.", "").matches("[A-Z ]+! .+!");
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
