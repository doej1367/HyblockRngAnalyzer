package me.hyblockrnganalyzer.eventhandler;

import me.hyblockrnganalyzer.Main;
import me.hyblockrnganalyzer.event.GiftOpenedEvent;
import me.hyblockrnganalyzer.util.DiscordWebhook;
import me.hyblockrnganalyzer.util.GiftLocation;
import me.hyblockrnganalyzer.util.GiftLocationList;
import me.hyblockrnganalyzer.util.HorizontalPlane;
import me.hyblockrnganalyzer.util.HypixelEntityExtractor;
import me.hyblockrnganalyzer.util.StackedArmorStand;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GiftEventHandler {
	private String fileName = "databaseGifts.txt";
	private DiscordWebhook hook = new DiscordWebhook("944620482852970536");
	private byte[] token = new byte[] { 78, 67, 66, 88, 116, 78, 78, 71, 85, 45, 54, 83, 119, 85, 95, 67, 119, 84, 75,
			66, 53, 76, 101, 74, 113, 86, 70, 52, 52, 121, 65, 109, 52, 86, 73, 97, 107, 119, 54, 104, 55, 65, 122, 98,
			77, 86, 65, 84, 45, 101, 87, 83, 51, 86, 50, 52, 72, 56, 70, 105, 75, 89, 83, 110, 56, 83, 111, 111 };
	private Main main;
	private static GiftLocationList giftTypeAtPosition = new GiftLocationList();

	public GiftEventHandler(Main main) {
		this.main = main;
		hook.setToken(token);
		main.getTxtDatabase().addFileNameWithWebhook(fileName, hook);
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
				if (g != null && HorizontalPlane.distanceBetween(g.getPos(), v) < 0.1)
					closestGiftLocation = g;
			final int giftType = closestGiftLocation != null ? closestGiftLocation.getGiftType() : -1;
			final String tmp_loot = closestGiftLocation != null ? closestGiftLocation.getGiftReward() : null;
			final StackedArmorStand tmp = closestArmorStand;
			new Thread() {
				@Override
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
						main.getTxtDatabase()
								.addDataset("_giftType:" + giftType + ",_x:" + (int) closestArmorStand.getPos().xCoord
										+ ",_y:" + closestArmorStand.getPos().yCoord + ",_z:"
										+ (int) closestArmorStand.getPos().zCoord + "," + name + ":" + count + "\n",
										fileName);
					giftTypeAtPosition.cleanup();
				}
			}.start();
		}
	}

	private StackedArmorStand getClosestArmorStand(Vec3 position) {
		for (StackedArmorStand s : HypixelEntityExtractor.extractStackedArmorStands(position, 3.0d, false))
			if (s != null && HorizontalPlane.distanceBetween(s.getPos(), position) < 0.1
					&& (hasGiftReward(s) || hasGiftType(s)))
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
				&& closestArmorStand.getName().replaceAll("\\u00a7.", "").matches("[A-Z ]+! .+!");
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
