package com.example.projetoamc2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
    public BayesianNetwork Learn(String dataset_path, int n) {
        // Sofia :D
        return new BayesianNetwork(new Graph(1), new Sample(dataset_path), 0.5); // Escrevi esta linha só para não dar erro, depois muda :D
    }
}