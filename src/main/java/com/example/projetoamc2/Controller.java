package com.example.projetoamc2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
    public static Graph greedy(Sample s, int n0graphs) {
        //passo 0: criar lista de grafos random (chamando o construtor random)

        // learn disconnected graph, add to list (passo 1)
        for (int i = 1; i<n0graphs; i++) { // passo 1.1
            //learn n-1 random graphs, add to list

        }

        // from list: return graph with max mdl
        return new Graph(1);
    }

    private static Graph learn_graph(Graph graph, Sample s) {
        double MDLDelta = 1;
        while (MDLDelta > 0) {
            MDLDelta = learn_iteration(graph, s);
        }
        // Aprender o melhor grafo derivado de 1 grafo inicial
        return graph;
    }

    private static double learn_iteration(Graph graph, Sample s) {
        // Algoritmo do prof
        // vês os vizinhos todos
        // descobres o melhor (ainda tens guardado o o, d e operação (2 é add, 1 é inv, 0 é remove))
        // transformas o grafo no grafo vizinho melhor (não esquecer de atualizar os mdls (parciais e talvez o total)) (se inv chamar graph.node_mdl(o, ...) e graph.node_mdl(d, ...) se add ou rem chamar graph.node_mdl(d))
        // devolves o mdldelta deste grafo
        return 0;
    }
}