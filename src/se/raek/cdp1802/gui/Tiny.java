package se.raek.cdp1802.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import se.raek.cdp1802.sim.Cpu;
import se.raek.cdp1802.sim.Memory;

public class Tiny {

	public static class SimpleRom implements Memory {

		private final int[] values;

		public SimpleRom(String content) {
			int hexLength = content.length();
			if (hexLength % 2 != 0) {
				throw new IllegalArgumentException();
			}
			int byteLength = hexLength / 2;
			values = new int[byteLength];
			for (int i = 0; i < byteLength; i++) {
				int high = hexDigit(content.charAt(2 * i));
				int low = hexDigit(content.charAt(2 * i + 1));
				values[i] = (high << 4) | low;
			}
		}

		private static int hexDigit(char h) {
			if (h >= '0' && h <= '9') {
				return h - '0';
			} else if (h >= 'a' && h <= 'f') {
				return h - 'a' + 10;
			} else if (h >= 'A' && h <= 'F') {
				return h - 'A' + 10;
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public int read(int i) {
			return values[i];
		}

	}

	public static void main(String[] args) {
		final Cpu.State s = new Cpu.State();
		final Memory m = new SimpleRom("7BC47A3000");
		final Cpu cpu = new Cpu(s, m);

		final JFrame frame = new JFrame("1802");
		final JLabel qLabel = new JLabel("Q=0");
		final JLabel rpLabel = new JLabel("R(P)=0x0000");
		final JButton clockButton = new JButton("clock");

		clockButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cpu.tick();
				qLabel.setText(s.q ? "Q=1" : "Q=0");
				rpLabel.setText(String.format("R(P)=0x%04X", s.r[s.p]));
			}
		});
		frame.setLayout(new FlowLayout());
		frame.add(qLabel);
		frame.add(rpLabel);
		frame.add(clockButton);
		frame.pack();
		frame.setVisible(true);
	}

}
