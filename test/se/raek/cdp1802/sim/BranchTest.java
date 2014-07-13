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
}
