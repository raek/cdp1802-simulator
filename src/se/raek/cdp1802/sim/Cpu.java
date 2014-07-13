package se.raek.cdp1802.sim;

public final class Cpu {

	public static final class State {

		public int d;
		public int p;
		public int x;
		public int t;
		public final int[] r = new int[16];
		public boolean df;
		public boolean ie = true;
		public boolean q;
		public boolean idle;

	}

	public static final class InstructionNotImplementedException extends
			RuntimeException {

		private static final long serialVersionUID = 1L;
		public final int in;

		public InstructionNotImplementedException(int i, int n) {
			this.in = (i << 4) | n;
		}

		@Override
		public String getMessage() {
			return String.format("Instruction not implemented: %02X", in);
		}

	}

	private State s;
	private Memory m;
	private Io io;

	public Cpu(State s, Memory m, Io io) {
		this.s = s;
		this.m = m;
		this.io = io;
	}

	public void tick() {
		if (s.idle) {
			return;
		}
		int in = fetch();
		int i = highNibble(in);
		int n = lowNibble(in);
		execute(i, n);
	}

	private int fetch() {
		int in = m.read(s.r[s.p]++);
		s.r[s.p] &= 0xFFFF;
		return in;
	}

	private static int lowNibble(int in) {
		return in & 0xF;
	}

	private static int highNibble(int in) {
		return in >> 4;
	}

	private static int lowByte(int w) {
		return w & 0x00FF;
	}

	private static int highByte(int w) {
		return w >> 8;
	}

	private static int withLowByte(int w, int b) {
		return (w & 0xFF00) | b;
	}

	private static int withHighByte(int w, int b) {
		return (b << 8) | (w & 0x00FF);
	}

	private void execute(int i, int n) {
		switch (i) {
		case 0x0:
			if (n == 0) { // IDL
				s.idle = true;
			} else { // LDN
				s.d = m.read(s.r[n]);
			}
			break;
		case 0x1: // INC
			s.r[n]++;
			s.r[n] &= 0xFFFF;
			break;
		case 0x2: // DEC
			s.r[n]--;
			s.r[n] &= 0xFFFF;
			break;
		case 0x3:
			executeShortBranch(n);
			break;
		case 0x4: // LDA
			s.d = m.read(s.r[n]++);
			s.r[n] &= 0xFFFF;
			break;
		case 0x5: // STR
			m.write(s.r[n], s.d);
			break;
		case 0x6:
			executeInputOutput(n);
			break;
		case 0x7:
			executeControl(n);
			break;
		case 0x8: // GLO
			s.d = lowByte(s.r[n]);
			break;
		case 0x9: // GHI
			s.d = highByte(s.r[n]);
			break;
		case 0xA: // PLO
			s.r[n] = withLowByte(s.r[n], s.d);
			break;
		case 0xB: // PHI
			s.r[n] = withHighByte(s.r[n], s.d);
			break;
		case 0xC:
			executeLongBranch(n);
			break;
		case 0xD: // SEP
			s.p = n;
			break;
		case 0xE: // SEX
			s.x = n;
			break;
		case 0xF:
			executeArithmeticLogic(n);
			break;
		}
	}

	private void executeShortBranch(int n) {
		switch (n) {
		case 0x0: // BR
			s.r[s.p] = withLowByte(s.r[s.p], m.read(s.r[s.p]));
			break;
		case 0x8: // NBR
			s.r[s.p]++;
			s.r[s.p] &= 0xFFFF;
			break;
		default:
			throw new InstructionNotImplementedException(0x3, n);
		}
	}

	private void executeInputOutput(int n) {
		if (n == 0) { // IRX
			s.r[s.x]++;
			s.r[s.x] &= 0xFFFF;
		} else if (n < 8) { // OUT
			io.output(n, m.read(s.r[s.x]++));
			s.r[s.x] &= 0xFFFF;
		} else if (n == 8) {
			throw new InstructionNotImplementedException(0x6, n);
		} else { // INP
			int data = io.input(n & 0x7);
			s.d = data;
			m.write(s.r[s.x], data);
		}
	}

	private void executeControl(int n) {
		switch (n) {
		case 0x2: // LDXA
			s.d = m.read(s.r[s.x]++);
			s.r[s.x] &= 0xFFFF;
			break;
		case 0x3: // STXD
			m.write(s.r[s.x]--, s.d);
			s.r[s.x] &= 0xFFFF;
			break;
		case 0x4: // ADC
		{
			int carry = s.df ? 1 : 0;
			int sum = m.read(s.r[s.x]) + s.d + carry;
			s.d = sum & 0xFF;
			s.df = sum > 0xFF;
			break;
		}
		case 0x5: // SDB
		{
			int borrow = s.df ? 0 : 1;
			int diff = m.read(s.r[s.x]) - s.d - borrow;
			s.d = diff & 0xFF;
			s.df = diff >= 0x00;
			break;
		}
		case 0x6: // SHRC
		{
			int oldD = s.d;
			boolean oldDf = s.df;
			s.d = (s.d >>> 1) | (oldDf ? 0x80 : 0x00);
			s.df = (oldD & 0x01) != 0x00;
			break;
		}
		case 0x7: // SMB
		{
			int borrow = s.df ? 0 : 1;
			int diff = s.d - m.read(s.r[s.x]) - borrow;
			s.d = diff & 0xFF;
			s.df = diff >= 0x00;
			break;
		}
		case 0xA: // REQ
			s.q = false;
			break;
		case 0xB: // SEQ
			s.q = true;
			break;
		case 0xC: // ADCI
		{
			int carry = s.df ? 1 : 0;
			int sum = m.read(s.r[s.p]++) + s.d + carry;
			s.r[s.p] &= 0xFFFF;
			s.d = sum & 0xFF;
			s.df = sum > 0xFF;
			break;
		}
		case 0xD: // SDBI
		{
			int borrow = s.df ? 0 : 1;
			int diff = m.read(s.r[s.p]++) - s.d - borrow;
			s.r[s.p] &= 0xFFFF;
			s.d = diff & 0xFF;
			s.df = diff >= 0x00;
			break;
		}
		case 0xE: // SHLC
		{
			int oldD = s.d;
			boolean oldDf = s.df;
			s.d = (oldD << 1) | (oldDf ? 0x01 : 0x00);
			s.d &= 0xFF;
			s.df = (oldD & 0x80) != 0x00;
			break;
		}
		case 0xF: // SMBI
		{
			int borrow = s.df ? 0 : 1;
			int diff = s.d - m.read(s.r[s.p]++) - borrow;
			s.r[s.p] &= 0xFFFF;
			s.d = diff & 0xFF;
			s.df = diff >= 0x00;
			break;
		}
		default:
			throw new InstructionNotImplementedException(0x7, n);
		}
	}

	private void executeLongBranch(int n) {
		switch (n) {
		case 0x4: // NOP
			break;
		default:
			throw new InstructionNotImplementedException(0xC, n);
		}
	}

	private void executeArithmeticLogic(int n) {
		switch (n) {
		case 0x0: // LDX
			s.d = m.read(s.r[s.x]);
			break;
		case 0x1: // OR
			s.d = m.read(s.r[s.x]) | s.d;
			break;
		case 0x2: // AND
			s.d = m.read(s.r[s.x]) & s.d;
			break;
		case 0x3: // XOR
			s.d = m.read(s.r[s.x]) ^ s.d;
			break;
		case 0x4: // ADD
		{
			int sum = m.read(s.r[s.x]) + s.d;
			s.d = sum & 0xFF;
			s.df = sum > 0xFF;
			break;
		}
		case 0x5: // SD
		{
			int diff = m.read(s.r[s.x]) - s.d;
			s.d = diff & 0xFF;
			s.df = diff >= 0x00;
			break;
		}
		case 0x6: // SHR
			s.df = (s.d & 0x01) != 0x00;
			s.d = s.d >>> 1;
			break;
		case 0x7: // SM
		{
			int diff = s.d - m.read(s.r[s.x]);
			s.d = diff & 0xFF;
			s.df = diff >= 0x00;
			break;
		}
		case 0x8: // LDI
			s.d = m.read(s.r[s.p]++);
			s.r[s.p] &= 0xFFFF;
			break;
		case 0x9: // ORI
			s.d = m.read(s.r[s.p]++) | s.d;
			s.r[s.p] &= 0xFFFF;
			break;
		case 0xA: // ANI
			s.d = m.read(s.r[s.p]++) & s.d;
			s.r[s.p] &= 0xFFFF;
			break;
		case 0xB: // XRI
			s.d = m.read(s.r[s.p]++) ^ s.d;
			s.r[s.p] &= 0xFFFF;
			break;
		case 0xC: // ADI
		{
			int sum = m.read(s.r[s.p]++) + s.d;
			s.r[s.p] &= 0xFFFF;
			s.d = sum & 0xFF;
			s.df = sum > 0xFF;
			break;
		}
		case 0xD: // SDI
		{
			int diff = m.read(s.r[s.p]++) - s.d;
			s.r[s.p] &= 0xFFFF;
			s.d = diff & 0xFF;
			s.df = diff >= 0x00;
			break;
		}
		case 0xE: // SHL
			s.df = (s.d & 0x80) != 0x00;
			s.d = (s.d << 1) & 0xFF;
			break;
		case 0xF: // SMI
		{
			int diff = s.d - m.read(s.r[s.p]++);
			s.r[s.p] &= 0xFFFF;
			s.d = diff & 0xFF;
			s.df = diff >= 0x00;
			break;
		}
		}
	}

}
