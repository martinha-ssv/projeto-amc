package com.example.projetoamc2;

import java.io.Serializable;

public class BayesianNetwork implements Serializable {

    public Graph DAG;
    public Cache DFOs;
    public BayesianNetwork(Graph g, Sample s, double S) {
        this.DAG = g;
    }
}
