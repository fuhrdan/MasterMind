import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class MasterMind extends JFrame {
    private final String[] COLORS = {"Red", "Green", "Blue", "Yellow", "Orange", "Purple"};
    private String[] secretCode = new String[4];
    private ArrayList<JButton> colorButtons = new ArrayList<>();
    private ArrayList<String> currentGuess = new ArrayList<>();
    private JTextArea feedbackArea;

    public MasterMind() {
        setTitle("MasterMind Game");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Generate a secret code
        generateSecretCode();

        // Create UI
        createUI();
    }

    private void generateSecretCode() {
        ArrayList<String> colorList = new ArrayList<>();
        Collections.addAll(colorList, COLORS);
        Collections.shuffle(colorList);
        for (int i = 0; i < 4; i++) {
            secretCode[i] = colorList.get(i);
        }
        System.out.println("Secret Code: " + String.join(", ", secretCode)); // For debugging
    }

    private void createUI() {
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel instructions = new JLabel("Select 4 colors and press Submit.");
        topPanel.add(instructions);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        
        // Color selection buttons
        JPanel colorPanel = new JPanel(new GridLayout(2, COLORS.length / 2));
        for (String color : COLORS) {
            JButton button = new JButton(color);
            button.setBackground(getColor(color));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> addColorToGuess(color));
            colorButtons.add(button);
            colorPanel.add(button);
        }
        centerPanel.add(colorPanel);

        // Feedback area
        feedbackArea = new JTextArea(10, 40);
        feedbackArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton submitButton = new JButton("Submit Guess");
        submitButton.addActionListener(e -> checkGuess());
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetGame());
        bottomPanel.add(submitButton);
        bottomPanel.add(resetButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addColorToGuess(String color) {
        if (currentGuess.size() < 4) {
            currentGuess.add(color);
            feedbackArea.append("Added: " + color + "\n");
        } else {
            feedbackArea.append("You already selected 4 colors. Press Submit.\n");
        }
    }

    private void checkGuess() {
        if (currentGuess.size() != 4) {
            feedbackArea.append("Please select 4 colors before submitting.\n");
            return;
        }

        int blackPegs = 0; // Correct color, correct position
        int whitePegs = 0; // Correct color, wrong position
        boolean[] codeUsed = new boolean[4];
        boolean[] guessUsed = new boolean[4];

        // Check for black pegs
        for (int i = 0; i < 4; i++) {
            if (currentGuess.get(i).equals(secretCode[i])) {
                blackPegs++;
                codeUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Check for white pegs
        for (int i = 0; i < 4; i++) {
            if (!guessUsed[i]) {
                for (int j = 0; j < 4; j++) {
                    if (!codeUsed[j] && currentGuess.get(i).equals(secretCode[j])) {
                        whitePegs++;
                        codeUsed[j] = true;
                        break;
                    }
                }
            }
        }

        feedbackArea.append("Guess: " + String.join(", ", currentGuess) + "\n");
        feedbackArea.append("Black Pegs: " + blackPegs + ", White Pegs: " + whitePegs + "\n");

        if (blackPegs == 4) {
            feedbackArea.append("Congratulations! You've guessed the code!\n");
        }

        currentGuess.clear();
    }

    private void resetGame() {
        feedbackArea.setText("");
        currentGuess.clear();
        generateSecretCode();
    }

    private Color getColor(String colorName) {
        return switch (colorName) {
            case "Red" -> Color.RED;
            case "Green" -> Color.GREEN;
            case "Blue" -> Color.BLUE;
            case "Yellow" -> Color.YELLOW;
            case "Orange" -> Color.ORANGE;
            case "Purple" -> new Color(128, 0, 128); // Custom purple
            default -> Color.BLACK;
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MasterMind game = new MasterMind();
            game.setVisible(true);
        });
    }
}
