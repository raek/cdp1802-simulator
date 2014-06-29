package se.raek.cdp1802.sim;

public class MockIo implements Io {

	@Override
	public void output(int n, int data) {
	}

	@Override
	public int input(int n) {
		return 0;
	}

}
