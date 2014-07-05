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
	public void testShrc() {
		loadRom("F83C76F83D76F83C76F83D76");
		setDf(false);
		singleStep();
		singleStep();
		assertD(0x1E);
		assertDf(false);
		setDf(false);
		singleStep();
		singleStep();
		assertD(0x1E);
		assertDf(true);
		setDf(true);
		singleStep();
		singleStep();
		assertD(0x9E);
		assertDf(false);
		setDf(true);
		singleStep();
		singleStep();
		assertD(0x9E);
		assertDf(true);
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
	public void testShlc() {
		loadRom("F83C7EF8BC7EF83C7EF8BC7E");
		setDf(false);
		singleStep();
		singleStep();
		assertD(0x78);
		assertDf(false);
		setDf(false);
		singleStep();
		singleStep();
		assertD(0x78);
		assertDf(true);
		setDf(true);
		singleStep();
		singleStep();
		assertD(0x79);
		assertDf(false);
		setDf(true);
		singleStep();
		singleStep();
		assertD(0x79);
		assertDf(true);
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
	public void testOrAndXor() {
		loadRom("F855F1F855F2F855F3");
		loadRam("0F");
		setupXToRamStart();
		singleStep();
		singleStep();
		assertD(0x5F);
		singleStep();
		singleStep();
		assertD(0x05);
		singleStep();
		singleStep();
		assertD(0x5A);
	}

	@Test
	public void testShr() {
		loadRom("F83CF6F83DF6");
		singleStep();
		singleStep();
		assertD(0x1E);
		assertDf(false);
		singleStep();
		singleStep();
		assertD(0x1E);
		assertDf(true);
	}

	@Test
	public void testLdi() {
		loadRom("F8A500");
		runUntilIdle();
		assertD(0xA5);
	}

	@Test
	public void testOriAniXri() {
		loadRom("F80FF955F80FFA55F80FFB55");
		singleStep();
		singleStep();
		assertD(0x5F);
		singleStep();
		singleStep();
		assertD(0x05);
		singleStep();
		singleStep();
		assertD(0x5A);
	}

	@Test
	public void testShl() {
		loadRom("F83CFEF8BCFE");
		singleStep();
		singleStep();
		assertD(0x78);
		assertDf(false);
		singleStep();
		singleStep();
		assertD(0x78);
		assertDf(true);
	}

}
