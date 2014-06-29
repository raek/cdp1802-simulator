package se.raek.cdp1802.sim;

import se.raek.cdp1802.sim.Memory;

public class MockMemory implements Memory {

	private int value;

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

	@Override
	public void write(int addr, int data) {
		if (addr == 0) {
			value = data;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

}
