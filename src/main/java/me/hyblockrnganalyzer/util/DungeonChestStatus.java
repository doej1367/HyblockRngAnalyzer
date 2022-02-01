package me.hyblockrnganalyzer.util;

public class DungeonChestStatus {
	private int floor;
	private int score;
	private int dungeonChestLastOpened = -1;
	private int[] dungeonChestStates = new int[6];
	private long timestampDungeonLoot;
	private long timestampLastReroll;

	public int getFloor() {
		return floor;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int isRerolled(int chestType) {
		return dungeonChestStates[chestType] == 2 ? 1 : 0;
	}

	public void setRerolled() {
		long lastReroll = timestampLastReroll;
		timestampLastReroll = System.currentTimeMillis();
		if (timestampLastReroll - lastReroll > 1000 && timestampLastReroll - timestampDungeonLoot < 1000 * 60 * 5
				&& dungeonChestLastOpened >= 0)
			dungeonChestStates[dungeonChestLastOpened] = 2;
	}

	public boolean isStatusSaved(int chestType) {
		return dungeonChestStates[chestType] == 1;
	}

	public void setStatusSaved(int chestType) {
		dungeonChestStates[chestType] = 1;
	}

	public void resetDungeonChestStatus(String romanFloor, boolean isMasterMode) {
		timestampDungeonLoot = System.currentTimeMillis();
		dungeonChestLastOpened = -1;
		dungeonChestStates = new int[6];
		floor = parseRomanNumeral(romanFloor) + (isMasterMode ? 7 : 0);
	}

	public void setDungeonChestLastOpened(int dungeonChestLastOpened) {
		this.dungeonChestLastOpened = dungeonChestLastOpened;
	}

	private int parseRomanNumeral(String romanNumeral) {
		// core idea from https://stackoverflow.com/a/17534350
		if (romanNumeral.length() < 1)
			return 0;
		if (romanNumeral.startsWith("V"))
			return 5 + parseRomanNumeral(romanNumeral.substring(1));
		if (romanNumeral.startsWith("IV"))
			return 4 + parseRomanNumeral(romanNumeral.substring(2));
		if (romanNumeral.startsWith("I"))
			return 1 + parseRomanNumeral(romanNumeral.substring(1));
		return 0;
	}

}
