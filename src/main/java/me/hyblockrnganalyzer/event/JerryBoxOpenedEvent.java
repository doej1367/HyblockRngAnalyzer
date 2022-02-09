package me.hyblockrnganalyzer.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class JerryBoxOpenedEvent extends Event {
	private String name;
	private int count = 1;

	public JerryBoxOpenedEvent(String text) {
		String lootText = text.replaceAll(".* claimed ", "").replaceAll(".* found ", "")
				.replaceAll(" from the Jerry Box!", "").replaceAll(" in a Jerry Box!", "");
		if (lootText.matches("[0-9,]+ [a-zA-Z3\\- ]+"))
			count = Integer.parseInt(lootText.replaceAll(" [a-zA-Z]+", "").replaceAll(",", ""));
		name = lootText.replaceAll("[0-9,]+ ", "");
	}

	public String getItemName() {
		return name;
	}

	public int getItemCount() {
		return count;
	}

}
