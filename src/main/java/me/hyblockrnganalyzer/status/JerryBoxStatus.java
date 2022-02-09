package me.hyblockrnganalyzer.status;

public class JerryBoxStatus {
	private String boxType;

	public void setBoxType(String boxType) {
		this.boxType = boxType;
	}

	public String getBoxType() {
		return boxType;
	}

	public int getBoxTypeNumber() {
		if (boxType == null || boxType.length() < 2)
			return -1;
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
		return -1;
	}

}
