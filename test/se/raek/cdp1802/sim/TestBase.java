package se.raek.cdp1802.sim;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;

import se.raek.cdp1802.util.ArrayMemory;
import se.raek.cdp1802.util.MemoryMapper;
import se.raek.cdp1802.util.MemoryWriteProtector;
import se.raek.cdp1802.util.Utils;

public class TestBase {

	private Cpu.State s;
	private Memory rom;
	private Memory ram;
	protected Io io;
	private Cpu cpu;

	private static final int romStart = 0x0000;
	private static final int romLength = 0x100;
	private static final int ramStart = 0x0100;
	private static final int ramLength = 0x100;
	private static final int defaultX = 0xF;

	@Before
	public void setUp() {
		s = new Cpu.State();
		rom = new ArrayMemory(0x100);
		ram = new ArrayMemory(0x100);
		MemoryMapper mm = new MemoryMapper();
		mm.map(romStart, romLength, new MemoryWriteProtector(rom));
		mm.map(ramStart, ramLength, ram);
		io = mock(Io.class);
		cpu = new Cpu(s, mm, io);
	}

	protected void loadRom(String hexString) {
		Utils.loadHex(rom, hexString);
	}

	protected void loadRam(String hexString) {
		Utils.loadHex(ram, hexString);
	}

	protected void setupXToRamStart() {
		s.r[defaultX] = ramStart;
		s.x = defaultX;
	}

	protected void runUntilIdle() {
		while (!s.idle) {
			cpu.tick();
		}
	}

	protected void singleStep() {
		cpu.tick();
	}

	protected void setDf(boolean df) {
		s.df = df;
	}

	protected void assertD(int expected) {
		assertEquals(expected, s.d);
	}

	protected void assertR(int r, int expected) {
		assertEquals(expected, s.r[r]);
	}

	protected void assertDf(boolean expected) {
		assertEquals(expected, s.df);
	}

	protected void assertQ(boolean expected) {
		assertEquals(expected, s.q);
	}

}