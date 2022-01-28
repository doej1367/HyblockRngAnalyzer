package me.hyblockrnganalyzer.util;

import net.minecraft.util.Vec3;

public class Vec2 {
	public static double distanceBetween(Vec3 position, Vec3 v) {
		double deltaX = position.xCoord - v.xCoord;
		double deltaZ = position.zCoord - v.zCoord;
		return Math.sqrt(deltaX*deltaX + deltaZ*deltaZ);
	}
}
