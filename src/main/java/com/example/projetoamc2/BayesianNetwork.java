package com.example.projetoamc2;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class BayesianNetwork implements Serializable {

    public Graph DAG;
    public DFONode[] dfos;
    public BayesianNetwork(Graph g, Sample s, double S) {
        this.DAG = g;
    }

    public void export_network(String path) {
        if (path == null || path.isEmpty()) path = "bayesNet.txt";
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            fileOut.close();
            System.out.println("The Bayesian Network was exported successfully.");
            System.out.print("To find it, check the "); System.out.print(path); System.out.print(" directory.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
