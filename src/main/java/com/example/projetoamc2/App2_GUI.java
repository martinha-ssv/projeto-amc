package com.example.projetoamc2;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class App2_GUI {
    private JFrame frame;
    private BayesianNetwork bn;

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
        actions_lbl.setFont(new Font("Arial", Font.BOLD, 30));
        // Missing: Format text
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;

        frame.add(actions_lbl);

        // Load Bayes Network button
        JButton load_bayes = new JButton("Load Bayes Network");

        c.gridy = 1;
        c.weightx = 0;

        frame.getContentPane().add(load_bayes, c);

        // Action Listeners -----------------------------------------------
        load_bayes.addActionListener(actionEvent -> {
            /*JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(load_bayes);
            File f = fc.getSelectedFile();
            String path = f.getAbsolutePath();
            try {
                bn = BayesUtils.deserialize(path, BayesianNetwork.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }*/
            display_input_dialog();

        });
/*
        // Classify Sample label
        JLabel classify_lbl = new JLabel("Classify Sample");
        classify_lbl.setFont(new Font("Arial", Font.BOLD, 35));

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 5;
        c.insets = new Insets(20,20,20,20);

        frame.getContentPane().add(classify_lbl, c);

        // Vector input panel
        JPanel input_pnl = new JPanel(new GridBagLayout());
        input_pnl.setBackground(new Color(250, 250, 250)); // Example setting a cyan background
        input_pnl.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1)); // Black line border, 2 pixels thick



        // for loop: Variable labels and Variable text fields

        // Predict button: vector panel

        // Output text area

*/


    }

    private void display_input_dialog() {
        GridBagConstraints c = new GridBagConstraints();

        // Predict button (sidebar)
        JButton predict_sidebar = new JButton("Predict");

        c.gridy = 2;

        frame.getContentPane().add(predict_sidebar, c);

        // Classify Sample label
        JLabel classify_lbl = new JLabel("Classify Sample");
        classify_lbl.setFont(new Font("Arial", Font.BOLD, 35));

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = bn.getNumNodes();
        c.insets = new Insets(20,20,20,20);

        frame.getContentPane().add(classify_lbl, c);

        // Vector input panel
        JPanel input_pnl = new JPanel(new GridBagLayout());
        input_pnl.setBackground(new Color(250, 250, 250));
        input_pnl.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));

        JTextField[] input_fields = new JTextField[bn.getNumNodes()];
        JLabel[] labels = new JLabel[bn.getNumNodes()];

        // for loop: Variable labels and Variable text fields
        for (int i = 0; i<bn.getNumNodes(); i++) {
                // Input Labels
                c.gridx = i+1;
                c.gridy = 0;
                labels[i] = new JLabel("X"+Integer.toString(i));
                labels[i].setFont(new Font("Andale Mono", Font.PLAIN, 15));

                input_pnl.add(labels[i],c);

                // Input Fields
                c.gridy = 1;

                input_fields[i] = new JTextField();

                input_pnl.add(input_fields[i], c);


        }

    }


}
