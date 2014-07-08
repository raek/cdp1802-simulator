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
		for (int i = 1; i <= 7; i++) {
			int pattern = (i << 4) | i;
			execute(0x60 + i, pattern);
		}
		InOrder inOrder = inOrder(io);
		for (int i = 1; i <= 7; i++) {
			int pattern = (i << 4) | i;
			inOrder.verify(io).output(i, pattern);
		}
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testInp() {
		setupXToRamStart();
		for (int i = 1; i <= 7; i++) {
			when(io.input(i)).thenReturn((i << 4) | i);
		}
		InOrder inOrder = inOrder(io);
		for (int i = 1; i <= 7; i++) {
			execute(0x68 + i);
			assertD((i << 4) | i);
			assertMX((i << 4) | i);
			inOrder.verify(io).input(i);
		}
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testIrxLdxaStxdLdx() {
		setupXToRamEnd();
		setD(0x01);
		execute(0x73);
		setD(0x02);
		execute(0x73);
		setD(0x03);
		execute(0x73);
		setD(0x00);
		execute(0x60);
		execute(0x72);
		assertD(0x03);
		execute(0x72);
		assertD(0x02);
		execute(0xF0);
		assertD(0x01);
		assertXAtRamEnd();
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
	public void testGloGhiPloPhi() {
		setD(0x12);
		execute(0xB4);
		assertR(0x4, 0x1200);
		setD(0x34);
		execute(0xA4);
		assertR(0x4, 0x1234);
		execute(0x94);
		assertD(0x12);
		execute(0xB5);
		assertR(0x5, 0x1200);
		execute(0x84);
		assertD(0x34);
		execute(0xA5);
		assertR(0x5, 0x1234);
	}

	@Test
	public void testNop() {
		execute(0xC4);
		assertR(0x0, 0x0001);
	}

	@Test
	public void testLdi() {
		execute(0xF8, 0xA5);
		assertD(0xA5);
	}

}
