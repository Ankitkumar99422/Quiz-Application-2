package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Register {
	private JFrame frame;
	private JTextField userIdField;
	private JTextField nameField;
	private JTextField emailField;
	private JPasswordField passwordField;

	public Register() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(400, 350);
		frame.setTitle("User Registration");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(new Color(240, 248, 255));
		frame.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel titleLabel = new JLabel("User Registration");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(new Color(70, 130, 180));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		frame.add(titleLabel, gbc);

		JLabel userIdLabel = new JLabel("User ID:");
		userIdLabel.setForeground(new Color(60, 60, 60));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		frame.add(userIdLabel, gbc);

		userIdField = new JTextField(15);
		userIdField.setBackground(Color.WHITE);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		frame.add(userIdField, gbc);

		JLabel nameLabel = new JLabel("Full Name:");
		nameLabel.setForeground(new Color(60, 60, 60));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		frame.add(nameLabel, gbc);

		nameField = new JTextField(15);
		nameField.setBackground(Color.WHITE);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		frame.add(nameField, gbc);

		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setForeground(new Color(60, 60, 60));
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		frame.add(emailLabel, gbc);

		emailField = new JTextField(15);
		emailField.setBackground(Color.WHITE);
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		frame.add(emailField, gbc);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setForeground(new Color(60, 60, 60));
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.EAST;
		frame.add(passwordLabel, gbc);

		passwordField = new JPasswordField(15);
		passwordField.setBackground(Color.WHITE);
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		frame.add(passwordField, gbc);

		JButton registerButton = new JButton("Register");
		registerButton.setBackground(new Color(70, 130, 180));
		registerButton.setForeground(Color.WHITE);
		registerButton.setFont(new Font("Arial", Font.BOLD, 14));
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleRegistration();
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		frame.add(registerButton, gbc);

		JButton backButton = new JButton("Back to Login");
		backButton.setBackground(new Color(60, 179, 113));
		backButton.setForeground(Color.WHITE);
		backButton.setFont(new Font("Arial", Font.BOLD, 14));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new UserLogin();
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		frame.add(backButton, gbc);

		frame.setVisible(true);
	}

	private void handleRegistration() {
		String userId = userIdField.getText().trim();
		String name = nameField.getText().trim();
		String email = emailField.getText().trim();
		String password = new String(passwordField.getPassword());

		// Data Validation
		if (userId.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Please fill in all fields!");
			return;
		}

		if (!isValidEmail(email)) {
			JOptionPane.showMessageDialog(frame, "Please enter a valid email address!");
			return;
		}

		if (!isValidPassword(password)) {
			JOptionPane.showMessageDialog(frame, "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number!");
			return;
		}

		// Hash the password
		String hashedPassword = hashPassword(password);

		try {
			// Sanitize input to prevent SQL injection (basic example)
			int userIdInt = Integer.parseInt(userId);
			name = sanitizeInput(name);
			email = sanitizeInput(email);

			DataBase.addUser(userIdInt, name, email, hashedPassword); // Store the hashed password
			JOptionPane.showMessageDialog(frame, "Registration successful!");
			frame.dispose();
			new UserLogin();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "User ID must be a valid number!");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, "Registration failed: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private boolean isValidEmail(String email) {
		String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private boolean isValidPassword(String password) {
		// Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	private String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = md.digest(password.getBytes());

			// Convert byte array to a hexadecimal string
			StringBuilder sb = new StringBuilder();
			for (byte b : hashBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(frame, "Hashing algorithm not found!");
			e.printStackTrace();
			return null; // Or handle the error appropriately
		}
	}

	private String sanitizeInput(String input) {
		// Basic sanitization to prevent SQL injection (improve this for production)
		return input.replaceAll("[^a-zA-Z0-9\\s@\\.]", ""); // Allow alphanumeric, spaces, @, and .
	}
}
