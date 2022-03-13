package me.hyblockrnganalyzer.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class StackedEntity {
	private ArrayList<Entity> entities;

	public StackedEntity(Entity e) {
		this.entities = new ArrayList<>();
		entities.add(e);
	}

	public String getName() {
		if (entities.size() == 1)
			return entities.get(0).getName();
		return entities.stream().map(a -> a.hasCustomName() ? a.getName() : "")
				.reduce("", (a, b) -> a + (a.isEmpty() || b.isEmpty() ? "" : " ") + b).trim();
	}

	public int getStackSize() {
		return entities.size();
	}

	public Vec3 getPos() {
		return entities.get(getStackSize() - 1).getPositionVector();
	}

	public ArrayList<ItemStack> getInv() {
		ArrayList<ItemStack> result = new ArrayList<>();
		entities.stream().filter(a -> a.getInventory() != null).map(a -> a.getInventory()).map(Arrays::asList)
				.forEach(result::addAll);
		result = (ArrayList<ItemStack>) result.stream().filter(a -> a != null).collect(Collectors.toList());
		return result;
	}

	public boolean add(Entity e) {
		if (isCloseTo(e))
			return entities.add(e);
		return false;
	}

	private boolean isCloseTo(Entity entity) {
		for (Entity e : entities)
			if (Math.abs(e.posY - entity.posY) <= 4.0) // TODO fine tune
				return true;
		return false;
	}

	@Override
	public String toString() {
		return getName() + "," + getInv();
	}

}