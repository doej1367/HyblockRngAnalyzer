package me.hyblockrnganalyzer.wrapper;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class StackedEntity {
	private ArrayList<Entity> entities;
	private ArrayList<ItemStack> inv;

	public StackedEntity(Entity e) {
		this.entities = new ArrayList<>();
		entities.add(e);
	}

	public String getName() {
		return entities.stream().map(a -> a.getName()).reduce("", (a, b) -> a + (a.isEmpty() ? "" : " ") + b);
	}

	public int getStackSize() {
		return entities.size();
	}

	public Vec3 getPos() {
		return entities.get(getStackSize() - 1).getPositionVector();
	}

	public ArrayList<ItemStack> getInv() {
		ArrayList<ItemStack> result = new ArrayList<>();
		entities.stream().map(a -> a.getInventory()).map(Arrays::asList).forEach(result::addAll);
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
		// TODO
		return "";
	}

}