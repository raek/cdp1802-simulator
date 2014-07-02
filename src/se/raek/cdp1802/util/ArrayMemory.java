package se.raek.cdp1802.util;

import se.raek.cdp1802.sim.Memory;

public final class ArrayMemory implements Memory {

	private final int[] array;

	public ArrayMemory(int length) {
		array = new int[length];
	}

	@Override
	public int read(int addr) {
		return array[addr];
	}

	@Override
	public void write(int addr, int data) {
		array[addr] = data;
	}

}
