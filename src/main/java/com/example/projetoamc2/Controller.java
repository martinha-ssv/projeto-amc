package com.example.projetoamc2;

import java.util.LinkedList;


public class Controller {
    public static Graph greedy(Sample s, int n0graphs) {
        //passo 0: criar lista de grafos random (chamando o construtor random)
        int dim = s.noColumns()-1;
        LinkedList<Graph> graphs = new LinkedList<>();
        graphs.add(new Graph(dim, s));

        for (int i = 1; i < n0graphs; i++) {
            graphs.add(new Graph(dim, s, "Random!"));
        }

        System.out.println("Graphs initiated!");
        System.out.println(graphs);

        // Aprender cada grafo
        for (int i = 0; i<n0graphs; i++) {
            learn_graph(graphs.get(i), s);
            System.out.println("Graph "+Integer.toString(i)+" learned!");
            System.out.println("MDL is "+Double.toString(graphs.get(i).MDL(s, false)));
        }

        // Grafo com melhor MDL
        int bestInd = 0;

        for (int i = 1; i<n0graphs; i++) {
            if (graphs.get(i).MDL(s, false) > graphs.get(bestInd).MDL(s, false)) {
                bestInd = i;
            }
        }
        System.out.println(graphs.get(bestInd));

        // from list: return graph with max mdl
        return graphs.get(bestInd);
    }

    /**
     *
     * @param graph starting graph.
     * @param s dataset.
     * @return The best graph learned from a given starting graph.
     */
    private static Graph learn_graph(Graph graph, Sample s) {
        double MDLDelta = 1;
        while (MDLDelta > 0) {
            MDLDelta = learn_iteration(graph, s);
        }

        return graph;
    }

    // feitoo
    private static double learn_iteration(Graph graph, Sample s) {

        Double bestMDLDelta = Double.NEGATIVE_INFINITY;
        Double currentMDLDelta;
        Integer[] bestAlteration = new Integer[] {0,0,0};

        // vês os vizinhos todos
        for (int o = 0; o < graph.getDim(); o++) {
            for (int d = 1; d < graph.getDim(); d++) {
                for (int operation = 0; operation <=2; operation ++) {
                    currentMDLDelta = graph.MDLdelta(s, o, d, operation);
                    // descobres o melhor (ainda tens guardado o o, d e operação (2 é add, 1 é inv, 0 é remove))
                    if (currentMDLDelta > bestMDLDelta) {
                        bestMDLDelta = currentMDLDelta;
                        bestAlteration = new Integer[] {o,d,operation};
                    }
                }
            }
        }

        if (bestMDLDelta > 0) {
            // transformas o grafo no grafo vizinho melhor (não esquecer de atualizar os mdls (parciais e talvez o total)) (se inv chamar graph.node_mdl(o, ...) e graph.node_mdl(d, ...) se add ou rem chamar graph.node_mdl(d))
            System.out.println("We found a best neighbor! Still going.");
            graph.applyOperation(bestAlteration[0], bestAlteration[1], bestAlteration[2]);
            if (bestAlteration[2]==1) {
                graph.node_MDL(s, bestAlteration[0], true);
                graph.node_MDL(s, bestAlteration[1], true);
            } else {
                graph.node_MDL(s, bestAlteration[1], true);
            }

        }

        // devolves o mdldelta deste grafo
        return bestMDLDelta;
    }



}
