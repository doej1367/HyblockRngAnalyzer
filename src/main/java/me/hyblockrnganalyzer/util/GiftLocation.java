package me.hyblockrnganalyzer.util;

import net.minecraft.util.Vec3;

public class GiftLocation {
	private Vec3 pos;
	private int giftType;
	private long timestamp;
	private String giftReward;

	public GiftLocation(Vec3 v, int giftType) {
		this.timestamp = System.currentTimeMillis();
		this.pos = v;
		this.giftType = giftType;
	}

	public GiftLocation(Vec3 v, String giftReward) {
		this.timestamp = System.currentTimeMillis();
		this.pos = v;
		this.giftReward = giftReward;
	}

	public Vec3 getPos() {
		return pos;
	}

	public int getGiftType() {
		return giftType;
	}

	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}

	public String getGiftReward() {
		return giftReward;
	}

	public void setGiftReward(String giftReward) {
		this.giftReward = giftReward;

	}

	public boolean isOld() {
		return System.currentTimeMillis() - timestamp > 1000 * 2;
	}

}
