package se.raek.cdp1802.util;

import se.raek.cdp1802.sim.Memory;

public final class Utils {

	private Utils() {
	}

	public static void loadHex(Memory m, String hexString) {
		int hexLength = hexString.length();
		if (hexLength % 2 != 0) {
			throw new RuntimeException("odd number of hex digits");
		}
		int byteLength = hexLength / 2;
		for (int i = 0; i < byteLength; i++) {
			char high = hexString.charAt(2 * i);
			char low = hexString.charAt(2 * i + 1);
			m.write(i, hexByte(high, low));
		}
	}

	public static int hexByte(char high, char low) {
		int highInt = hexDigit(high);
		int lowInt = hexDigit(low);
		return (highInt << 4) | lowInt;
	}

	public static int hexDigit(char h) {
		if (h >= '0' && h <= '9') {
			return h - '0';
		} else if (h >= 'a' && h <= 'f') {
			return h - 'a' + 10;
		} else if (h >= 'A' && h <= 'F') {
			return h - 'A' + 10;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public static int hexByteFromString(String s) {
		if (s.length() != 2) {
			throw new IllegalArgumentException();
		}
		return hexByte(s.charAt(0), s.charAt(1));
	}

}
