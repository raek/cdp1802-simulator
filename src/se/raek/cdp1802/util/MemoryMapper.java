package se.raek.cdp1802.util;

import java.util.Collection;
import java.util.LinkedList;

import se.raek.cdp1802.sim.Memory;

public final class MemoryMapper implements Memory {

	private static final class Range {

		public final int start;
		public final int end;
		public final Memory memory;

		public Range(int start, int end, Memory memory) {
			this.start = start;
			this.end = end;
			this.memory = memory;
		}

	}

	private final Collection<Range> ranges = new LinkedList<Range>();

	public void map(int start, int length, Memory memory) {
		Range newRange = new Range(start, start + length, memory);
		for (Range range : ranges) {
			if (overlaps(newRange, range)) {
				throw new RuntimeException("overlapping ranges");
			}
		}
		ranges.add(newRange);
	}

	private static boolean overlaps(Range x, Range y) {
		return Math.max(x.start, y.start) < Math.min(x.end, y.end);
	}

	private static boolean contains(Range range, int addr) {
		return addr >= range.start && addr < range.end;
	}

	@Override
	public int read(int addr) {
		for (Range range : ranges) {
			if (contains(range, addr)) {
				return range.memory.read(addr - range.start);
			}
		}
		throw new RuntimeException("memory unmapped");
	}

	@Override
	public void write(int addr, int data) {
		for (Range range : ranges) {
			if (contains(range, addr)) {
				range.memory.write(addr - range.start, data);
				return;
			}
		}
		throw new RuntimeException("memory unmapped");
	}

}
