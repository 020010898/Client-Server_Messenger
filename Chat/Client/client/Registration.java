package client;

import javax.swing.*;
import java.awt.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.*;

//import de.myPro.chatserver.ClientChat;

import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Registration extends JFrame {

	// create a variable to set the image path in it
	String image_path = null;
	private JFrame frame;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField nutzernameField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registration window = new Registration();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// create a function to verify the empty fields
	private boolean verifyFields() {
		String fname = nutzernameField.getText();
		String vname = textField_3.getText();
		String nname = textField_2.getText();
		String pass = String.valueOf(passwordField.getPassword());
		String pass2 = String.valueOf(passwordField_1.getPassword());

		// check empty fields
		if (fname.trim().equals("") || vname.trim().equals("") || nname.trim().equals("") || pass.trim().equals("")
				|| pass2.trim().equals("")) {
			JOptionPane.showMessageDialog(null, "Ein oder mehrere Felder sind leer", "Leerfelder", 2);
			return false;
		}
		// check if two passwords are equals
		else if (!pass.equals(pass2)) {
			JOptionPane.showMessageDialog(null, "Passwörter stimmen nicht ", "Passwort bestätigen", 2);
			return false;
		} else if (pass.length() < 6 || pass2.length() < 6) {
			JOptionPane.showMessageDialog(null, "Kennwort ist zu kurz", "Du kannst besser", 2);
			return false;
		} else {
			return true;
		}
	}

	// create a function to check if the entered username is already exist in db
	private boolean checkUsername(String username) {
		PreparedStatement st;
		ResultSet rs;
		boolean username_exist = false;
		String query = "SELECT * FROM `users` WHERE `username` = ?";

		try {
			st = ForDB.getConnection().prepareStatement(query);
			st.setString(1, username);
			rs = st.executeQuery();

			if (rs.next()) {
				username_exist = true;
				JOptionPane.showMessageDialog(null,
						"der Benutzername ist schon vorhanden, musst du dir einen anderen überlegen",
						"falscher Benutzername", 2);
			}
		} catch (SQLException e) {
			Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, e);
		}

		return username_exist;
	}

	/**
	 * Create the application.
	 */
	public Registration() {

		JPanel panel = new JPanel();

		getContentPane().add(panel, BorderLayout.CENTER);

		JLabel nutzername = new JLabel("Nutzername");
		JLabel nachname = new JLabel("Vorname");
		JLabel vorname = new JLabel("Nachname");
		JLabel password = new JLabel("Passwort");
		JLabel password2 = new JLabel("Best\u00E4tigen");
		JButton abbrechen = new JButton("Abbrechen");
		JButton ausfuehren = new JButton("Go");

		abbrechen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});
		ausfuehren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nutzername = nutzernameField.getText();
				String vname = textField_3.getText();
				String nname = textField_2.getText();
				String pass1 = String.valueOf(passwordField.getPassword());
				String pass2 = String.valueOf(passwordField_1.getPassword());

				if (verifyFields()) {
					if (!checkUsername(nutzername)) {
						PreparedStatement ps;
						ResultSet rs;
						String registerUserQuery = "INSERT INTO `users`(`username`, `Vorname`, `Nachname`, `password`, `picture`) VALUES (?, ?, ?, ?, ?)";
						InputStream image;
						try {
							ps = ForDB.getConnection().prepareStatement(registerUserQuery);
							ps.setString(1, nutzername);
							ps.setString(2, vname);
							ps.setString(3, nname);
							ps.setString(4, pass1);

							// save the image as blob in the dbase

							if (image_path != null) {
								try {
									image = new FileInputStream(new File(image_path));
									ps.setBlob(5, image);
								} catch (FileNotFoundException e1) {
									e1.printStackTrace();
								}
							}

							if (ps.executeUpdate() != 0) {
								JOptionPane.showMessageDialog(null, "Your Account Has Been Created");
								dispose();

							} else {
								JOptionPane.showMessageDialog(null, "Error: Check Your Information");
							}
						} catch (SQLException e1) {
							System.out.println("SQLEx");
							Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, e);
						}
					}
				}
			}

		});

		textField_2 = new JTextField();
		textField_2.setColumns(10);

		textField_3 = new JTextField();
		textField_3.setColumns(10);

		nutzernameField = new JTextField();
		nutzernameField.setColumns(10);

		JLabel lblImage = new JLabel("Image :");
		JLabel lblImagePath = new JLabel("image path");

		JButton btnSelectImage = new JButton("Select image");
		btnSelectImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// select an image and set the image path into a jlabel
				String path = null;
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

				// file extension

				FileNameExtensionFilter extension = new FileNameExtensionFilter("*Image", "jpg", "png", "jpeg");
				chooser.addChoosableFileFilter(extension);

				int filestate = chooser.showSaveDialog(null);

				// check if the user select an image
				if (filestate == JFileChooser.APPROVE_OPTION) {
					File selectedImage = chooser.getSelectedFile();
					path = selectedImage.getAbsolutePath();
					lblImagePath.setText(path);

					image_path = path;

				}
			}
		});

		passwordField = new JPasswordField();

		passwordField_1 = new JPasswordField();

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(17)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(nachname)
								.addComponent(nutzername, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
								.addComponent(vorname).addComponent(password)
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addComponent(lblImage)
										.addComponent(password2)))
						.addGap(28)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(passwordField_1, GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
								.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
								.addComponent(textField_2, 288, 288, Short.MAX_VALUE)
								.addComponent(textField_3, 288, 288, Short.MAX_VALUE)
								.addComponent(
										nutzernameField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 288,
										Short.MAX_VALUE)
								.addGroup(gl_panel.createSequentialGroup().addGap(4).addGroup(gl_panel
										.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel.createSequentialGroup().addComponent(abbrechen).addGap(68)
												.addComponent(ausfuehren))
										.addGroup(gl_panel.createSequentialGroup().addComponent(btnSelectImage)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblImagePath,
														GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)))))
						.addGap(27)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addGap(37)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(nutzernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(nutzername))
				.addGap(18)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(nachname))
				.addGap(21)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(vorname))
				.addGap(18)
				.addGroup(gl_panel
						.createParallelGroup(Alignment.BASELINE).addComponent(password).addComponent(passwordField,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_panel
						.createParallelGroup(Alignment.BASELINE).addComponent(password2).addComponent(passwordField_1,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(33)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblImage)
						.addComponent(btnSelectImage).addComponent(lblImagePath))
				.addGap(24).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(abbrechen)
						.addComponent(ausfuehren))
				.addContainerGap(40, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 550, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

	}
}
