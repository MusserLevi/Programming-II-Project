import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScrambleGUI extends JFrame {
    private back gameLogic;
    private GameTimer gameTimer;
    private String currentWordTarget;
    private int wordLengthChoice; // 1 = 4 letters, 2 = 5 letters, 3 = 6 letters
    
    private JLabel scrambledWordLabel;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private JLabel messageLabel;
    private JTextField guessInput;
    private JButton submitButton;

    public ScrambleGUI() {
        // Initialize backend logic
        gameLogic = new back();
        gameLogic.setup(); 

        // Setup Main Window
        setTitle("Word Scramble Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Prompt for settings before showing main window
        if (!promptSettings()) {
            System.exit(0); // Exit if user cancels setup
        }

        initializeUI();
        startNewRound();
    }

    private boolean promptSettings() {
        // Difficulty Selection
        String[] difficulties = {"Easy (3 min)", "Medium (1 min)", "Hard (30 sec)"};
        String selectedDiff = (String) JOptionPane.showInputDialog(null, 
                "Select Difficulty:", "Game Setup", JOptionPane.QUESTION_MESSAGE, 
                null, difficulties, difficulties[0]);
        
        if (selectedDiff == null) return false;

        String diffLevel = "easy";
        if (selectedDiff.contains("Medium")) diffLevel = "medium";
        if (selectedDiff.contains("Hard")) diffLevel = "hard";

        // Word Length Selection
        String[] lengths = {"4 Letters", "5 Letters", "6 Letters"};
        String selectedLen = (String) JOptionPane.showInputDialog(null, 
                "Select Word Length:", "Game Setup", JOptionPane.QUESTION_MESSAGE, 
                null, lengths, lengths[0]);
        
        if (selectedLen == null) return false;

        if (selectedLen.startsWith("4")) wordLengthChoice = 1;
        else if (selectedLen.startsWith("5")) wordLengthChoice = 2;
        else wordLengthChoice = 3;

        // Initialize Timer
        gameTimer = new GameTimer(diffLevel, this::updateTimerLabel, this::endGame);
        gameTimer.start();

        return true;
    }

    private void initializeUI() {
        // Top Panel: Timer and Score
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        timerLabel = new JLabel("Time: --:--", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(timerLabel);
        topPanel.add(scoreLabel);

        // Center Panel: Word and Message
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        scrambledWordLabel = new JLabel("Loading...", SwingConstants.CENTER);
        scrambledWordLabel.setFont(new Font("Arial", Font.BOLD, 28));
        messageLabel = new JLabel("Unscramble the word above!", SwingConstants.CENTER);
        messageLabel.setForeground(Color.BLUE);
        centerPanel.add(scrambledWordLabel);
        centerPanel.add(messageLabel);

        // Bottom Panel: Input and Submit
        JPanel bottomPanel = new JPanel();
        guessInput = new JTextField(15);
        guessInput.setFont(new Font("Arial", Font.PLAIN, 18));
        submitButton = new JButton("Guess");
        
        // Handle enter key and button click
        ActionListener guessAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processGuess();
            }
        };
        guessInput.addActionListener(guessAction);
        submitButton.addActionListener(guessAction);

        bottomPanel.add(guessInput);
        bottomPanel.add(submitButton);

        // Add to frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void startNewRound() {
        currentWordTarget = gameLogic.getWord(wordLengthChoice);
        String scrambled = gameLogic.ScrambleWord(currentWordTarget);
        scrambledWordLabel.setText(scrambled.toUpperCase());
        guessInput.setText("");
        guessInput.requestFocus();
    }

    private void processGuess() {
        String userGuess = guessInput.getText().trim().toLowerCase();
        
        if (userGuess.isEmpty()) return;

        int result = gameLogic.guessWord(userGuess, currentWordTarget);
        
        if (result == 1) {
            messageLabel.setText("Correct! +Points");
            messageLabel.setForeground(new Color(0, 150, 0)); // Dark green
            
            scoreLabel.setText("Correct Answer Logged!"); 
            startNewRound();
        } else {
            messageLabel.setText("Incorrect, try again!");
            messageLabel.setForeground(Color.RED);
            guessInput.setText("");
        }
    }

    private void updateTimerLabel() {
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText("Time: " + gameTimer.getFormattedTime());
        });
    }

    private void endGame() {
        SwingUtilities.invokeLater(() -> {
            guessInput.setEnabled(false);
            submitButton.setEnabled(false);
            scrambledWordLabel.setText("TIME'S UP!");
            messageLabel.setText("Game Over. The word was: " + currentWordTarget);
            JOptionPane.showMessageDialog(this, "Time is up! Check console for final stats.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            gameLogic.printTotalScore();
        });
    }

    public static void main(String[] args) {
        // Ensure GUI runs on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new ScrambleGUI().setVisible(true);
        });
    }
}
