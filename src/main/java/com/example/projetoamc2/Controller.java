package com.example.projetoamc2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
    public static Graph greedy(Sample s, int n0graphs) {
        // learn disconnected graph, add to list
        for (int i = 1; i<n0graphs; i++) {
            //learn n-1 random graphs, add to list
        }
        // from list: return graph with max mdl
        return new Graph(1);
    }

    private static Graph learn_graph(Sample s) {
        return new Graph(1);
    }

}