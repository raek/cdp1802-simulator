package se.raek.cdp1802.sim;

import org.junit.Test;

public class ArithmeticTest extends TestBase {

	@Test
	public void testAdcDfZeroIn() {
		setupMX(0x03);
		setD(0x02);
		setDf(false);
		execute(0x74);
		assertD(0x05);
		assertDf(false);
	}

	@Test
	public void testAdcDfZeroInOverflow() {
		setupMX(0x03);
		setD(0xFE);
		setDf(false);
		execute(0x74);
		assertD(0x01);
		assertDf(true);
	}

	@Test
	public void testAdcDfOneIn() {
		setupMX(0x03);
		setD(0x02);
		setDf(true);
		execute(0x74);
		assertD(0x06);
		assertDf(false);
	}

	@Test
	public void testAdcDfOneInOverflow() {
		setupMX(0x03);
		setD(0xFE);
		setDf(true);
		execute(0x74);
		assertD(0x02);
		assertDf(true);
	}

	@Test
	public void testSdbDfOneIn() {
		setupMX(0x03);
		setD(0x02);
		setDf(true);
		execute(0x75);
		assertD(0x01);
		assertDf(true);
	}

	@Test
	public void testSdbDfOneInUnderflow() {
		setupMX(0x03);
		setD(0x05);
		setDf(true);
		execute(0x75);
		assertD(0xFE);
		assertDf(false);
	}

	@Test
	public void testSdbDfZeroIn() {
		setupMX(0x03);
		setD(0x02);
		setDf(false);
		execute(0x75);
		assertD(0x00);
		assertDf(true);
	}

	@Test
	public void testSdbDfZeroInUnderflow() {
		setupMX(0x03);
		setD(0x05);
		setDf(false);
		execute(0x75);
		assertD(0xFD);
		assertDf(false);
	}

	@Test
	public void testSmbDfOneIn() {
		setupMX(0x03);
		setD(0x05);
		setDf(true);
		execute(0x77);
		assertD(0x02);
		assertDf(true);
	}

	@Test
	public void testSmbDfOneInUnderflow() {
		setupMX(0x03);
		setD(0x01);
		setDf(true);
		execute(0x77);
		assertD(0xFE);
		assertDf(false);
	}

	@Test
	public void testSmbDfZeroIn() {
		setupMX(0x03);
		setD(0x05);
		setDf(false);
		execute(0x77);
		assertD(0x01);
		assertDf(true);
	}

	@Test
	public void testSmbDfZeroInUnderflow() {
		setupMX(0x03);
		setD(0x01);
		setDf(false);
		execute(0x77);
		assertD(0xFD);
		assertDf(false);
	}

	@Test
	public void testAdciDfZeroIn() {
		setD(0x02);
		setDf(false);
		execute(0x7C, 0x03);
		assertD(0x05);
		assertDf(false);
	}

	@Test
	public void testAdciDfZeroInOverflow() {
		setD(0xFE);
		setDf(false);
		execute(0x7C, 0x03);
		assertD(0x01);
		assertDf(true);
	}

	@Test
	public void testAdciDfOneIn() {
		setD(0x02);
		setDf(true);
		execute(0x7C, 0x03);
		assertD(0x06);
		assertDf(false);
	}

	@Test
	public void testAdciDfOneInOverflow() {
		setD(0xFE);
		setDf(true);
		execute(0x7C, 0x03);
		assertD(0x02);
		assertDf(true);
	}

	@Test
	public void testSdbiDfOneIn() {
		setD(0x02);
		setDf(true);
		execute(0x7D, 0x03);
		assertD(0x01);
		assertDf(true);
	}

	@Test
	public void testSdbiDfOneInUnderflow() {
		setD(0x05);
		setDf(true);
		execute(0x7D, 0x03);
		assertD(0xFE);
		assertDf(false);
	}

	@Test
	public void testSdbiDfZeroIn() {
		setD(0x02);
		setDf(false);
		execute(0x7D, 0x03);
		assertD(0x00);
		assertDf(true);
	}

	@Test
	public void testSdbiDfZeroInUnderflow() {
		setD(0x05);
		setDf(false);
		execute(0x7D, 0x03);
		assertD(0xFD);
		assertDf(false);
	}

	@Test
	public void testSmbiDfOneIn() {
		setD(0x05);
		setDf(true);
		execute(0x7F, 0x03);
		assertD(0x02);
		assertDf(true);
	}

	@Test
	public void testSmbiDfOneInUnderflow() {
		setD(0x01);
		setDf(true);
		execute(0x7F, 0x03);
		assertD(0xFE);
		assertDf(false);
	}

	@Test
	public void testSmbiDfZeroIn() {
		setD(0x05);
		setDf(false);
		execute(0x7F, 0x03);
		assertD(0x01);
		assertDf(true);
	}

	@Test
	public void testSmbiDfZeroInUnderflow() {
		setD(0x01);
		setDf(false);
		execute(0x7F, 0x03);
		assertD(0xFD);
		assertDf(false);
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
