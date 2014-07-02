package se.raek.cdp1802.sim;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.raek.cdp1802.util.ArrayMemory;
import se.raek.cdp1802.util.MemoryMapper;
import se.raek.cdp1802.util.MemoryWriteProtector;
import se.raek.cdp1802.util.Utils;

public class InstructionTest {

	private Cpu.State s;
	private Memory rom;
	private Memory ram;
	private Io io;
	private Cpu cpu;

	@Before
	public void setUp() {
		s = new Cpu.State();
		rom = new ArrayMemory(0x100);
		ram = new ArrayMemory(0x100);
		MemoryMapper mm = new MemoryMapper();
		mm.map(0x0000, 0x100, new MemoryWriteProtector(rom));
		mm.map(0x0100, 0x100, ram);
		io = new MockIo();
		cpu = new Cpu(s, mm, io);
	}

	private void loadRom(String hexString) {
		Utils.loadHex(rom, hexString);
	}

	private void runUntilIdle() {
		while (!s.idle) {
			cpu.tick();
		}
	}

	@Test
	public void testReq() {
		loadRom("7B7A00");
		runUntilIdle();
		assertFalse(s.q);
	}

	@Test
	public void testSeq() {
		loadRom("7B00");
		runUntilIdle();
		assertTrue(s.q);
	}

}
