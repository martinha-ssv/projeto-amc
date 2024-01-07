package com.example.projetoamc2;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BayesianNetwork implements Serializable {

    public Graph DAG;
    private int numNodes;
    public DFONode[] dfos;
    public int noClasses;

    public Integer[] domains;
    public BayesianNetwork(Graph g, Sample sample, double S) {
        this.DAG = g;
        this.dfos = new DFONode[this.DAG.getDim()];
        for (int i = 0; i < this.DAG.getDim(); i++) {
            dfos[i] = new DFONode(g, sample, S, i, this);
        }
        this.domains = sample.getDomains();
        this.noClasses = domains[domains.length-1]+1;

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

    public int getNumNodes() {
        return numNodes;
    }

    public DFONode getDFO(int node) {
        return dfos[node];
    }

    private Double[] classes_probs(List<Integer> vec) {
        Double[] Ps = new Double[this.noClasses];
        for (int c = 0; c<this.noClasses; c++) Ps[c] = 1.0;

        for (int i = 0; i<this.DAG.getDim(); i++) {
            for (int c = 0; c<this.noClasses; c++) {
                Ps[c]*= this.getDFO(i).getTheta(vec, c);
            }
        }

        return Ps;
    }

    public Integer classify(List<Integer> vec) {
        Double[] Ps = classes_probs(vec);
        int max_ind = 0;
        for (int i = 1; i<Ps.length; i++) {
            if (Ps[i] > Ps[max_ind]) {
                max_ind = i;
            }
        }
        return max_ind;
    }

}
