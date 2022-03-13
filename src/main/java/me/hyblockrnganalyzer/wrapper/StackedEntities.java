package me.hyblockrnganalyzer.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;

public class StackedEntities {
	private ArrayList<Entity> entities;

	public StackedEntities(Entity e) {
		this.entities = new ArrayList<>();
		entities.add(e);
	}

	public ArrayList<StackedEntity> getStackedEntities() {
		ArrayList<StackedEntity> result = new ArrayList<>();
		List<Entity> list = entities.stream().sorted((a, b) -> (int) -Math.signum((a.posY - b.posY)))
				.collect(Collectors.toList());
		StackedEntity se = null;
		for (Entity e : list) {
			if (se == null)
				se = new StackedEntity(e);
			else if (!se.add(e)) {
				result.add(se);
				se = new StackedEntity(e);
			}
		}
		if (se != null)
			result.add(se);
		return result;
	}

	public boolean add(Entity e) {
		if (isCloseTo(e))
			return entities.add(e);
		return false;
	}

	private boolean isCloseTo(Entity entity) {
		for (Entity e : entities)
			if (Math.abs(e.posX - entity.posX) + Math.abs(e.posZ - entity.posZ) <= 0.2) // TODO fine tune
				return true;
		return false;
	}

}