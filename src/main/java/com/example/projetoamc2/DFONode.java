package com.example.projetoamc2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DFONode implements Serializable {
    public int nodei;
    public LinkedList<Integer> parents_ind;
    public HashMap<List<Integer>, Double> tthetas = new HashMap<>();

    private BayesianNetwork bayes;

    @Override
    public String toString() {
        String start = "i="+ Integer.toString(nodei)+",di=";
        String res = "";
        for (List<Integer> parent_val : tthetas.keySet()) {
            res += start+Integer.toString(parent_val.get(0))+"parents="+parents_ind+",c="+parent_val.get(1)+",valparents="+parent_val.subList(2,parent_val.size()).toString()+", Theta="+Double.toString(tthetas.get(parent_val))+"\n";
        }
        return res;
    }

    public DFONode(Sample sample, Double S, int node, BayesianNetwork bn) {
        nodei = node;
        bayes = bn;
        Graph g = bn.DAG;
        parents_ind = g.parents(nodei);
        int Di = sample.getDomain(node);

        if (!parents_ind.isEmpty()) {

            LinkedList<LinkedList<Integer>> parents_vals = g.possibleParentValues(sample, new LinkedList<>(parents_ind));

            LinkedList<Integer> key_vars = new LinkedList<>(List.of(node, sample.noColumns() - 1));
            key_vars.addAll(parents_ind);

            for (LinkedList<Integer> parent_val : parents_vals) {

                for (int c = 0; c < sample.getDomain(g.getDim()-1); c++) {

                    for (int di = 0; di < Di; di++) {
                        LinkedList<Integer> key = new LinkedList<>(List.of(di, c));                    // Vector with
                        key.addAll(parent_val); // variable values
                        // este vetor está ordenado (di, c, valores(pais))
                        int T_di_wi_c = sample.count(key_vars.subList(0, key_vars.size()), key.subList(0, key.size()));
                        int T_wi_c = sample.count(key_vars.subList(1, key_vars.size()), key.subList(1, key.size()));

                        tthetas.put(key, (double) ((T_di_wi_c + S) / (T_wi_c + S * Di)));

                    }
                }
            }
        } else if (nodei == bn.DAG.getDim()-1) {// No time para ficar bonito, vai martelado (acontece :c)
            LinkedList<Integer> key_vars = new LinkedList<>(List.of(nodei));
            for (int c = 0; c < sample.getDomain(g.getDim()-1); c++) {

                    LinkedList<Integer> key = new LinkedList<>(List.of(c));

                    int T_c = sample.count(key_vars.subList(0, key_vars.size()), key.subList(0, key.size()));
                    int T = sample.getLen();

                    tthetas.put(key, (double) ((T_c + S) / (T + S * Di)));

            }

        } else {
            LinkedList<Integer> key_vars = new LinkedList<>(List.of(node, sample.noColumns() - 1));
            for (int c = 0; c < sample.getDomain(g.getDim()-1); c++) {
                for (int di = 0; di < Di; di++) {
                    LinkedList<Integer> key = new LinkedList<>(List.of(di, c));

                    int T_di_wi_c = sample.count(key_vars.subList(0, key_vars.size()), key.subList(0, key.size()));
                    int T_wi_c = sample.count(key_vars.subList(1, key_vars.size()), key.subList(1, key.size()));

                    tthetas.put(key, (double) ((T_di_wi_c + S) / (T_wi_c + S * Di)));
                }
            }
        }
        System.out.println("End of function: "+parents_ind.toString());

    }

    /**
     *
     * @param vec Vetor amostra, com todas as entradas, para cada variável
     * @return Probabilidade de uma variável tomar um valor di dado que os seus pais tomam valores dij e dada uma classe.
     * Estes valores são obtidos de uma amostra, vetor fornecido com valores para todas as variáveis.
     */
    public Double getTheta(List<Integer> vec, int c) {
        // Começamos a montar a chave para o HashMap
        LinkedList<Integer> vals = new LinkedList<>(List.of(vec.get(this.nodei), c));
        // Wi part do HashMap
        for (int i : parents_ind) {
            vals.add(vec.get(i));
        }
        // Return do theta correspondente à chave construída
        return tthetas.get(vals);
    }



    public static void main(String[] args) {
        Sample sample = new Sample("data/raw/bcancer.csv");
        System.out.println(sample);
        Graph g = new Graph(sample.noColumns()-1, sample);
        g.addEdge(3,2);
        g.addEdge(5,2);
        g.addEdge(5,3);
        g.addEdge(6,4);
        System.out.println(g);
        BayesianNetwork bn = new BayesianNetwork(g, sample, 0.5);

        for (int i = 0; i < bn.DAG.getDim()-1; i++) {
            System.out.println(Integer.toString(i)+" ; parents="+g.parents(i).toString());
        }
        for (int i =0; i< bn.DAG.getDim(); i++) {
            DFONode node= new DFONode(sample, 0.5, i, bn);
            System.out.println(node);
        }


    }
}
