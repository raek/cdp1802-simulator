package se.raek.cdp1802.sim;

import se.raek.cdp1802.sim.Memory;

public class MockMemory implements Memory {

	private final int value;

	public MockMemory(int value) {
		if (value < 0 || value > 0xFF) {
			throw new IllegalArgumentException();
		}
		this.value = value;
	}

	@Override
	public int read(int addr) {
		if (addr == 0) {
			return value;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

}
