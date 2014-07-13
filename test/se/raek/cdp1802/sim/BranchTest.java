package se.raek.cdp1802.sim;

import org.junit.Test;

public class BranchTest extends TestBase {

	@Test
	public void testBrPageStart() {
		executeAt(0x0100, 0x30, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBrPageEnd() {
		executeAt(0x01FE, 0x30, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBrPageBoundary() {
		executeAt(0x01FF, 0x30, 0x80);
		assertRP(0x0280);
	}

	@Test
	public void testNbrPageStart() {
		executeAt(0x0100, 0x38, 0x80);
		assertRP(0x0102);
	}

	@Test
	public void testNbrPageEnd() {
		executeAt(0x01FE, 0x38, 0x80);
		assertRP(0x0200);
	}

	@Test
	public void testNbrPageBoundary() {
		executeAt(0x01FF, 0x38, 0x80);
		assertRP(0x0201);
	}

	@Test
	public void testBqPassPageStart() {
		setQ(true);
		executeAt(0x0100, 0x31, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBqFailPageStart() {
		setQ(false);
		executeAt(0x0100, 0x31, 0x80);
		assertRP(0x0102);
	}

	@Test
	public void testBqPassPageEnd() {
		setQ(true);
		executeAt(0x01FE, 0x31, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBqFailPageEnd() {
		setQ(false);
		executeAt(0x01FE, 0x31, 0x80);
		assertRP(0x0200);
	}

	@Test
	public void testBqPassPageBoundary() {
		setQ(true);
		executeAt(0x01FF, 0x31, 0x80);
		assertRP(0x0280);
	}

	@Test
	public void testBqFailPageBoundary() {
		setQ(false);
		executeAt(0x01FF, 0x31, 0x80);
		assertRP(0x0201);
	}

	@Test
	public void testBnqPassPageStart() {
		setQ(false);
		executeAt(0x0100, 0x39, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBnqFailPageStart() {
		setQ(true);
		executeAt(0x0100, 0x39, 0x80);
		assertRP(0x0102);
	}

	@Test
	public void testBnqPassPageEnd() {
		setQ(false);
		executeAt(0x01FE, 0x39, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBnqFailPageEnd() {
		setQ(true);
		executeAt(0x01FE, 0x39, 0x80);
		assertRP(0x0200);
	}

	@Test
	public void testBnqPassPageBoundary() {
		setQ(false);
		executeAt(0x01FF, 0x39, 0x80);
		assertRP(0x0280);
	}

	@Test
	public void testBnqFailPageBoundary() {
		setQ(true);
		executeAt(0x01FF, 0x39, 0x80);
		assertRP(0x0201);
	}

	@Test
	public void testBzPassPageStart() {
		setD(0x00);
		executeAt(0x0100, 0x32, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBzFailPageStart() {
		setD(0x01);
		executeAt(0x0100, 0x32, 0x80);
		assertRP(0x0102);
	}

	@Test
	public void testBzPassPageEnd() {
		setD(0x00);
		executeAt(0x01FE, 0x32, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBzFailPageEnd() {
		setD(0x01);
		executeAt(0x01FE, 0x32, 0x80);
		assertRP(0x0200);
	}

	@Test
	public void testBzPassPageBoundary() {
		setD(0x00);
		executeAt(0x01FF, 0x32, 0x80);
		assertRP(0x0280);
	}

	@Test
	public void testBzFailPageBoundary() {
		setD(0x01);
		executeAt(0x01FF, 0x32, 0x80);
		assertRP(0x0201);
	}

	@Test
	public void testBnzPassPageStart() {
		setD(0x01);
		executeAt(0x0100, 0x3A, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBnzFailPageStart() {
		setD(0x00);
		executeAt(0x0100, 0x3A, 0x80);
		assertRP(0x0102);
	}

	@Test
	public void testBnzPassPageEnd() {
		setD(0x01);
		executeAt(0x01FE, 0x3A, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBnzFailPageEnd() {
		setD(0x00);
		executeAt(0x01FE, 0x3A, 0x80);
		assertRP(0x0200);
	}

	@Test
	public void testBnzPassPageBoundary() {
		setD(0x01);
		executeAt(0x01FF, 0x3A, 0x80);
		assertRP(0x0280);
	}

	@Test
	public void testBnzFailPageBoundary() {
		setD(0x00);
		executeAt(0x01FF, 0x3A, 0x80);
		assertRP(0x0201);
	}

}
