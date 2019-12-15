package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.Border;

public class Login {

	private JFrame frame;
	private JTextField usrField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
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
	public Login() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {

		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 435, 304);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// center the form
		frame.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(436, 360));
		panel.setMinimumSize(new Dimension(436, 360));
		panel.setBackground(Color.YELLOW);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 11, 416, 282);
		panel_1.setBackground(new Color(128, 128, 128));
		panel.add(panel_1);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 66, 417, 222);

		Border label_bord = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);

		JLabel lblLabel = new JLabel(" X");
		lblLabel.setBounds(387, 11, 20, 17);
		lblLabel.setBorder(label_bord);
		lblLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				Border label_bord = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white);
				lblLabel.setForeground(Color.white);
				lblLabel.setBorder(label_bord);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				Border label_bord = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);
				lblLabel.setForeground(Color.black);
				lblLabel.setBorder(label_bord);
			}
		});
		lblLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel label = new JLabel(" -");
		label.setBounds(364, 11, 17, 17);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Border label_bord = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white);
				label.setForeground(Color.white);
				label.setBorder(label_bord);
			}

			public void mouseExited(MouseEvent e) {
				Border label_bord = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);
				label.setForeground(Color.black);
				label.setBorder(label_bord);
			}

			@Override
			public void mouseClicked(MouseEvent me) {
				frame.setState(JFrame.ICONIFIED);
			}
		});
		label.setPreferredSize(new Dimension(6, 14));
		label.setMinimumSize(new Dimension(6, 14));
		label.setMaximumSize(new Dimension(6, 14));
		label.setBorder(label_bord);
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_2.setLayout(null);

		JLabel usr = new JLabel("Username:");
		usr.setFont(new Font("Tahoma", Font.BOLD, 13));
		usr.setBounds(10, 29, 79, 26);
		panel_2.add(usr);

		JLabel psw = new JLabel("Password:");
		psw.setFont(new Font("Tahoma", Font.BOLD, 13));
		psw.setBounds(10, 82, 79, 29);
		panel_2.add(psw);

		usrField = new JTextField();
		usrField.setBounds(96, 28, 206, 30);
		usrField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent ev) {

				if (usrField.getText().trim().toLowerCase().equals("username")) {
					usrField.setText("");
					usrField.setForeground(Color.black);
				}

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (usrField.getText().trim().equals("")
						|| usrField.getText().trim().toLowerCase().equals("username")) {
					usrField.setText("username");
					usrField.setForeground(Color.lightGray);
				}

			}
		});
		usrField.setFont(new Font("Tahoma", Font.BOLD, 12));
		usrField.setForeground(Color.BLACK);
		panel_2.add(usrField);
		usrField.setColumns(10);

		JButton login = new JButton("Login");
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				PreparedStatement st;
				ResultSet rs;

				// get the username & password
				String username = usrField.getText();
				String password = String.valueOf(passwordField.getPassword());

				// create a select query to check if the username and the password exist int the
				// database
				String query = "SELECT * FROM `users` WHERE `username` = ? AND `password` = ?";
				try {
					st = ForDB.getConnection().prepareStatement(query);

					st.setString(1, username);
					st.setString(2, password);

					rs = st.executeQuery();
					if (rs.next()) {
						// show a new form

						Client clientChat = new Client(username);

						// close the current form (login form)
						frame.dispose();
					} else {
						// error message
						JOptionPane.showMessageDialog(null, "Invalid Username / Password", "Login Error", 2);
					}

				} catch (SQLException e) {
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		});
		login.setBounds(214, 127, 88, 41);
		panel_2.add(login);

		passwordField = new JPasswordField("password");
		passwordField.setBounds(96, 82, 206, 30);
		passwordField.addFocusListener(new FocusAdapter() {

			public void focusGained(FocusEvent ev) {

				String pass = String.valueOf(passwordField.getPassword());

				if (pass.trim().toLowerCase().equals("password")) {
					passwordField.setText("");
					passwordField.setForeground(Color.black);
				}
			}

			@Override
			public void focusLost(FocusEvent fe) {

				String pass = String.valueOf(passwordField.getPassword());
				if (pass.equals("") || pass.trim().toLowerCase().equals("password")) {
					passwordField.setText("password");
					passwordField.setForeground(Color.LIGHT_GRAY);
				}
			}
		});
		passwordField.setFont(new Font("Tahoma", Font.BOLD, 12));
		passwordField.setForeground(Color.LIGHT_GRAY);
		panel_2.add(passwordField);

		JButton cancel = new JButton("Cancel");
		cancel.setBounds(96, 127, 91, 41);
		panel_2.add(cancel);

		JLabel ForgotPsw = new JLabel("Forgot your password?");
		ForgotPsw.setBounds(267, 193, 125, 14);
		ForgotPsw.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent me) {
				ForgotPsw.setForeground(Color.orange);
			}

			public void mouseExited(MouseEvent me) {
				ForgotPsw.setForeground(Color.blue);
			}

			@Override
			public void mouseClicked(MouseEvent evt) {
				JOptionPane.showMessageDialog(null, "Es tut mir leid :( ");

			}
		});

		ForgotPsw.setFont(new Font("Tahoma", Font.PLAIN, 9));
		ForgotPsw.setForeground(Color.BLUE);
		panel_2.add(ForgotPsw);

		JCheckBox showPass = new JCheckBox("Show Pass");
		showPass.setFont(new Font("Tahoma", Font.BOLD, 10));
		showPass.setBounds(308, 82, 81, 30);
		showPass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (showPass.isSelected()) {
					passwordField.setEchoChar((char) 0);
				} else {
					passwordField.setEchoChar('*');
				}

			}
		});
		panel_2.add(showPass);
		panel_1.setLayout(null);
		panel_1.add(label);
		panel_1.add(lblLabel);
		panel_1.add(panel_2);

		JLabel HauptLabel = new JLabel(" Messenger");
		HauptLabel.setBounds(135, 18, 105, 37);
		panel_1.add(HauptLabel);
		HauptLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

	}

}
