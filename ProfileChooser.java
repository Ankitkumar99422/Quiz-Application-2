package project;

import project.AdminLogin;
import project.DataBase;
import project.UserLogin;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ProfileChooser {

	private JFrame frmQuizApplication;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProfileChooser window = new ProfileChooser();
					window.frmQuizApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ProfileChooser() {
		DataBase.dbInit();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmQuizApplication = new JFrame();
		frmQuizApplication.setSize(450, 350);
		frmQuizApplication.setTitle("Quiz Application");
		frmQuizApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmQuizApplication.setLocationRelativeTo(null);
		frmQuizApplication.getContentPane().setBackground(new Color(240, 248, 255));
		frmQuizApplication.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);

		// Welcome title
		JLabel lblWelcome = new JLabel("Welcome to Quiz Application");
		lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
		lblWelcome.setForeground(new Color(70, 130, 180));
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		frmQuizApplication.add(lblWelcome, gbc);

		// Instruction label
		JLabel lblInstruction = new JLabel("Please Select Login Profile:");
		lblInstruction.setFont(new Font("Arial", Font.PLAIN, 16));
		lblInstruction.setForeground(new Color(60, 60, 60));
		lblInstruction.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 15, 20, 15);
		frmQuizApplication.add(lblInstruction, gbc);

		// Reset insets for buttons
		gbc.insets = new Insets(10, 15, 10, 15);
		gbc.gridwidth = 1;

		// Admin button
		JButton btnAdmin = new JButton("Admin");
		btnAdmin.setBackground(new Color(70, 130, 180));
		btnAdmin.setForeground(Color.WHITE);
		btnAdmin.setFont(new Font("Arial", Font.BOLD, 16));
		btnAdmin.setPreferredSize(new java.awt.Dimension(150, 50));
		btnAdmin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frmQuizApplication.dispose();
				AdminLogin adminLogin = new AdminLogin();
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		frmQuizApplication.add(btnAdmin, gbc);

		// User button
		JButton btnUser = new JButton("User");
		btnUser.setBackground(new Color(70, 130, 180));
		btnUser.setForeground(Color.WHITE);
		btnUser.setFont(new Font("Arial", Font.BOLD, 16));
		btnUser.setPreferredSize(new java.awt.Dimension(150, 50));
		btnUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frmQuizApplication.dispose();
				UserLogin userLogin = new UserLogin();
			}
		});
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		frmQuizApplication.add(btnUser, gbc);

		frmQuizApplication.setVisible(true);
	}
}