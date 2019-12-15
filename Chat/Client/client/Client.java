package client;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import javax.swing.border.CompoundBorder;

import network.*;
import server.Server;

public class Client implements ActionListener, ConnectionListener, ItemListener {

	private JFrame frame;
	private JTextField textField;
	private static final String IP_ADDR = "127.0.0.1";
	private static final int PORT = 5555;
	private Connection connection;
	private JTextArea textArea;
	public BufferedImage img = null;
	public static String userName;
	private int txtGroesse = 12;
	private Font font;
	JFileChooser chooser;
	private Integer[] groesse = { 10, 12, 14, 16, 18, 20, 22, 24, 26 };
	JComboBox comboBox;
	int helpInt = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client(userName);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Client(String name) {
		userName = name;
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void actionPerformed(ActionEvent e) {
		String msg = textField.getText();
		if (msg.equals(""))
			return;
		textField.setText(null);
		connection.sendString(userName + ": " + msg);
	}

	@Override
	public void onConnectionReady(Connection tcpConnection) {
		printMsg("Connection ready...");

	}

	@Override
	public void onReceiveString(Connection tcpConnection, String value) {
		printMsg(value);
	}

	@Override
	public void onDisconnect(Connection tcpConnection) {
		printMsg("Connection close");

	}

	@Override
	public void onException(Connection tcpConnection, Exception e) {
		printMsg("Connection exception: " + e);
	}

	private synchronized void printMsg(String msg) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				textArea.append(msg + "\n");
				textArea.setCaretPosition(textArea.getDocument().getLength());
			}
		});
	}

	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 650, 388);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Edit");
		menuBar.add(mnNewMenu);

		JMenu mnEtwas = new JMenu("Benutzer registrieren");
		mnNewMenu.add(mnEtwas);

		JCheckBoxMenuItem userReg = new JCheckBoxMenuItem("neues Konto");
		mnEtwas.add(userReg);

		if (userName.equals("Admin")) {
			mnEtwas.setEnabled(true);
		} else {
			mnEtwas.setEnabled(false);
		}

		userReg.setState(false);
		userReg.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Registration nr = new Registration();
					nr.setVisible(true);
					nr.pack();
					nr.setLocationRelativeTo(null);

				} else {
				}
			}
		});

		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(panel,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1301, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 338, Short.MAX_VALUE).addContainerGap()));

		JLabel lblNewLabel = new JLabel(userName);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));

		textField = new JTextField();
		textField.setColumns(10);
		textField.addActionListener(this);

		final JButton cmdSend = new JButton("Send");
		cmdSend.addActionListener(this);
//		cmdSend.setBorder(new CompoundBorder());
		cmdSend.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JPopupMenu popUp = new JPopupMenu();
		popUp.setBackground(new java.awt.Color(0, 0, 0, 0));

		JButton btnFarbe = new JButton("Textfarbe");
		btnFarbe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Color txtColor = JColorChooser.showDialog(null, "title", Color.BLUE);
				textField.setForeground(txtColor);
				textArea.setForeground(txtColor);
			}
		});

		JButton btnFont = new JButton("Schrift");

		btnFont.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (helpInt == 0) {

					font = new Font("Arial Bold", Font.BOLD, txtGroesse);
					textField.setFont(font);
					textArea.setFont(font);
					helpInt = 1;
				} else if (helpInt == 1) {
					font = new Font("Arial", 0, txtGroesse);
					textField.setFont(font);
					textArea.setFont(font);
					helpInt = 0;
				}
			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();

		comboBox = new JComboBox(groesse);
		comboBox.setToolTipText("  ");
		comboBox.setFont(new Font("Comic Sans MS", Font.BOLD, 13));

		comboBox.setEditable(true);
		comboBox.addItemListener(this);

		JButton btnSignOut = new JButton("Sign out");
		btnSignOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		JLabel lblNewLabel_1 = null;
		/*
		 * it's not good
		 */
		ImageIcon imageIcon = new ImageIcon(
				new ImageIcon("your icon").getImage().getScaledInstance(135, 120, Image.SCALE_SMOOTH));

		lblNewLabel_1 = new JLabel(imageIcon);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createSequentialGroup().addContainerGap()
										.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
												.addComponent(btnSignOut, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
														128, Short.MAX_VALUE)
												.addComponent(btnFarbe, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
														128, Short.MAX_VALUE)
												.addGroup(gl_panel.createSequentialGroup()
														.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 58,
																GroupLayout.PREFERRED_SIZE)
														.addGap(7).addComponent(btnFont, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
										.addGap(18))
								.addGroup(gl_panel.createSequentialGroup().addGroup(gl_panel
										.createParallelGroup(Alignment.TRAILING)
										.addGroup(Alignment.LEADING,
												gl_panel.createSequentialGroup().addContainerGap().addComponent(
														lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
										.addGroup(gl_panel.createSequentialGroup().addGap(32).addComponent(lblNewLabel,
												GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)))
										.addGap(19)))
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, 296,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(cmdSend, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 463,
										GroupLayout.PREFERRED_SIZE))
						.addGap(682)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup()
						.addComponent(lblNewLabel).addGap(18)
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
						.addGap(18).addComponent(btnFarbe, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
						.addGap(15)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnFont, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup().addGap(1)
										.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
												.addComponent(cmdSend, GroupLayout.PREFERRED_SIZE, 57,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(textField, GroupLayout.DEFAULT_SIZE, 57,
														Short.MAX_VALUE)))
								.addGroup(gl_panel.createSequentialGroup().addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(btnSignOut, GroupLayout.PREFERRED_SIZE, 37,
												GroupLayout.PREFERRED_SIZE))))
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE))
				.addGap(32)));

		textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		textArea.setEditable(false);
		panel.setLayout(gl_panel);
		frame.setVisible(true);
		frame.getContentPane().setLayout(groupLayout);

		try {

			connection = new Connection(this, IP_ADDR, PORT);
		} catch (IOException e) {
			printMsg("Connection exception: " + e);
		} catch (ClassNotFoundException cnfe) {

		}
	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
		if (ie.getSource() == comboBox) {
			txtGroesse = (Integer) comboBox.getSelectedItem();
			font = new Font("Arial", 0, txtGroesse);
			textField.setFont(font);
			textArea.setFont(font);
		}
	}
}

