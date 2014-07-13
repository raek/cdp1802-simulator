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
	private Memory m;
	protected Io io;
	private Cpu cpu;

	private static final int romStart = 0x0000;
	private static final int romLength = 0x400;
	private static final int ramStart = 0x0400;
	private static final int ramLength = 0x400;
	private static final int defaultX = 0xF;

	@Before
	public void setUp() {
		s = new Cpu.State();
		rom = new ArrayMemory(romLength);
		ram = new ArrayMemory(ramLength);
		MemoryMapper mm = new MemoryMapper();
		mm.map(romStart, romLength, new MemoryWriteProtector(rom));
		mm.map(ramStart, ramLength, ram);
		m = mm;
		io = mock(Io.class);
		cpu = new Cpu(s, m, io);
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

	protected void setupXToRamEnd() {
		s.r[defaultX] = ramStart + ramLength - 1;
		s.x = defaultX;
	}

	protected void setupMX(int data) {
		setupXToRamStart();
		m.write(ramStart, data);
	}

	protected void runUntilIdle() {
		while (!s.idle) {
			cpu.tick();
		}
	}

	protected void singleStep() {
		cpu.tick();
	}

	protected void execute(int instruction) {
		rom.write(0, instruction);
		s.p = 0x0;
		s.r[s.p] = romStart;
		s.idle = false;
		cpu.tick();
	}

	protected void execute(int instruction, int immediateData) {
		rom.write(0, instruction);
		rom.write(1, immediateData);
		s.p = 0x0;
		s.r[s.p] = romStart;
		s.idle = false;
		cpu.tick();
	}

	protected void executeAtPageStart(int page, int instruction, int immediateData) {
		int pageStart = page << 8;
		rom.write(pageStart, instruction);
		rom.write(pageStart + 1, immediateData);
		s.p = 0x0;
		s.r[s.p] = pageStart;
		s.idle = false;
		cpu.tick();
	}

	protected void executeBeforePageEnd(int page, int instruction, int immediateData) {
		int nextPageStart = (page + 1) << 8;
		rom.write(nextPageStart - 2, instruction);
		rom.write(nextPageStart - 1, immediateData);
		s.p = 0x0;
		s.r[s.p] = nextPageStart - 2;
		s.idle = false;
		cpu.tick();
	}

	protected void executeOnFollowingPageBoundary(int page, int instruction, int immediateData) {
		int nextPageStart = (page + 1) << 8;
		rom.write(nextPageStart - 1, instruction);
		rom.write(nextPageStart, immediateData);
		s.p = 0x0;
		s.r[s.p] = nextPageStart - 1;
		s.idle = false;
		cpu.tick();
	}

	protected void setD(int d) {
		s.d = d;
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

	protected void assertRP(int expected) {
		assertEquals(expected, s.r[s.p]);
	}

	protected void assertMX(int expected) {
		assertEquals(expected, m.read(s.r[s.x]));
	}

	protected void assertXAtRamEnd() {
		assertEquals(ramStart + ramLength - 1, s.r[s.x]);
	}

	protected void assertDf(boolean expected) {
		assertEquals(expected, s.df);
	}

	protected void assertQ(boolean expected) {
		assertEquals(expected, s.q);
	}

}