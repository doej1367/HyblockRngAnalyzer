package me.hyblockrnganalyzer.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import me.hyblockrnganalyzer.util.ArmorStandItemDrop;
import me.hyblockrnganalyzer.util.HypixelEntityExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.Event;

public class NucleusLootEvent extends Event {

	public TreeMap<String, Integer> getArmorStandContentsSummary() {
		return HypixelEntityExtractor.extractDrops();
	}
}
