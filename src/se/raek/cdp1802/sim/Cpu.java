package se.raek.cdp1802.sim;

public class Cpu {

	public static class State {

		public int d;
		public int p;
		public int x;
		public int t;
		public final int[] r = new int[16];
		public boolean df;
		public boolean ie = true;
		public boolean q;

	}

	private State s;
	private Memory m;

	public Cpu(State state, Memory memory) {
		s = state;
		m = memory;
	}

	public void tick() {
		int in = fetch();
		int i = highNibble(in);
		int n = lowNibble(in);
		execute(i, n);
	}

	private int fetch() {
		return m.read(s.r[s.p]++);
	}

	private static int lowNibble(int in) {
		return in & 0xF;
	}

	private static int highNibble(int in) {
		return in >> 4;
	}

	private void execute(int i, int n) {
		switch (i) {
		case 0x3:
			executeShortBranch(n);
			break;
		case 0x7:
			executeControl(n);
			break;
		case 0xC:
			executeLongBranch(n);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	private void executeControl(int n) {
		switch (n) {
		case 0xA: // REQ
			s.q = false;
			break;
		case 0xB: // SEQ
			s.q = true;
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	private void executeShortBranch(int n) {
		switch (n) {
		case 0x0: // BR
			s.r[s.p] = m.read(s.r[s.p]);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	private void executeLongBranch(int n) {
		switch (n) {
		case 0x4:
			// NOP
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

}
