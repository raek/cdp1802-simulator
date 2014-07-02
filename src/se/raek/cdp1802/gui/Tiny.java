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
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import se.raek.cdp1802.sim.Cpu;
import se.raek.cdp1802.sim.Io;
import se.raek.cdp1802.sim.Memory;
import se.raek.cdp1802.util.ArrayMemory;
import se.raek.cdp1802.util.MemoryMapper;
import se.raek.cdp1802.util.MemoryWriteProtector;
import se.raek.cdp1802.util.Utils;

public final class Tiny {

	private final class SimpleIo implements Io {

		public final int[] outputValues;
		public final int[] inputValues;

		public SimpleIo() {
			outputValues = new int[7];
			inputValues = new int[7];
			for (int i = 0; i < 7; i++) {
				inputValues[i] = ((i + 1) << 4) | (i + 1);
			}
		}

		@Override
		public void output(int n, int data) {
			outputValues[n - 1] = data;
		}

		@Override
		public int input(int n) {
			return inputValues[n - 1];
		}

	}

	private final class InputListener implements DocumentListener {

		private final int i;
		private final JTextField field;

		public InputListener(int i, JTextField field) {
			this.i = i;
			this.field = field;
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			parse();

		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			parse();

		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			parse();
		}

		private void parse() {
			try {
				int value = Utils.hexByteFromString(field.getText());
				io.inputValues[i] = value;
				field.setForeground(Color.BLACK);
			} catch (IllegalArgumentException e) {
				field.setForeground(Color.RED);
			}
		}

	}

	public static void main(String[] args) {
		if (args.length == 0) {
			new Tiny(testProgram);
		} else if (args.length == 1) {
			new Tiny(args[0]);
		} else {
			System.out.println("got too many arguments");
			System.exit(1);
		}
	}

	private Cpu.State s;
	private Memory m;
	private SimpleIo io;
	private Cpu cpu;

	private JFrame frame;
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JLabel[] rLabels;
	private JLabel[] outputLabels;
	private JTextField[] inputFields;
	private JPanel[] inputPanels;
	private JLabel dLabel;
	private JLabel qLabel;
	private JLabel idleLabel;
	private JLabel pLabel;
	private JLabel xLabel;
	private JButton clockButton;

	private Map<JLabel, String> currentLabelTexts = new HashMap<JLabel, String>();

	private static final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 14);

	private static final String testProgram = "EDF801BCBDF800ACADF8A15DF81AF1F885F958F8D05DDD2DF85A7360F800F0F800722DF8A55CF8000CF8004C2C2B1BF812BFF834AF9FBE8FAE7BC469612D6A622D6B632D6C642D6D652D6E662D6F672D7A3000";

	public Tiny(String program) {
		s = new Cpu.State();
		{
			Memory rom = new ArrayMemory(0x100);
			Utils.loadHex(rom, program);
			Memory ram = new ArrayMemory(0x100);
			MemoryMapper mm = new MemoryMapper();
			mm.map(0x0000, 0x100, new MemoryWriteProtector(rom));
			mm.map(0x0100, 0x100, ram);
			m = mm;
		}
		io = new SimpleIo();
		cpu = new Cpu(s, m, io);

		frame = new JFrame("1802");
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		rLabels = new JLabel[16];
		outputLabels = new JLabel[7];
		inputFields = new JTextField[7];
		inputPanels = new JPanel[7];
		dLabel = makeLabel();
		qLabel = makeLabel();
		idleLabel = makeLabel();
		pLabel = makeLabel();
		xLabel = makeLabel();
		clockButton = new JButton("clock");

		for (int i = 0; i < 16; i++) {
			rLabels[i] = makeLabel();
		}
		for (int i = 0; i < 7; i++) {
			outputLabels[i] = makeLabel();
		}
		for (int i = 0; i < 7; i++) {
			JTextField field = makeInputField(i);
			inputFields[i] = field;
			inputPanels[i] = labelField(String.format("INP%d=", i + 1), field);
		}

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getRootPane().setDefaultButton(clockButton);
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
		frame.setLayout(new GridLayout(1, 3, 20, 0));
		panel1.setLayout(new GridLayout(16, 1, 0, 0));
		for (int i = 0; i < 16; i++) {
			panel1.add(rLabels[i]);
		}
		frame.add(panel1);
		panel2.setLayout(new GridLayout(10, 1, 0, 0));
		for (int i = 0; i < 7; i++) {
			panel2.add(outputLabels[i]);
		}
		panel2.add(dLabel);
		panel2.add(qLabel);
		panel2.add(idleLabel);
		frame.add(panel2);
		panel3.setLayout(new GridLayout(10, 1, 0, 0));
		for (int i = 0; i < 7; i++) {
			panel3.add(inputPanels[i]);
		}
		panel3.add(pLabel);
		panel3.add(xLabel);
		panel3.add(clockButton);
		frame.add(panel3);
		updateGui();
		frame.pack();
		clockButton.requestFocusInWindow();
		frame.setVisible(true);
	}

	private JLabel makeLabel() {
		JLabel label = new JLabel();
		label.setFont(font);
		return label;
	}

	private JTextField makeInputField(int i) {
		JTextField field = new JTextField(String.format("%02X",
				io.inputValues[i]), 2);
		field.setFont(font);
		field.getDocument().addDocumentListener(new InputListener(i, field));
		return field;
	}

	private JPanel labelField(String text, JTextField field) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(text);
		label.setFont(font);
		panel.add(label);
		panel.add(field);
		return panel;

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
		setLabelText(idleLabel, s.idle ? "idle" : "run");
		setLabelText(pLabel, String.format("P=%X", s.p));
		setLabelText(xLabel, String.format("X=%X", s.x));
	}

}
