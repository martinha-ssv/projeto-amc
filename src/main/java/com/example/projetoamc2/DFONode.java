package com.example.projetoamc2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DFONode implements Serializable {
    public int nodei;
    public LinkedList<Integer> parents_ind;
    public HashMap<List<Integer>, Double> tthetas = new HashMap<>();

    @Override
    public String toString() {
        return "DFONode{" +
                "tthetas=" + tthetas.toString() +
                '}';
    }

    public DFONode(Graph g, Sample sample, Double S, int node) {
        nodei = node;
        parents_ind = g.parents(node);
        int Di = sample.getDomain(node);
        LinkedList<LinkedList<Integer>> parents_vals = g.possibleParentValues(sample, parents_ind);
        for (LinkedList<Integer> parent_val : parents_vals) {
            LinkedList<Integer> key;
            for (int c = 0; c < sample.getDomain(g.getDim()); c++) {


                for (int di = 0; di < Di; di++) {
                    key = new LinkedList<>(List.of(di));                    // Vector with
                    key.add(c);
                    key.addAll(parent_val); // variable values
                    // este vetor está ordenado (di, c, valores(pais))

                    int T_di_wi_c = sample.count(parent_val.subList(0, parent_val.size()), key.subList(0, key.size()));
                    int T_wi_c = sample.count(parent_val.subList(1, parent_val.size()), key.subList(1, key.size()));

                    tthetas.put(key, (double) ((T_di_wi_c + S) / ( T_wi_c + S * Di)));

                }
            }
        }

    }
    public static void main(String[] args) {
        Sample sample = new Sample("data/raw/bcancer.csv");
        Graph g = new Graph(sample.noColumns()-1, sample, "");
        System.out.println(g);
        for (int i =0; i< sample.noColumns()-1; i++) {
            DFONode node= new DFONode(g, sample, 0.5, i);
            System.out.println(node.toString());
        }



    }
}
