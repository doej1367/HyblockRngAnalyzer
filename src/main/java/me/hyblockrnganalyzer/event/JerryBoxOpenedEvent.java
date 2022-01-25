package me.hyblockrnganalyzer.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class JerryBoxOpenedEvent extends Event {
	private String boxType;
	private String name;
	private int count = 1;

	public JerryBoxOpenedEvent(String boxType, String text) {
		this.boxType = boxType;
		String lootText = text.replaceAll(".* claimed ", "").replaceAll(".* found ", "")
				.replaceAll(" from the Jerry Box!", "").replaceAll(" in a Jerry Box!", "");
		if (lootText.matches("[0-9,]+ [a-zA-Z3\\- ]+"))
			count = Integer.parseInt(lootText.replaceAll(" [a-zA-Z]+", "").replaceAll(",", ""));
		name = lootText.replaceAll("[0-9,]+ ", "");
	}

	public String getBoxType() {
		return boxType;
	}

	public int getBoxTypeNumber() {
		switch (boxType.charAt(1)) {
		case 'r':
			return 0;
		case 'l':
			return 1;
		case 'u':
			return 2;
		case 'o':
			return 3;
		}
		return 0;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

}
