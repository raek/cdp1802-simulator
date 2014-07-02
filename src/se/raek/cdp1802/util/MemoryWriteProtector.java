package se.raek.cdp1802.util;

import se.raek.cdp1802.sim.Memory;

public final class MemoryWriteProtector implements Memory {

	final Memory m;

	public MemoryWriteProtector(Memory m) {
		this.m = m;
	}

	@Override
	public int read(int addr) {
		return m.read(addr);
	}

	@Override
	public void write(int addr, int data) {
		throw new RuntimeException("memory unwritable");
	}

}
