package com.example.projetoamc2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
    public static BayesianNetwork Learn(Sample s, int n0grafos) {
        // Sofia :D
        return new BayesianNetwork(new Graph(1), s, 0.5); // Escrevi esta linha só para não dar erro, depois muda :D
    }


}