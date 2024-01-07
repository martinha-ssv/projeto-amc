package com.example.projetoamc2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;


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

        // AlTERAÇÕES = [] --- código professor
        //Para cada origem em {1, ..., n}:
        //Para cada destino em {1, ..., n}:
        //Se a aresta (origem, destino) não existe em G:
        //Se adicionar (origem, destino) não cria um ciclo nem viola a restrição do número máximo de pais:
        //ALTERAÇÕES += [adicionar a aresta (origem, destino)]
        //Se a aresta (origem, destino) existe em G:
        //Se inverter (origem, destino) não cria um ciclo nem viola a restrição do número máximo de pais:
        //ALTERAÇÕES += [inverter a aresta (origem, destino), apagar a aresta (origem, destino)]
        //Caso contrário:
        //ALTERAÇÕES += [apagar a aresta (origem, destino)]
        ArrayList<Integer> possibleChanges = new ArrayList<>();
        //erro porque falta dizer que possibleChanges contém entradas de int o, int d e int operation?
        for (int o=1; o < graph.length; int o; o++) {
            for (int d=1; d < graph.length; int d; d++)) {
                if graph.edgeQ(o,d) == False: {
                    if (!graph.addcreatesCycle(d, o) && graph.operationAllowed()) {
                        possibleChanges+=[2 (o,d)];
                    }
                else {
                    if (!graph.invertcreatesCycle(d, o) && graph.operationAllowed()) {
                        possibleChanges +=[1(o,d)];
                        }
                    else {
                        possibleChanges += [0(o,d)];
                        }
                    }
                }
            }

        }
        for (int i=0; i <possibleChanges.length; int i; i++){
            double bestMDLDelta = Double.NEGATIVE_INFINITY;
            Graph bestGraph = graph;
            double mdlDelta = MDLdelta(i,o,d,operation);
            Array bestChange =
            Graph modifiedGraph = applyOperation(int o, int d, int operation); //dúvida: como é que escolho o o, d e operation da melhor entrada da possibleChanges

            if (mdlDelta > bestMDLDelta) {
                bestMDLDelta = mdlDelta;

                bestGraph = modifiedGraph;


            }

        }
    return bestMDLDelta;
    }



}
