package com.example.projetoamc2;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BayesianNetwork implements Serializable {

    public Graph DAG;
    public DFONode[] dfos;
    public int noClasses;
    public Double[] probs_c;
    public Integer[] domains;

    public BayesianNetwork(Graph g, Sample sample, double S) {
        this.DAG = g;
        this.dfos = new DFONode[this.DAG.getDim()];
        this.domains = sample.getDomains();
        this.noClasses = domains[domains.length-1];
        this.probs_c = this.calculate_class_probs(sample, S);
        for (int i = 0; i < this.DAG.getDim(); i++) {
            dfos[i] = new DFONode(sample, S, i, this);
        }

    }

    private Double[] calculate_class_probs(Sample sample, Double S) {
        Double[] res = new Double[this.noClasses];
        int T = sample.getLen();
        for (int c = 0; c<this.noClasses; c++) {
            int T_c = sample.count(List.of(this.DAG.getDim()), List.of(c));
            res[c] = ((double) ((T_c + S) / (T + S * this.noClasses)));
        }
        return res;
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
        return this.DAG.getDim();
    }

    public DFONode getDFO(int node) {
        return dfos[node];
    }

    private Double[] classes_probs(List<Integer> vec) {
        Double[] Ps = Arrays.copyOf(probs_c, probs_c.length);

        for (int i = 0; i<this.DAG.getDim(); i++) {
            for (int c = 0; c<this.noClasses; c++) {
                Ps[c] *= this.getDFO(i).getTheta(vec, c);
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

    public static void main(String[] args) {
        Sample sample = new Sample("data/raw/bcancer.csv");
        Graph g = new Graph(10, sample);
        g.addEdge(3,2);
        g.addEdge(5,2);
        g.addEdge(5,3);
        g.addEdge(6,4);
        BayesianNetwork bn = new BayesianNetwork(g, sample, 0.5);


    }
}
