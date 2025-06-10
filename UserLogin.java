package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class UserLogin {
	private JFrame frame;
	private JTextField idField;
	private JPasswordField passwordField;

	public UserLogin() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(400, 300);
		frame.setTitle("User Login");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(new Color(240, 248, 255));
		frame.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		// Title
		JLabel titleLabel = new JLabel("User Login");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(new Color(70, 130, 180));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		frame.add(titleLabel, gbc);

		// User ID label
		JLabel idLabel = new JLabel("User ID:");
		idLabel.setForeground(new Color(60, 60, 60));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		frame.add(idLabel, gbc);

		// User ID field
		idField = new JTextField(15);
		idField.setBackground(Color.WHITE);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		frame.add(idField, gbc);

		// Password label
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setForeground(new Color(60, 60, 60));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		frame.add(passwordLabel, gbc);

		// Password field
		passwordField = new JPasswordField(15);
		passwordField.setBackground(Color.WHITE);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		frame.add(passwordField, gbc);

		// Login button
		JButton loginButton = new JButton("Login");
		loginButton.setBackground(new Color(70, 130, 180));
		loginButton.setForeground(Color.WHITE);
		loginButton.setFont(new Font("Arial", Font.BOLD, 14));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleLogin();
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		frame.add(loginButton, gbc);

		// Register button
		JButton registerButton = new JButton("Register");
		registerButton.setBackground(new Color(60, 179, 113));
		registerButton.setForeground(Color.WHITE);
		registerButton.setFont(new Font("Arial", Font.BOLD, 14));
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new Register();
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		frame.add(registerButton, gbc);

		frame.setVisible(true);
	}

	private void handleLogin() {
		String userId = idField.getText().trim();
		String password = new String(passwordField.getPassword());

		if (userId.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Please fill in all fields!");
			return;
		}

		try {
			if (DataBase.validatePassword(userId, password)) {
				JOptionPane.showMessageDialog(frame, "Login successful!");
				frame.dispose();
				new Quiz();
			} else {
				JOptionPane.showMessageDialog(frame, "Invalid credentials!");
				passwordField.setText("");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}