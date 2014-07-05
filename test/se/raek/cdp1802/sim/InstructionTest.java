package se.raek.cdp1802.sim;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

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

	private void loadRom(String hexString) {
		Utils.loadHex(rom, hexString);
	}

	private void loadRam(String hexString) {
		Utils.loadHex(ram, hexString);
	}

	private void setupXToRamStart() {
		s.r[defaultX] = ramStart;
		s.x = defaultX;
	}

	private void runUntilIdle() {
		while (!s.idle) {
			cpu.tick();
		}
	}

	private void singleStep() {
		cpu.tick();
	}

	@Test
	public void testIncDecWrapAround() {
		loadRom("2414");
		assertEquals(0x0000, s.r[0x4]);
		singleStep();
		assertEquals(0xFFFF, s.r[0x4]);
		singleStep();
		assertEquals(0x0000, s.r[0x4]);
	}

	@Test
	public void testOut() {
		loadRom("611162226333644465556666677700");
		runUntilIdle();
		InOrder inOrder = inOrder(io);
		for (int i = 1; i <= 7; i++) {
			inOrder.verify(io).output(i, (i << 4) | i);	
		}
		inOrder.verifyNoMoreInteractions();
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

	@Test
	public void testGloGhiPloPhi() {
		loadRom("F812B4F834A494B584A5");
		singleStep();
		singleStep();
		assertEquals(0x1200, s.r[0x4]);
		singleStep();
		singleStep();
		assertEquals(0x1234, s.r[0x4]);
		singleStep();
		assertEquals(0x12, s.d);
		singleStep();
		assertEquals(0x1200, s.r[0x5]);
		singleStep();
		assertEquals(0x34, s.d);
		singleStep();
		assertEquals(0x1234, s.r[0x5]);
	}

	@Test
	public void testOr() {
		loadRom("F80FF100");
		loadRam("55");
		setupXToRamStart();
		runUntilIdle();
		assertEquals(0x5F, s.d);
	}

	@Test
	public void testOri() {
		loadRom("F80FF95500");
		runUntilIdle();
		assertEquals(0x5F, s.d);
	}

	@Test
	public void testLdi() {
		loadRom("F8A500");
		runUntilIdle();
		assertEquals(0xA5, s.d);
	}

}
