package se.raek.cdp1802.sim;

import static org.junit.Assert.*;

import org.junit.Test;

import se.raek.cdp1802.sim.Cpu;
import se.raek.cdp1802.sim.Memory;

public class CpuTest {

	@Test
	public void testFetch() {
		Cpu.State s = new Cpu.State();
		Memory m = new MockMemory(0xC4);
		Io io = new MockIo();
		Cpu cpu = new Cpu(s, m, io);
		cpu.tick();
		assertEquals(0x0001, s.r[s.p]);
	}

}
