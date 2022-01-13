package com.kenzie.app;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

/**
 * Represents a GUI frame that will display according to the operating
 * system on which the program runs.
 *
 * This class extends the JFrame class in order to store all the code
 * that defines a frame within a single class. This class.
 * See Section 4, GUI Programming, Murach's Java Programming for more
 * on this.
 *
 * @author Ethan Tauriainen
 */
public class GameFrame extends JFrame {

    private List<ClueDTO> clues;
    private int currentIndex = 0;   // Keeping track, so only 10 questions are asked.
    private int score = 0;

    private JTextField scoreBoardField;
    private JTextField colorCodeField;
    private JTextField timerField;
    private JTextField answerField;
    private JTextArea questionArea;

    private JButton startButton;
    private JButton nextButton;
    private JButton submitButton;

    // The following defines a timer object.
    // It will be used to update the timerField and control
    // the pace of play. The user will have 45 seconds to answer
    // each question.
    //
    // I learned how to use this object here:
    // https://docs.oracle.com/javase/tutorial/uiswing/misc/timer.html
    // And here:
    // Swing: A Beginner's Guide by Herbert Schildt.
    // See Module 10: Threading, Applets, Painting, and Layouts.
    private Timer timer;
    private int time;

    /**
     * The constructor for the GameFrame class.
     * It simply calls the below method, initComponents(), which
     * builds the frame and its components.
     */
    public GameFrame() {
        initComponents();
    }

    /**
     * This method builds the window that will display
     * and all of its components. I could have put all of this
     * code into the public constructor, however I prefer to keep
     * all of these details guarded within this private method.
     * This way the only public-facing interface is the constructor,
     * which simply calls this method to "initialize components".
     */
    private void initComponents() {

        // The following try/catch block allows the look and feel of the form
        // to be determined by whatever the native operating system is that is
        // running the code.

        // This is standard boilerplate. See Murach's Java Programming (5th Edition),
        // Chapter 18, How to get started with Swing.

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("Trivia, YAY!");
        setLocationByPlatform(true);

        // Initialize all of my text fields.
        scoreBoardField = new JTextField("0", 30);
        scoreBoardField.setBackground(Color.WHITE);
        colorCodeField = new JTextField(30);
        colorCodeField.setBackground(Color.WHITE);
        timerField = new JTextField(String.valueOf(time), 30);
        timerField.setBackground(Color.BLACK);
        timerField.setForeground(Color.GREEN);
        answerField = new JTextField("", 30);

        // Initialize text area for question (larger text box).
        // Notice I do not set the size of the text area here.
        // This is done below using the scroll pane feature.
        // See the "setPreferredSize" method for the questionAreaScrollPane
        // below.
        questionArea = new JTextArea();
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);

        // Add scrolling capability in case there are long questions.
        // I learned about this feature from the official Oracle Documentation:
        // https://docs.oracle.com/javase/tutorial/uiswing/components/textarea.html
        JScrollPane questionAreaScrollPane = new JScrollPane(questionArea);
        questionAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        questionAreaScrollPane.setPreferredSize(new Dimension(250, 100));   // Sets the size of the text area.

        // I don't want the user to be able to mess around with the contents
        // of the following fields.
        scoreBoardField.setEditable(false);
        colorCodeField.setEditable(false);
        timerField.setEditable(false);
        questionArea.setEditable(false);

        // Initialize the buttons, which will have action event handlers attached to them
        // to communicate with and enact the "backend" functionality of the application.
        startButton = new JButton("Start");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");

        // Here we're going to disable the nextButton and the submitButton.
        // We want the first button pressed to be the startButton in order
        // to get things going.
        nextButton.setEnabled(false);
        submitButton.setEnabled(false);

        // Setting up the event handlers to link a function to the buttons.
        // Using a lambda expression to keep everything nice and tight.
        // Upon each button click, the corresponding function will be invoked.
        startButton.addActionListener(e -> startButtonClicked());
        nextButton.addActionListener(e -> nextButtonClicked());
        submitButton.addActionListener(e -> submitButtonClicked());

        // The following sets up the action listener for the timer:
        timer = new Timer(1000, e -> updateTimerField());

        // Below there are two panels. A button panel and a main panel.
        // This helps me to better organize the components in the form.
        // Since I want to use a GridBagLayout for the main panel components,
        // I organize them individually within the main panel according to the
        // grid. However, I want to use a simple FlowLayout for the buttons.
        // Therefore, I group them into their own panel, which I will add to the
        // GameFrame exclusive of the main panel.
        //
        // Each panel (the button and the main) will be placed upon the GameFrame
        // using the BorderLayout.

        // Button panel.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(startButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);

        // Main panel.
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        panel.add(new JLabel("Scoreboard:"), getConstraints(0, 0));
        panel.add(scoreBoardField, getConstraints(1, 0));

        panel.add(new JLabel("Timer:"), getConstraints(0, 1));
        panel.add(timerField, getConstraints(1, 1));

        panel.add(new JLabel("You answered:"), getConstraints(0, 2));
        panel.add(colorCodeField, getConstraints(1, 2));

        panel.add(new JLabel("Question:"), getConstraints(0, 3));
        panel.add(questionAreaScrollPane, getConstraints(1, 3));

        panel.add(new JLabel("Answer:"), getConstraints(0, 4));
        panel.add(answerField, getConstraints(1, 4));

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
    }

    /**
     * Helper method to assist in laying out the grid above.
     * First create a new GridBagConstraints object. Then update the
     * relevant position-related settings. We're only using a few here.
     *
     * Using a method like this is much preferred against the alternative, which
     * would involve updating each of the position items below for each
     * component above. As you can imagine this would be quite verbose. This way
     * we create a new GridBagConstraints object each time we want to determine
     * the position of a component on the grid.
     *
     * The anchor method determines where in the cell the component should
     * be shifted. The insets determine the space around the component.
     *
     * @param x the x-axis position of the component on the grid.
     * @param y the y-axis position of the component on the grid.
     * @return the updated GridBagConstraints object that will be used to set the
     * component's position.
     */
    private GridBagConstraints getConstraints(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(10, 10, 0, 10);
        c.gridx = x;
        c.gridy = y;
        return c;
    }

    /**
     * Update the elapsed time in the timerField.
     * The timer starts out in green font, but when it reaches
     * 10 seconds left it turns to red.
     * When time is out, the submit button will be disabled and a
     * dialogue box will appear containing instructions to move on,
     * as well as the correct answer to the missed question.
     *
     * This method will 'fire' every second as indicated when I
     * constructed the Timer object above.
     */
    private void updateTimerField() {
        if (time == 0) {
            timer.stop();
            submitButton.setEnabled(false);
            String msg = "Oh no! You ran out of time.\n" +
                    "Hit 'Next' to continue.\n" +
                    "If it's the end of your game, hit 'Start' to play again!\n" +
                    "The correct answer was: " + clues.get(currentIndex).getAnswer() + ".\n";
            JOptionPane.showMessageDialog(this, msg, "Time Infraction", JOptionPane.WARNING_MESSAGE);
        }
        if (time > 10) {
            timerField.setForeground(Color.GREEN);
        } else {
            timerField.setForeground(Color.RED);
        }
        timerField.setText(String.valueOf(time));
        time--;
    }

    /**
     * Simple method to extract the category title and the question
     * from the ClueDTO at the currentIndex.
     *
     * @return a string containing the category title and question from
     * the ClueDTO at currentIndex.
     */
    private String displayQuestion() {
        return "Category: " +
                clues.get(currentIndex).getCategory().getTitle() +
                "\n" +
                "Question: " +
                clues.get(currentIndex).getQuestion();
    }

    /**
     * When the start button is clicked this method runs. It makes the GET request and
     * populates the clues list with the clues to be used in the game. Then it
     * shuffles the list (ensures the randomization of the questions). After it completes
     * those tasks it enables the functionality of the other two buttons and disables itself.
     * This button may only be used once per game.
     */
    private void startButtonClicked() {
        try {
            String responseBody = CustomHttpClient.sendGET("https://jservice.kenzie.academy/api/clues");
            clues = CustomHttpClient.getCluesList(responseBody);    // Populate the clues list with clues.
            Collections.shuffle(clues);     // Randomize the list of clues.
            questionArea.setText(displayQuestion());
            colorCodeField.setText("");
            colorCodeField.setBackground(Color.WHITE);
            startButton.setEnabled(false);  // Served its purpose.
            nextButton.setEnabled(true);    // Now we need this functionality.
            submitButton.setEnabled(true);  // Same.
            time = 45;
            timer.start();
        } catch (URISyntaxException | IOException | InterruptedException | ResponseCodeException e) {
            // In the case of an error, display a dialogue box:
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0); // Exit gracefully.
        }
    }

    /**
     * Sets the questionArea with the next question and increments the currentIndex
     * variable. If ten questions have been asked, it disables itself. I decided to
     * allow the user to click next and get a new question whether or not an answer
     * was provided. This can serve as a "skip" as it were. Though, points will not
     * be awarded and the user may not return to a skipped question.
     */
    private void nextButtonClicked() {
        time = 45;
        colorCodeField.setText("");
        colorCodeField.setBackground(Color.WHITE);
        answerField.setText("");
        if (currentIndex == 9) {
            nextButton.setEnabled(false);
            startButton.setEnabled(true);   // To start another game.
        }
        timer.start();
        currentIndex++;
        questionArea.setText(displayQuestion());
        submitButton.setEnabled(true);
    }

    private void submitButtonClicked() {
        // First thing's first. Here is a check to see if the answer field
        // is empty. If it is, a dialog box pops up prompting the user to
        // fill in the field, or press next to skip the question and forfeit
        // points.
        if (answerField.getText().isEmpty()) {
            String msg = "Please enter an answer.\nIf you wish to forfeit, press 'Next'.";
            JOptionPane.showMessageDialog(this, msg, "Invalid Entry", JOptionPane.ERROR_MESSAGE);
            answerField.requestFocusInWindow();
            return;
        }

        // Create an answer variable from the user entered text.
        // Trim excess whitespace, set it to lower case for even comparison.
        String userAnswer = answerField.getText().toLowerCase().trim();

        // Make the comparison between what the user entered and the actual answer.
        // If the user's answer 'contains' the correct answer, the answer will be
        // counted as correct. This will allow for the user to type in variations of
        // the answer (i.e. what is a book, or who is Isiah, etc.). Or simply Isiah.
        // Either way, it will be correct. Also, if the answer is correct, then the user
        // can no longer submit an answer. However, if the answer is incorrect, the user
        // can guess again until time runs out.
        String realAnswer = clues.get(currentIndex).getAnswer().toLowerCase().trim();
        if (userAnswer.contains(realAnswer)) {
            score++;
            scoreBoardField.setText(String.valueOf(score));
            colorCodeField.setText("CORRECT!");
            colorCodeField.setBackground(Color.GREEN);
            submitButton.setEnabled(false);
            timer.stop();
        } else {
            colorCodeField.setText("WRONG!");
            colorCodeField.setBackground(Color.RED);
        }
    }
}
