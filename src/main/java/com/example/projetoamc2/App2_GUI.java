package com.example.projetoamc2;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class App2_GUI {
    private JFrame frame;
    private BayesianNetwork bn;
    JTextField[] input_fields;
    JButton predict = new JButton("Predict");
    JTextArea output_field = new JTextArea();

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
        c.weightx = 1;
        c.weighty = 0;

        frame.add(actions_lbl);

        // Load Bayes Network button
        JButton load_bayes = new JButton("Load Bayes Network");

        c.gridy = 2;
        c.weightx = 0;

        frame.getContentPane().add(load_bayes, c);

        // Output field
        output_field.setEditable(false);
        output_field.setFont(new Font("Andale Mono", Font.BOLD, 20));
        output_field.setLineWrap(true);

        // Action Listeners -----------------------------------------------

        // Load Bayes button
        load_bayes.addActionListener(actionEvent -> {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(load_bayes);
            File f = fc.getSelectedFile();
            String path = f.getAbsolutePath();
            try {
                bn = BayesUtils.deserialize(path, BayesianNetwork.class);
                System.out.println("Bayes Network imported successfully! :D");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            display_input_dialog();
        });

        // Predict Button

        predict.addActionListener(actionEvent -> {
            System.out.println("Predict button clicked");
                boolean allInputsValid = true;

                for (int i = 0; i<input_fields.length && allInputsValid; i++) {
                    try {
                        int value = Integer.parseInt(input_fields[i].getText());
                        if (value < 0 || value >= bn.domains[i]) {
                            JOptionPane.showMessageDialog(frame, "Integer "+i+" out of range (0 to " + bn.domains[i] + "): " + value + "(X"+i+")");
                            allInputsValid = false;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Please insert a valid integer: X"+i);
                        allInputsValid = false;
                    }
                }



            if (allInputsValid) {
                    LinkedList<Integer> vec = new LinkedList<>();
                    for (int i = 0; i<input_fields.length; i++) {
                        vec.add(Integer.parseInt(input_fields[i].getText()));
                    }
                    output_field.setText("The sample provided should be classified as class "+Integer.toString(bn.classify(vec)));
                    // All inputs are valid, proceed with the action
                    // ... rest of your button click action ...
                }
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
        c.gridy = 3;

        frame.getContentPane().add(predict, c);

        // Classify Sample label
        JLabel classify_lbl = new JLabel("Classify Sample");
        classify_lbl.setFont(new Font("Arial", Font.BOLD, 35));

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = bn.getNumNodes();
        c.weightx = 0;

        frame.getContentPane().add(classify_lbl, c);


        input_fields = new JTextField[bn.getNumNodes()];
        JLabel[] labels = new JLabel[bn.getNumNodes()];

        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 4, 5, 4);


        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        for (int i = 0; i < bn.getNumNodes(); i++) {
            JLabel label = new JLabel("X" + i);
            input_fields[i] = new JTextField(1);
            inputPanel.add(label);
            inputPanel.add(input_fields[i]);
        }

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = bn.getNumNodes();
        frame.getContentPane().add(inputPanel, c);


        c.gridy = 4;
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.weightx = 0.1;
        c.weighty = 0.1;

        frame.getContentPane().add(output_field, c);

        frame.revalidate();
        frame.repaint();
    }


}
