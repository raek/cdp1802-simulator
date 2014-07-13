package se.raek.cdp1802.sim;

import org.junit.Test;

public class BranchTest extends TestBase {

	@Test
	public void testBrPageStart() {
		executeAtPageStart(0x01, 0x30, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBrPageEnd() {
		executeBeforePageEnd(0x01, 0x30, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBrPageBoundary() {
		executeOnFollowingPageBoundary(0x01, 0x30, 0x80);
		assertRP(0x0280);
	}

	@Test
	public void testNbrPageStart() {
		executeAtPageStart(0x01, 0x38, 0x80);
		assertRP(0x0102);
	}

	@Test
	public void testNbrPageEnd() {
		executeBeforePageEnd(0x01, 0x38, 0x80);
		assertRP(0x0200);
	}

	@Test
	public void testNbrPageBoundary() {
		executeOnFollowingPageBoundary(0x01, 0x38, 0x80);
		assertRP(0x0201);
	}

	@Test
	public void testBqPassPageStart() {
		setQ(true);
		executeAtPageStart(0x01, 0x31, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBqFailPageStart() {
		setQ(false);
		executeAtPageStart(0x01, 0x31, 0x80);
		assertRP(0x0102);
	}

	@Test
	public void testBqPassPageEnd() {
		setQ(true);
		executeBeforePageEnd(0x01, 0x31, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBqFailPageEnd() {
		setQ(false);
		executeBeforePageEnd(0x01, 0x31, 0x80);
		assertRP(0x0200);
	}

	@Test
	public void testBqPassPageBoundary() {
		setQ(true);
		executeOnFollowingPageBoundary(0x01, 0x31, 0x80);
		assertRP(0x0280);
	}

	@Test
	public void testBqFailPageBoundary() {
		setQ(false);
		executeOnFollowingPageBoundary(0x01, 0x31, 0x80);
		assertRP(0x0201);
	}

	@Test
	public void testBnqPassPageStart() {
		setQ(false);
		executeAtPageStart(0x01, 0x39, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBnqFailPageStart() {
		setQ(true);
		executeAtPageStart(0x01, 0x39, 0x80);
		assertRP(0x0102);
	}

	@Test
	public void testBnqPassPageEnd() {
		setQ(false);
		executeBeforePageEnd(0x01, 0x39, 0x80);
		assertRP(0x0180);
	}

	@Test
	public void testBnqFailPageEnd() {
		setQ(true);
		executeBeforePageEnd(0x01, 0x39, 0x80);
		assertRP(0x0200);
	}

	@Test
	public void testBnqPassPageBoundary() {
		setQ(false);
		executeOnFollowingPageBoundary(0x01, 0x39, 0x80);
		assertRP(0x0280);
	}

	@Test
	public void testBnqFailPageBoundary() {
		setQ(true);
		executeOnFollowingPageBoundary(0x01, 0x39, 0x80);
		assertRP(0x0201);
	}
}
