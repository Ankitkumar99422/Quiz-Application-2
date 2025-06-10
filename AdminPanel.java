package project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

enum Mode {
	DATABASE, FILE
}

public class AdminPanel extends JFrame {
	private Mode currentMode = Mode.DATABASE;
	private JComboBox<String> modeSelector;

	private JTextField option1Field;
	private JTextField option2Field;
	private JTextField option3Field;
	private JTextField option4Field;
	private JTextField answerField;
	private JTextField remIDfield;
	private JTextArea queTextArea;
	private JComboBox<String> comboBox;

	// Color scheme
	private final Color PRIMARY_COLOR = new Color(70, 130, 180);      // Steel Blue
	private final Color SECONDARY_COLOR = new Color(135, 206, 250);   // Light Sky Blue
	private final Color ACCENT_COLOR = new Color(255, 99, 71);        // Tomato
	private final Color SUCCESS_COLOR = new Color(60, 179, 113);      // Medium Sea Green
	private final Color BACKGROUND_COLOR = new Color(245, 248, 250);  // Light Blue Gray
	private final Color TEXT_COLOR = new Color(47, 79, 79);           // Dark Slate Gray

	public AdminPanel() {
		setTitle("Quiz Admin Panel");
		setSize(900, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Set background color
		getContentPane().setBackground(BACKGROUND_COLOR);

		createComponents();
		setVisible(true);
	}

	private void createComponents() {
		// Top panel for title, mode selector, and delete section
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
		topPanel.setBackground(PRIMARY_COLOR);
		topPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

		JLabel titleLabel = new JLabel("Quiz Admin Panel");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
		titleLabel.setForeground(Color.WHITE);
		topPanel.add(titleLabel);

		// Add separator
		JSeparator separator1 = new JSeparator(SwingConstants.VERTICAL);
		separator1.setPreferredSize(new Dimension(2, 30));
		separator1.setForeground(Color.WHITE);
		topPanel.add(separator1);

		// Mode selector
		JLabel modeLabel = new JLabel("Mode:");
		modeLabel.setFont(new Font("Arial", Font.BOLD, 14));
		modeLabel.setForeground(Color.WHITE);
		topPanel.add(modeLabel);

		String[] modes = {"Database", "File"};
		modeSelector = new JComboBox<>(modes);
		modeSelector.setSelectedIndex(0);
		modeSelector.setFont(new Font("Arial", Font.PLAIN, 12));
		modeSelector.setPreferredSize(new Dimension(100, 25));
		modeSelector.addActionListener(e -> {
			String selected = (String) modeSelector.getSelectedItem();
			if ("File".equals(selected)) {
				currentMode = Mode.FILE;
			} else {
				currentMode = Mode.DATABASE;
			}
		});
		topPanel.add(modeSelector);

		// Add separator
		JSeparator separator2 = new JSeparator(SwingConstants.VERTICAL);
		separator2.setPreferredSize(new Dimension(2, 30));
		separator2.setForeground(Color.WHITE);
		topPanel.add(separator2);

		// Delete section
		JLabel deleteLabel = new JLabel("Delete:");
		deleteLabel.setFont(new Font("Arial", Font.BOLD, 14));
		deleteLabel.setForeground(Color.WHITE);
		topPanel.add(deleteLabel);

		comboBox = new JComboBox<>();
		comboBox.addItem("users");
		comboBox.addItem("question");
		comboBox.setFont(new Font("Arial", Font.PLAIN, 12));
		comboBox.setPreferredSize(new Dimension(100, 25));
		topPanel.add(comboBox);

		remIDfield = new JTextField(10);
		remIDfield.setFont(new Font("Arial", Font.PLAIN, 12));
		remIDfield.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));
		topPanel.add(remIDfield);

		JButton deleteBtn = createStyledButton("Delete", ACCENT_COLOR);
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleDelete();
			}
		});
		topPanel.add(deleteBtn);

		add(topPanel, BorderLayout.NORTH);

		// Center panel for question form
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(BACKGROUND_COLOR);
		centerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.anchor = GridBagConstraints.WEST;

		// Question label and text area
		gbc.gridx = 0; gbc.gridy = 0;
		JLabel questionLabel = new JLabel("Question:");
		questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
		questionLabel.setForeground(TEXT_COLOR);
		centerPanel.add(questionLabel, gbc);

		gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
		queTextArea = new JTextArea(4, 35);
		queTextArea.setLineWrap(true);
		queTextArea.setWrapStyleWord(true);
		queTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		queTextArea.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
				BorderFactory.createEmptyBorder(8, 8, 8, 8)
		));
		JScrollPane questionScroll = new JScrollPane(queTextArea);
		questionScroll.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 2));
		centerPanel.add(questionScroll, gbc);

		// Options
		String[] optionLabels = {"Option 1:", "Option 2:", "Option 3:", "Option 4:"};
		JTextField[] optionFields = {option1Field, option2Field, option3Field, option4Field};

		for (int i = 0; i < 4; i++) {
			gbc.gridx = 0; gbc.gridy = i + 1; gbc.fill = GridBagConstraints.NONE;
			JLabel optionLabel = new JLabel(optionLabels[i]);
			optionLabel.setFont(new Font("Arial", Font.BOLD, 14));
			optionLabel.setForeground(TEXT_COLOR);
			centerPanel.add(optionLabel, gbc);

			gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
			optionFields[i] = new JTextField(35);
			optionFields[i].setFont(new Font("Arial", Font.PLAIN, 12));
			optionFields[i].setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
					BorderFactory.createEmptyBorder(8, 8, 8, 8)
			));
			centerPanel.add(optionFields[i], gbc);
		}

		// Assign to instance variables
		option1Field = optionFields[0];
		option2Field = optionFields[1];
		option3Field = optionFields[2];
		option4Field = optionFields[3];

		// Answer
		gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
		JLabel answerLabel = new JLabel("Correct Answer:");
		answerLabel.setFont(new Font("Arial", Font.BOLD, 14));
		answerLabel.setForeground(TEXT_COLOR);
		centerPanel.add(answerLabel, gbc);

		gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
		answerField = new JTextField(35);
		answerField.setFont(new Font("Arial", Font.PLAIN, 12));
		answerField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(SUCCESS_COLOR, 2),
				BorderFactory.createEmptyBorder(8, 8, 8, 8)
		));
		centerPanel.add(answerField, gbc);

		// Add Question button
		gbc.gridx = 1; gbc.gridy = 6; gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(20, 8, 8, 8);
		JButton addButton = createStyledButton("Add Question", SUCCESS_COLOR);
		addButton.setPreferredSize(new Dimension(150, 40));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAddQuestion();
			}
		});
		centerPanel.add(addButton, gbc);

		add(centerPanel, BorderLayout.CENTER);

		// Right panel for action buttons
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBackground(BACKGROUND_COLOR);
		rightPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
						"Actions",
						0, 0,
						new Font("Arial", Font.BOLD, 16),
						PRIMARY_COLOR
				),
				new EmptyBorder(15, 15, 15, 15)
		));

		JButton viewQuestionsBtn = createStyledButton("View Questions", SECONDARY_COLOR);
		viewQuestionsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		viewQuestionsBtn.setMaximumSize(new Dimension(150, 35));
		viewQuestionsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAllQuestions();
			}
		});
		rightPanel.add(viewQuestionsBtn);

		rightPanel.add(Box.createVerticalStrut(15));

		JButton logoutBtn = createStyledButton("Logout", new Color(255, 165, 0)); // Orange
		logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		logoutBtn.setMaximumSize(new Dimension(150, 35));
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new AdminLogin();
			}
		});
		rightPanel.add(logoutBtn);

		rightPanel.add(Box.createVerticalStrut(15));

		JButton exitBtn = createStyledButton("Exit", new Color(220, 20, 60)); // Crimson
		exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitBtn.setMaximumSize(new Dimension(150, 35));
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		rightPanel.add(exitBtn);

		add(rightPanel, BorderLayout.EAST);
	}

	private Question extractQuestionFromFields() {
		// Generate an ID or use 0 for auto-generation
		int id = 0; // Let the system auto-generate or handle ID assignment
		String question = queTextArea.getText().trim();
		String a = option1Field.getText().trim();
		String b = option2Field.getText().trim();
		String c = option3Field.getText().trim();
		String d = option4Field.getText().trim();
		String correct = answerField.getText().trim();
		return new Question(id, question, a, b, c, d, correct);
	}

	private JButton createStyledButton(String text, Color backgroundColor) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 12));
		button.setBackground(backgroundColor);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Add hover effect
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(backgroundColor.brighter());
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(backgroundColor);
			}
		});

		return button;
	}

	private void handleAddQuestion() {
		try {
			String[] options = {
					option1Field.getText(),
					option2Field.getText(),
					option3Field.getText(),
					option4Field.getText()
			};

			if (currentMode == Mode.DATABASE) {
				DataBase.addQuestion(queTextArea.getText(), options, answerField.getText());
			} else {
				// Use file-based storage - create Question object with extracted data
				Question q = extractQuestionFromFields();
				FileQuestionDAO.addQuestion(q);
			}

			clearForm();

			// Custom success dialog
			JOptionPane optionPane = new JOptionPane(
					"Question added successfully!",
					JOptionPane.INFORMATION_MESSAGE
			);
			JDialog dialog = optionPane.createDialog(this, "Success");
			dialog.setVisible(true);

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Failed to add question: " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Failed to add question: " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private void handleDelete() {
		String idText = remIDfield.getText().trim();
		String selectedType = (String) comboBox.getSelectedItem();

		if (idText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter an ID to delete!",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int result = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete this " + selectedType + " with ID: " + idText + "?",
				"Confirm Deletion", JOptionPane.YES_NO_OPTION);

		if (result != JOptionPane.YES_OPTION) return;

		try {
			if ("question".equals(selectedType)) {
				try {
					int questionId = Integer.parseInt(idText);
					if (currentMode == Mode.DATABASE) {
						DataBase.delete(String.valueOf(questionId), selectedType);
					} else {
						FileQuestionDAO.deleteQuestion(questionId);
					}
				} catch (NumberFormatException ex) {
					if (currentMode == Mode.DATABASE) {
						DataBase.delete(idText, selectedType);
					} else {
						throw new Exception("Invalid question ID format for file mode");
					}
				}
			} else {
				if (currentMode == Mode.DATABASE) {
					DataBase.delete(idText, selectedType);
				} else {
					throw new Exception("User deletion not supported in file mode");
				}
			}

			remIDfield.setText("");
			JOptionPane.showMessageDialog(this, selectedType + " deleted successfully!");

		} catch (SQLException ex) {
			String errorMsg = ex.getMessage();
			if (errorMsg.contains("not found") || errorMsg.contains("does not exist")) {
				JOptionPane.showMessageDialog(this, "No " + selectedType + " found with ID: " + idText,
						"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Failed to delete " + selectedType + ": " + errorMsg,
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			ex.printStackTrace();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private void showAllQuestions() {
		JFrame frame = new JFrame("Questions Database");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(700, 600);
		frame.setLocationRelativeTo(this);
		frame.getContentPane().setBackground(BACKGROUND_COLOR);

		try {
			ArrayList<Question> questions;
			if (currentMode == Mode.DATABASE) {
				questions = DataBase.getQuestionAnsWithIds();
			} else {
				// Convert List to ArrayList
				questions = new ArrayList<>(FileQuestionDAO.getAllQuestions());
			}

			JTextArea qTextArea = new JTextArea();
			qTextArea.setFont(new Font("Arial", Font.PLAIN, 13));
			qTextArea.setLineWrap(true);
			qTextArea.setWrapStyleWord(true);
			qTextArea.setEditable(false);
			qTextArea.setBackground(Color.WHITE);
			qTextArea.setBorder(new EmptyBorder(15, 15, 15, 15));

			JScrollPane scroll = new JScrollPane(qTextArea);
			scroll.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));

			StringBuilder content = new StringBuilder();
			int questionNum = 1;

			for (Question question : questions) {
				content.append("Question ").append(questionNum++).append(" [ID: ").append(question.getId()).append("]: ")
						.append(question.getQuestion()).append("\n\n")
						.append("A) ").append(question.getOptionA()).append("\n")
						.append("B) ").append(question.getOptionB()).append("\n")
						.append("C) ").append(question.getOptionC()).append("\n")
						.append("D) ").append(question.getOptionD()).append("\n\n")
						.append("Correct Answer: ").append(question.getCorrectOption()).append("\n")
						.append("========================================\n\n");
			}

			if (content.length() == 0) {
				content.append("No questions found in the database.");
			}

			qTextArea.setText(content.toString());
			qTextArea.setCaretPosition(0);

			frame.add(scroll);
			frame.setVisible(true);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Failed to load questions: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to load questions: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void clearForm() {
		queTextArea.setText("");
		option1Field.setText("");
		option2Field.setText("");
		option3Field.setText("");
		option4Field.setText("");
		answerField.setText("");
	}
}