package se.raek.cdp1802.sim;

import org.junit.Test;

public class LogicTest extends TestBase {

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

}
