package project;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;

public class AdminLogin {

	private JFrame frame;
	private JTextField adminNameField;
	private JPasswordField passwordField;
	private final String adminName = "Admin";
	private final String password = "1234";

	public AdminLogin() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(400, 300);
		frame.setTitle("Admin Login");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(new Color(240, 248, 255));
		frame.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		// Title
		JLabel titleLabel = new JLabel("Admin Login");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(new Color(70, 130, 180));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		frame.add(titleLabel, gbc);

		// Username label
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setForeground(new Color(60, 60, 60));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		frame.add(usernameLabel, gbc);

		// Username field
		adminNameField = new JTextField(15);
		adminNameField.setBackground(Color.WHITE);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		frame.add(adminNameField, gbc);

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
				if (adminNameField.getText().equals(adminName) &&
						password.equals(new String(passwordField.getPassword()))) {
					frame.dispose();
					AdminPanel ap = new AdminPanel();
				} else {
					JOptionPane.showMessageDialog(frame,
							"Incorrect Username or Password",
							"Login Failed",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		frame.add(loginButton, gbc);

		frame.setVisible(true);
	}
}