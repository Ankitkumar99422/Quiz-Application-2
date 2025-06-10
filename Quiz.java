package project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

public class Quiz extends JFrame {
	ArrayList<Question> questions = null;
	private int count = 0;
	private int score = 0;

	// UI Components
	private JTextArea queTextArea;
	private JRadioButton[] optionButtons;
	private ButtonGroup bg;
	private JLabel timerLabel;
	private JLabel progressLabel;
	private JProgressBar progressBar;
	private JButton btnNext;
	private Timer countdownTimer;

	// Color Scheme
	private final Color PRIMARY_COLOR = new Color(74, 144, 226);
	private final Color SECONDARY_COLOR = new Color(52, 73, 94);
	private final Color ACCENT_COLOR = new Color(46, 204, 113);
	private final Color DANGER_COLOR = new Color(231, 76, 60);
	private final Color BACKGROUND_COLOR = new Color(245, 247, 250);
	private final Color CARD_COLOR = Color.WHITE;

	public Quiz() {
		initializeFrame();
		createComponents();
		loadQuestions();
		startTimer(600);
		setVisible(true);
	}

	private void initializeFrame() {
		setTitle("Quiz Application");
		setSize(900, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setBackground(BACKGROUND_COLOR);

		// Custom icon (you can replace with your own)
		try {
			setIconImage(createQuizIcon());
		} catch (Exception e) {
			// Continue without icon if creation fails
		}
	}

	private Image createQuizIcon() {
		// Create a simple quiz icon
		int size = 32;
		Image img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) img.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(PRIMARY_COLOR);
		g2.fillRoundRect(4, 4, size-8, size-8, 8, 8);
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.BOLD, 16));
		g2.drawString("Q", 12, 22);
		g2.dispose();
		return img;
	}

	private void createComponents() {
		setLayout(new BorderLayout(15, 15));
		((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

		createHeader();
		createQuestionPanel();
		createOptionsPanel();
		createFooter();
	}

	private void createHeader() {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(BACKGROUND_COLOR);
		headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

		// Title
		JLabel titleLabel = new JLabel("Quiz Challenge", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titleLabel.setForeground(SECONDARY_COLOR);
		headerPanel.add(titleLabel, BorderLayout.NORTH);

		// Progress and Timer Panel
		JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		infoPanel.setBackground(BACKGROUND_COLOR);
		infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

		// Progress
		JPanel progressPanel = new JPanel(new BorderLayout(10, 0));
		progressPanel.setBackground(BACKGROUND_COLOR);

		progressLabel = new JLabel("Question 1 of 0");
		progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		progressLabel.setForeground(SECONDARY_COLOR);

		progressBar = new JProgressBar(0, 100);
		progressBar.setBackground(Color.WHITE);
		progressBar.setForeground(PRIMARY_COLOR);
		progressBar.setBorderPainted(false);
		progressBar.setPreferredSize(new Dimension(200, 8));

		progressPanel.add(progressLabel, BorderLayout.WEST);
		progressPanel.add(progressBar, BorderLayout.CENTER);

		// Timer
		JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		timerPanel.setBackground(BACKGROUND_COLOR);

		timerLabel = new JLabel("10:00");
		timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		timerLabel.setForeground(DANGER_COLOR);
		timerLabel.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(DANGER_COLOR, 2, true),
				new EmptyBorder(8, 15, 8, 15)
		));
		timerLabel.setOpaque(true);
		timerLabel.setBackground(Color.WHITE);

		timerPanel.add(new JLabel("Timer "));
		timerPanel.add(timerLabel);

		infoPanel.add(progressPanel);
		infoPanel.add(timerPanel);
		headerPanel.add(infoPanel, BorderLayout.CENTER);

		add(headerPanel, BorderLayout.NORTH);
	}

	private void createQuestionPanel() {
		JPanel questionCard = createCard();
		questionCard.setLayout(new BorderLayout());
		questionCard.setBorder(new EmptyBorder(25, 25, 25, 25));

		JLabel questionIcon = new JLabel("?", SwingConstants.CENTER);
		questionIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		questionIcon.setBorder(new EmptyBorder(0, 0, 15, 0));

		queTextArea = new JTextArea();
		queTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		queTextArea.setLineWrap(true);
		queTextArea.setWrapStyleWord(true);
		queTextArea.setEditable(false);
		queTextArea.setOpaque(false);
		queTextArea.setForeground(SECONDARY_COLOR);
		queTextArea.setBorder(null);

		questionCard.add(questionIcon, BorderLayout.NORTH);
		questionCard.add(queTextArea, BorderLayout.CENTER);

		add(questionCard, BorderLayout.CENTER);
	}

	private void createOptionsPanel() {
		JPanel optionsContainer = new JPanel(new GridLayout(2, 2, 15, 15));
		optionsContainer.setBackground(BACKGROUND_COLOR);
		optionsContainer.setBorder(new EmptyBorder(20, 0, 0, 0));

		optionButtons = new JRadioButton[4];
		bg = new ButtonGroup();

		String[] optionLabels = {"A", "B", "C", "D"};

		for (int i = 0; i < 4; i++) {
			optionButtons[i] = createStyledRadioButton("Option " + (i + 1), optionLabels[i]);
			bg.add(optionButtons[i]);
			optionsContainer.add(optionButtons[i]);
		}

		add(optionsContainer, BorderLayout.SOUTH);
	}

	private JRadioButton createStyledRadioButton(String text, String label) {
		JRadioButton radio = new JRadioButton(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Background
				if (isSelected()) {
					g2d.setColor(PRIMARY_COLOR);
				} else if (getModel().isRollover()) {
					g2d.setColor(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(),
							PRIMARY_COLOR.getBlue(), 30));
				} else {
					g2d.setColor(CARD_COLOR);
				}
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

				// Border
				if (isSelected()) {
					g2d.setColor(PRIMARY_COLOR);
				} else {
					g2d.setColor(new Color(220, 220, 220));
				}
				g2d.setStroke(new BasicStroke(2));
				g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 12, 12);

				g2d.dispose();
				super.paintComponent(g);
			}
		};

		radio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		radio.setForeground(SECONDARY_COLOR);
		radio.setOpaque(false);
		radio.setFocusPainted(false);
		radio.setBorderPainted(false);
		radio.setPreferredSize(new Dimension(200, 60));
		radio.setBorder(new EmptyBorder(15, 50, 15, 15));
		radio.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Add hover effect
		radio.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				radio.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				radio.repaint();
			}
		});

		// Update text color when selected
		radio.addActionListener(e -> {
			for (JRadioButton btn : optionButtons) {
				btn.setForeground(btn.isSelected() ? Color.WHITE : SECONDARY_COLOR);
				btn.repaint();
			}
		});

		return radio;
	}

	private void createFooter() {
		JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		footerPanel.setBackground(BACKGROUND_COLOR);
		footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

		btnNext = createStyledButton("Next Question →", PRIMARY_COLOR);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleNextQuestion();
			}
		});

		footerPanel.add(btnNext);
		add(footerPanel, BorderLayout.EAST);
	}

	private JButton createStyledButton(String text, Color bgColor) {
		JButton button = new JButton(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				if (getModel().isPressed()) {
					g2d.setColor(bgColor.darker());
				} else if (getModel().isRollover()) {
					g2d.setColor(bgColor.brighter());
				} else {
					g2d.setColor(bgColor);
				}

				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
				g2d.dispose();

				super.paintComponent(g);
			}
		};

		button.setFont(new Font("Segoe UI", Font.BOLD, 16));
		button.setForeground(Color.WHITE);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(180, 45));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		return button;
	}

	private JPanel createCard() {
		JPanel card = new JPanel();
		card.setBackground(CARD_COLOR);
		card.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(new Color(230, 230, 230), 1, true),
				new EmptyBorder(0, 0, 0, 0)
		));

		// Add subtle shadow effect
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 20)),
				card.getBorder()
		));

		return card;
	}

	private void loadQuestions() {
		try {
			questions = DataBase.getQuestionAnsWithIds();
			if (questions != null && !questions.isEmpty()) {
				updateQuestion();
				updateProgress();
			}
		} catch (SQLException e) {
			showError("Failed to load questions: " + e.getMessage());
		}
	}

	private void updateQuestion() {
		if (questions != null && count < questions.size()) {
			Question current = questions.get(count);
			queTextArea.setText(current.getQuestion());
			optionButtons[0].setText(current.getOptionA());
			optionButtons[1].setText(current.getOptionB());
			optionButtons[2].setText(current.getOptionC());
			optionButtons[3].setText(current.getOptionD());

			// Clear selection
			bg.clearSelection();
			for (JRadioButton btn : optionButtons) {
				btn.setForeground(SECONDARY_COLOR);
				btn.repaint();
			}
		}
	}

	private void updateProgress() {
		if (questions != null) {
			int total = questions.size();
			int current = count + 1;
			progressLabel.setText("Question " + current + " of " + total);
			progressBar.setValue((int) ((double) current / total * 100));

			if (current == total) {
				btnNext.setText("Finish Quiz");
			}
		}
	}

	private void handleNextQuestion() {
		if (bg.getSelection() == null) {
			showWarning("Please select an answer before proceeding!");
			return;
		}

		checkAnswer(count, bg);
		count++;

		if (questions != null && count < questions.size()) {
			updateQuestion();
			updateProgress();
		} else {
			displayScore();
		}
	}

	private void checkAnswer(int count, ButtonGroup bg) {
		for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			if (button.isSelected() && button.getText().equals(questions.get(count).getCorrectOption())) {
				score++;
				break;
			}
		}
	}

	private void displayScore() {
		if (countdownTimer != null) {
			countdownTimer.stop();
		}

		dispose();

		String message = String.format(
				"** Quiz Completed! **\n\n" +
						"Your Score: %d out of %d\n" +
						"Percentage: %.1f%%\n\n" +
						"Thank you for playing!",
				score,
				questions != null ? questions.size() : 0,
				questions != null ? (double) score / questions.size() * 100 : 0
		);

		JOptionPane.showMessageDialog(
				null,
				message,
				"Quiz Results",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void startTimer(int timeInSecs) {
		countdownTimer = new Timer(1000, new ActionListener() {
			int timeLeft = timeInSecs;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (timeLeft > 0) {
					timeLeft--;
					timerLabel.setText(String.format("%02d:%02d", timeLeft / 60, timeLeft % 60));

					// Change color when time is running low
					if (timeLeft <= 60) {
						timerLabel.setForeground(DANGER_COLOR);
						timerLabel.setBorder(BorderFactory.createCompoundBorder(
								new LineBorder(DANGER_COLOR, 2, true),
								new EmptyBorder(8, 15, 8, 15)
						));
					} else if (timeLeft <= 300) {
						timerLabel.setForeground(new Color(255, 165, 0)); // Orange
					}
				} else {
					((Timer) e.getSource()).stop();
					showWarning("Time's up!");
					displayScore();
				}
			}
		});

		countdownTimer.start();
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void showWarning(String message) {
		JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}
}