package me.hyblockrnganalyzer.wrapper;

import java.util.ArrayList;

import net.minecraft.entity.Entity;

public class StackedEntitiesList extends ArrayList<StackedEntities> {

	public boolean add(Entity e) {
		for (StackedEntities se : this)
			if (se.add(e))
				return true;
		return super.add(new StackedEntities(e));
	}

	public ArrayList<StackedEntity> getStackedEntities() {
		ArrayList<StackedEntity> entities = new ArrayList<>();
		for (StackedEntities se : this)
			entities.addAll(se.getStackedEntities());
		return entities;
	}

}
