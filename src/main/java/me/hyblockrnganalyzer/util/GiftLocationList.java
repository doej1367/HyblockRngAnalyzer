package me.hyblockrnganalyzer.util;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class GiftLocationList extends ArrayList<GiftLocation> {

	@Override
	public boolean add(GiftLocation element) {
		for (GiftLocation g : this)
			if (HorizontalPlane.distanceBetween(g.getPos(), element.getPos()) < 0.1) {
				if (element.getGiftReward() != null)
					g.setGiftReward(element.getGiftReward());
				if (element.getGiftType() >= 0)
					g.setGiftType(element.getGiftType());
				return false;
			}
		return super.add(element);
	}

	public void cleanup() {
		try {
			GiftLocation giftLocation = null;
			for (Iterator iterator = iterator(); iterator.hasNext(); giftLocation = (GiftLocation) iterator.next())
				if (giftLocation != null && giftLocation.isOld())
					iterator.remove();
		} catch (ConcurrentModificationException ignored) {

		}
	}

}
