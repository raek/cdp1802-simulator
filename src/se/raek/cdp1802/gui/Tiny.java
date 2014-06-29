package se.raek.cdp1802.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

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
	private JPanel panel1;
	private JPanel panel2;
	private JLabel[] rLabels;
	private JLabel[] outputLabels;
	private JLabel dLabel;
	private JLabel qLabel;
	private JLabel pLabel;
	private JLabel xLabel;
	private JButton clockButton;

	private Map<JLabel, String> currentLabelTexts = new HashMap<JLabel, String>();

	private static final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 14);

	private static final String program = "ED2D1DF812BFF834AF9FBE8FAEE07BC461016202630364046505660667077A3000";

	public Tiny() {
		s = new Cpu.State();
		m = new SimpleRom(program);
		io = new SimpleIo();
		cpu = new Cpu(s, m, io);

		frame = new JFrame("1802");
		panel1 = new JPanel();
		panel2 = new JPanel();
		rLabels = new JLabel[16];
		outputLabels = new JLabel[7];
		dLabel = makeLabel();
		qLabel = makeLabel();
		pLabel = makeLabel();
		xLabel = makeLabel();
		clockButton = new JButton("clock");

		for (int i = 0; i < 16; i++) {
			rLabels[i] = makeLabel();
		}
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
		frame.setLayout(new GridLayout(1, 2, 20, 0));
		panel1.setLayout(new GridLayout(16, 1, 0, 0));
		for (int i = 0; i < 16; i++) {
			panel1.add(rLabels[i]);
		}
		frame.add(panel1);
		panel2.setLayout(new GridLayout(12, 1, 0, 0));
		for (int i = 0; i < 7; i++) {
			panel2.add(outputLabels[i]);
		}
		panel2.add(dLabel);
		panel2.add(qLabel);
		panel2.add(pLabel);
		panel2.add(xLabel);
		panel2.add(clockButton);
		frame.add(panel2);
		updateGui();
		frame.pack();
		frame.setVisible(true);
	}

	private JLabel makeLabel() {
		JLabel label = new JLabel();
		label.setFont(font);
		return label;
	}

	private void setLabelText(JLabel label, String newText) {
		String oldText = currentLabelTexts.get(label);
		Color color;
		if (oldText == null || oldText.equals(newText)) {
			color = Color.BLACK;
		} else {
			color = Color.RED;
		}
		label.setText(newText);
		label.setForeground(color);
		currentLabelTexts.put(label, newText);
	}

	private void updateGui() {
		for (int i = 0; i < 16; i++) {
			setLabelText(rLabels[i], String.format("R(%X)=%04X", i, s.r[i]));
		}
		for (int i = 0; i < 7; i++) {
			setLabelText(outputLabels[i],
					String.format("OUT%d=%02X", i + 1, io.outputValues[i]));
		}
		setLabelText(dLabel, String.format("D=%02X", s.d));
		setLabelText(qLabel, s.q ? "Q=1" : "Q=0");
		setLabelText(pLabel, String.format("P=%X", s.p));
		setLabelText(xLabel, String.format("X=%X", s.x));
	}

}
