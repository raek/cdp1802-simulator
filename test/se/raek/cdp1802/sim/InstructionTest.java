package se.raek.cdp1802.sim;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InOrder;

public class InstructionTest extends TestBase {

	@Test
	public void testIncDecWrapAround() {
		assertR(0x4, 0x0000);
		execute(0x24);
		assertR(0x4, 0xFFFF);
		execute(0x14);
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
	public void testInp() {
		loadRom("696A6B6C6D6E6F");
		setupXToRamStart();
		for (int i = 1; i <= 7; i++) {
			when(io.input(i)).thenReturn((i << 4) | i);
		}
		InOrder inOrder = inOrder(io);
		for (int i = 1; i <= 7; i++) {
			singleStep();
			assertD((i << 4) | i);
			assertMX((i << 4) | i);
			inOrder.verify(io).input(i);
		}
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testIrxLdxaStxdLdx() {
		loadRom("F80173F80273F80373607272F000");
		setupXToRamEnd();
		for (int i = 0; i < 8; i++) {
			singleStep();
		}
		assertD(0x03);
		singleStep();
		assertD(0x02);
		singleStep();
		assertD(0x01);
		assertXAtRamEnd();
	}

	@Test
	public void testShrcDfZeroInZeroOut() {
		setD(0x3C);
		setDf(false);
		execute(0x76);
		assertD(0x1E);
		assertDf(false);
	}

	@Test
	public void testShrcDfZeroInOneOut() {
		setD(0x3D);
		setDf(false);
		execute(0x76);
		assertD(0x1E);
		assertDf(true);
	}

	@Test
	public void testShrcDfOneInZeroOut() {
		setD(0x3C);
		setDf(true);
		execute(0x76);
		assertD(0x9E);
		assertDf(false);
	}

	@Test
	public void testShrcDfOneInOneOut() {
		setD(0x3D);
		setDf(true);
		execute(0x76);
		assertD(0x9E);
		assertDf(true);
	}

	@Test
	public void testReq() {
		execute(0x7B);
		execute(0x7A);
		assertQ(false);
	}

	@Test
	public void testSeq() {
		execute(0x7B);
		assertQ(true);
	}

	@Test
	public void testShlcDfZeroInZeroOut() {
		setD(0x3C);
		setDf(false);
		execute(0x7E);
		assertD(0x78);
		assertDf(false);
	}

	@Test
	public void testShlcDfZeroInOneOut() {
		setD(0xBC);
		setDf(false);
		execute(0x7E);
		assertD(0x78);
		assertDf(true);
	}

	@Test
	public void testShlcDfOneInZeroOut() {
		setD(0x3C);
		setDf(true);
		execute(0x7E);
		assertD(0x79);
		assertDf(false);
	}

	@Test
	public void testShlcDfOneInOneOut() {
		setD(0xBC);
		setDf(true);
		execute(0x7E);
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
		execute(0xC4);
		assertR(0x0, 0x0001);
	}

	@Test
	public void testOr() {
		setupMX(0x0F);
		setD(0x55);
		execute(0xF1);
		assertD(0x5F);
	}

	@Test
	public void testAnd() {
		setupMX(0x0F);
		setD(0x55);
		execute(0xF2);
		assertD(0x05);
	}

	@Test
	public void testXor() {
		setupMX(0x0F);
		setD(0x55);
		execute(0xF3);
		assertD(0x5A);
	}

	@Test
	public void testAdd() {
		setupMX(0x03);
		setD(0x02);
		execute(0xF4);
		assertD(0x05);
		assertDf(false);
	}

	@Test
	public void testAddOverflow() {
		setupMX(0x03);
		setD(0xFE);
		execute(0xF4);
		assertD(0x01);
		assertDf(true);
	}

	@Test
	public void testSd() {
		setupMX(0x03);
		setD(0x02);
		execute(0xF5);
		assertD(0x01);
		assertDf(true);
	}

	@Test
	public void testSdUnderflow() {
		setupMX(0x03);
		setD(0x05);
		execute(0xF5);
		assertD(0xFE);
		assertDf(false);
	}

	@Test
	public void testShrDfZeroOut() {
		setD(0x3C);
		execute(0xF6);
		assertD(0x1E);
		assertDf(false);
	}

	@Test
	public void testShrDfOneOut() {
		setD(0x3D);
		execute(0xF6);
		assertD(0x1E);
		assertDf(true);
	}

	@Test
	public void testSm() {
		setupMX(0x03);
		setD(0x05);
		execute(0xF7);
		assertD(0x02);
		assertDf(true);
	}

	@Test
	public void testSmUnderflow() {
		setupMX(0x03);
		setD(0x01);
		execute(0xF7);
		assertD(0xFE);
		assertDf(false);
	}

	@Test
	public void testLdi() {
		execute(0xF8, 0xA5);
		assertD(0xA5);
	}

	@Test
	public void testOri() {
		setD(0x55);
		execute(0xF9, 0x0F);
		assertD(0x5F);
	}

	@Test
	public void testAni() {
		setD(0x55);
		execute(0xFA, 0x0F);
		assertD(0x05);
	}

	@Test
	public void testXri() {
		setD(0x55);
		execute(0xFB, 0x0F);
		assertD(0x5A);
	}

	@Test
	public void testAdi() {
		setD(0x02);
		execute(0xFC, 0x03);
		assertD(0x05);
		assertDf(false);
	}

	@Test
	public void testAdiOverflow() {
		setD(0xFE);
		execute(0xFC, 0x03);
		assertD(0x01);
		assertDf(true);
	}

	@Test
	public void testSdi() {
		setD(0x02);
		execute(0xFD, 0x03);
		assertD(0x01);
		assertDf(true);
	}

	@Test
	public void testSdiUnderflow() {
		setD(0x05);
		execute(0xFD, 0x03);
		assertD(0xFE);
		assertDf(false);
	}

	@Test
	public void testShlDfZeroOut() {
		setD(0x3C);
		execute(0xFE);
		assertD(0x78);
		assertDf(false);
	}

	@Test
	public void testShlDfOneOut() {
		setD(0xBC);
		execute(0xFE);
		assertD(0x78);
		assertDf(true);
	}

	@Test
	public void testSmi() {
		setD(0x05);
		execute(0xFF, 0x03);
		assertD(0x02);
		assertDf(true);
	}

	@Test
	public void testSmiUnderflow() {
		setD(0x01);
		execute(0xFF, 0x03);
		assertD(0xFE);
		assertDf(false);
	}

}
