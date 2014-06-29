package se.raek.cdp1802.gui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import se.raek.cdp1802.sim.Cpu;
import se.raek.cdp1802.sim.Io;
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
		public int read(int addr) {
			return values[addr];
		}

	}

	public static class SimpleIo implements Io {

		private final int[] outputValues;

		public SimpleIo() {
			outputValues = new int[7];
		}

		@Override
		public void output(int n, int data) {
			outputValues[n - 1] = data;
		}

	}

	public static void main(String[] args) {
		new Tiny();
	}

	private Cpu.State s;
	private SimpleRom m;
	private SimpleIo io;
	private Cpu cpu;

	private JFrame frame;
	private JLabel[] outputLabels;
	private JPanel outputPanel;
	private JLabel dLabel;
	private JLabel qLabel;
	private JLabel rpLabel;
	private JButton clockButton;
	private JPanel restPanel;

	private static final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 14);

	public Tiny() {
		s = new Cpu.State();
		m = new SimpleRom("F8A57BC461016202630364046505660667077A3000");
		io = new SimpleIo();
		cpu = new Cpu(s, m, io);

		frame = new JFrame("1802");
		outputLabels = new JLabel[7];
		outputPanel = new JPanel();
		dLabel = makeLabel();
		qLabel = makeLabel();
		rpLabel = makeLabel();
		clockButton = new JButton("clock");
		restPanel = new JPanel();

		for (int i = 0; i < 7; i++) {
			outputLabels[i] = makeLabel();
		}

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		clockButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					cpu.tick();
				} catch (Cpu.InstructionNotImplementedException e) {
					JOptionPane.showMessageDialog(frame, e.getMessage());
					clockButton.setEnabled(false);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, String.format(
							"Caught %s: %s", e.getClass().getName(),
							e.getMessage()));
					clockButton.setEnabled(false);
				}
				updateGui();
			}
		});
		frame.setLayout(new GridLayout(1, 2));
		outputPanel.setLayout(new GridLayout(7, 1));
		for (int i = 0; i < 7; i++) {
			outputPanel.add(outputLabels[i]);
		}
		frame.add(outputPanel);
		restPanel.setLayout(new GridLayout(4, 1));
		restPanel.add(dLabel);
		restPanel.add(qLabel);
		restPanel.add(rpLabel);
		restPanel.add(clockButton);
		frame.add(restPanel);
		updateGui();
		frame.pack();
		frame.setVisible(true);
	}

	private JLabel makeLabel() {
		JLabel label = new JLabel();
		label.setFont(font);
		return label;
	}

	private void updateGui() {
		dLabel.setText(String.format("D=%02X", s.d));
		qLabel.setText(s.q ? "Q=1" : "Q=0");
		rpLabel.setText(String.format("R(P)=0x%04X", s.r[s.p]));
		for (int i = 0; i < 7; i++) {
			outputLabels[i].setText(String.format("OUT%d=0x%02X", i,
					io.outputValues[i]));
		}
	}

}
