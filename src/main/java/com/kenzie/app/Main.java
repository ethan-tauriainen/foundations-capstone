package com.kenzie.app;

import javax.swing.JFrame;

/**
 * The main class simply serves as the driver for the program.
 * It contains the main method, which simply constructs
 * a GameFrame object.
 *
 * @author Ethan Tauriainen
 */
public class Main {

    /**
     * Simple main method. Its only purpose is to construct
     * the GameFrame object and some related settings.
     *
     * The first statement is used to execute statements in the
     * event dispatch thread. According to Cay Horstmann in
     * his book Core Java: Volume I, all swing components must
     * be configured form the event dispatch thread.
     *
     * Next up is the definition of what the app should do when
     * the user closes the application. It should exit.
     *
     * I set the frame to be resizable because I'm not yet comfortable
     * enough with Swing to use weightx and weighty parameters of the
     * GridBagConstraints object to allow for the window and its contents
     * to be gracefully resized.
     *
     * Finally, because constructing a frame doesn't mean it will display,
     * it must be explicitly declared with the setVisible() method.
     */
    public static void main(String[] args)  {
        java.awt.EventQueue.invokeLater(() -> {
            JFrame frame = new GameFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}