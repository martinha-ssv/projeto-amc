package com.example.projetoamc2;

import javax.swing.*;
import java.awt.*;

public class App2_GUI {
    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                App2_GUI window = new App2_GUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public App2_GUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        // Frame
        int width = 800;
        int height = 400;
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        // Add Components

        // Actions label
        JLabel actions_lbl = new JLabel("Actions");
        // Missing: Format text
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;

        frame.add(actions_lbl);

        // Load Bayes Network button
        JButton load_bayes = new JButton("Load Bayes Network");

        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;

        frame.getContentPane().add(load_bayes, c);

        // Predict button (sidebar)
        JButton predict_sidebar = new JButton("Predict");
        predict_sidebar.setEnabled(false);

        c.gridy = 2;

        frame.getContentPane().add(predict_sidebar, c);

        // Classify Sample label
        JLabel classify_lbl = new JLabel("Classify Sample");

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 5;

        frame.getContentPane().add(classify_lbl, c);

        // Vector input panel
        JPanel input_pnl = new JPanel(new GridBagLayout());

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 5;
        c.gridheight = 2;



        // for loop: Variable labels and Variable text fields

        // Predict button: vector panel

        // Output text area




    }



}
