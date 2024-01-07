package com.example.projetoamc2;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class App1_GUI {

    private JFrame frame;
    private Sample s;

    private BayesianNetwork bn;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                App1_GUI window = new App1_GUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public App1_GUI() {
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

        // Adding Components

        // Load CSV button
        JButton load_csv = new JButton("Load CSV");

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;

        frame.getContentPane().add(load_csv, c);

        // Learn Sample button
        JButton learn_sample = new JButton("Learn Sample");
        learn_sample.setEnabled(false);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;

        frame.getContentPane().add(learn_sample, c);

        // Save Learned Network button
        JButton save_bayes = new JButton("Save Learned Network");
        save_bayes.setEnabled(false);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;

        frame.getContentPane().add(save_bayes, c);

        // Dataset label
        JLabel dataset_lbl = new JLabel("Dataset: Not Loaded");

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 4;
        c.gridheight = 1;
        c.weightx = 4;
        c.weighty = 0;

        frame.getContentPane().add(dataset_lbl, c);

        // Dataset text area (acho que depois vou mudar para uma JTable mas MVP meninas MVP)
        JTextArea dataset_disp = new JTextArea();
        JScrollPane scroll = new JScrollPane(dataset_disp);
        dataset_disp.setEditable(false);
        frame.getContentPane().add(scroll);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 4;
        c.gridheight = 4;
        c.weightx = 4;
        c.weighty = 4;

        frame.getContentPane().add(dataset_disp, c);





        // Action Listeners -------------------------------------------------------------

        // Load CSV Button
        load_csv.addActionListener(actionEvent -> {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(load_csv);
            File f = fc.getSelectedFile();
            String path = f.getAbsolutePath();
            s = new Sample(path);
            dataset_lbl.setText("Dataset: Loaded");
            dataset_disp.setText(s.toString());
            learn_sample.setEnabled(true);

        });

        // Learn Sample Button
        learn_sample.addActionListener(actionEvent -> {
            boolean isInt = false;
            int n0graphs = 1;

            while (!isInt) {
                String input = JOptionPane.showInputDialog(learn_sample, "Please input the number of starting graphs for the learning algorithm.");
                try {
                    n0graphs = Integer.parseInt(input);
                    isInt = n0graphs > 0;
                } catch (NumberFormatException e) {
                    System.out.println("Error: The input provided is not valid.");
                }
            }

            Graph bestgraph = Controller.greedy(s, n0graphs);
            //Display graph

            Double S = 0.5;
            boolean isDouble = false;

            while (!isDouble) {
                String input = JOptionPane.showInputDialog(learn_sample, "Please input the pseudocount S value.");
                try {
                    S = Double.valueOf(input.replace(',', '.'));
                    isDouble = S > 0;
                } catch (NumberFormatException e) {
                    System.out.println("Error: The input provided is not valid.");
                }
            }

            bn = new BayesianNetwork(bestgraph, s, S);
            save_bayes.setEnabled(true);

            // Experiment: GraphPanel - maybe remove?
            Graph_Experiment graphpanel = new Graph_Experiment(bn.DAG);
            c.gridx = 1;
            c.gridy = 5;
            c.gridwidth = 4;
            c.gridheight = 3;
            c.weightx = 4;
            c.weighty = 3;

            frame.getContentPane().add(graphpanel, c);

        });

        // Save Bayesian Network Button
        save_bayes.addActionListener(actionEvent -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Select Directory");
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.showSaveDialog(save_bayes);
            File f = fc.getSelectedFile();
            String path = f.getAbsolutePath();
            bn.export_network(path);

        });

        /*
        c.gridx = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;


        for (int x = 0; x<3; x++) {
            c.gridy = x;
            frame.getContentPane().add(new JButton("Hello"), c);
        }

        c.gridx = 1;
        c.weightx = 5;
        c.gridwidth = 5;


        for (int x = 0; x<3; x++) {
            c.gridy = x;
            frame.getContentPane().add(new JButton("Hello"), c);
        }

        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 0;
        c.gridwidth = 1;
        frame.getContentPane().add(new JButton("Hello World"), c);




        int csvButtonWidth = 500;
        int csvButtonHeight = 10;
        JButton btnLoadCSV = new JButton("Load CSV");
        //btnLoadCSV.setSize(csvButtonWidth, csvButtonHeight);//setBounds((width-csvButtonWidth)/2, (height-csvButtonHeight)/2, csvButtonWidth, csvButtonHeight);
        frame.getContentPane().add(btnLoadCSV, c);

        JLabel lblSuccess = new JLabel("Success");
        //lblSuccess.setHorizontalAlignment(SwingConstants.CENTER);
        //lblSuccess.setSize(csvButtonWidth,csvButtonHeight);//setBounds(18, 62, 411, 16);
        left.add(lblSuccess);



        JButton btnSave = new JButton("Save Sample");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.showSaveDialog(btnSave);
                String path = fc.getSelectedFile().getAbsolutePath();
                System.out.println(path);
                BayesUtils.export_object(path, am);
            }
        });
        btnSave.setEnabled(false);
        //btnSave.setBounds(159, 90, 117, 29);
        left.add(btnSave);

        JButton btnLoadSample = new JButton("Load Sample");
        //btnLoadSample.setBounds(159, 131, 117, 29);
        left.add(btnLoadSample);


        JTextArea textArea = new JTextArea();
        textArea.setEditable(true);
        JScrollPane scroll = new JScrollPane(textArea);
        //scroll.setBounds(41, 176, 374, 112);
        //textArea.setBounds(41, 176, 374, 112);
        left.add(scroll);

        JFileChooser file_chooser = new JFileChooser();
        left.add(file_chooser);



        btnLoadCSV.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(btnLoadCSV);
                File f = fc.getSelectedFile();
                String path = f.getAbsolutePath();
                String name = f.getName();
                am = new Sample(path);
                lblSuccess.setText("Successfully loaded CSV file " + name);
                btnSave.setEnabled(true);

                System.out.println(am);
            }
        });

        btnLoadSample.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(btnLoadSample);
                File f = fc.getSelectedFile();
                String path = f.getAbsolutePath();
                Object ldSample = BayesUtils.import_object(path);
                textArea.setText(ldSample.toString());
            }
        });

         */
    }
}
