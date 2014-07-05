package se.raek.cdp1802.sim;

import static org.mockito.Mockito.inOrder;

import org.junit.Test;
import org.mockito.InOrder;

public class InstructionTest extends TestBase {

	@Test
	public void testIncDecWrapAround() {
		loadRom("2414");
		assertR(0x4, 0x0000);
		singleStep();
		assertR(0x4, 0xFFFF);
		singleStep();
		assertR(0x4, 0x0000);
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
		assertQ(false);
	}

	@Test
	public void testSeq() {
		loadRom("7B00");
		runUntilIdle();
		assertQ(true);
	}

	@Test
	public void testGloGhiPloPhi() {
		loadRom("F812B4F834A494B584A5");
		singleStep();
		singleStep();
		assertR(0x4, 0x1200);
		singleStep();
		singleStep();
		assertR(0x4, 0x1234);
		singleStep();
		assertD(0x12);
		singleStep();
		assertR(0x5, 0x1200);
		singleStep();
		assertD(0x34);
		singleStep();
		assertR(0x5, 0x1234);
	}

	@Test
	public void testNop() {
		loadRom("C4");
		singleStep();
		assertR(0x0, 0x0001);
	}

	@Test
	public void testOr() {
		loadRom("F80FF100");
		loadRam("55");
		setupXToRamStart();
		runUntilIdle();
		assertD(0x5F);
	}

	@Test
	public void testOri() {
		loadRom("F80FF95500");
		runUntilIdle();
		assertD(0x5F);
	}

	@Test
	public void testLdi() {
		loadRom("F8A500");
		runUntilIdle();
		assertD(0xA5);
	}

}
