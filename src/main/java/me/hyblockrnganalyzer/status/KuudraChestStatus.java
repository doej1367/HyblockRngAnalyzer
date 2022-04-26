
package me.hyblockrnganalyzer.status;

public class KuudraChestStatus {
	private int tier;
	private int percentage;
	private int tokens;
	private int damage;
	private int kuudraChestLastOpened = -1;
	private int[] kuudraChestStates = new int[2];
	private long timestampKuudraLoot;
	private long timestampLastReroll;

	public int getTier() {
		return tier;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;

	}

	public int getTokens() {
		return tokens;
	}

	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(String damage) {
		this.damage = parseShorenedNumber(damage);
	}

	private int parseShorenedNumber(String shortenedNumber) {
		int multiplier = (shortenedNumber.contains("k") || shortenedNumber.contains("K")) ? 1000
				: (shortenedNumber.contains("m") || shortenedNumber.contains("M")) ? 1000000
						: (shortenedNumber.contains("b") || shortenedNumber.contains("B")) ? 1000000000 : 1;
		double number = Double.parseDouble(shortenedNumber.replaceAll("[^0-9.]", ""));
		return (int) (number * multiplier);
	}

	public boolean isStatusSaved(int chestType) {
		return kuudraChestStates[chestType] == 1;
	}

	public void setStatusSaved(int chestType) {
		kuudraChestStates[chestType] = 1;
	}

	public void resetKuudraChestStatus() {
		timestampKuudraLoot = System.currentTimeMillis();
		kuudraChestLastOpened = -1;
		kuudraChestStates = new int[2];
		tier = 0;
		percentage = 0;
		tokens = 0;
		damage = 0;
	}

	public void setHotKuudraTier() {
		tier = 1;
	}

	public void setKuudraChestLastOpened(int kuudraChestLastOpened) {
		this.kuudraChestLastOpened = kuudraChestLastOpened;
	}

}
