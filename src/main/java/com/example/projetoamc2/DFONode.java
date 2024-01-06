package com.example.projetoamc2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DFONode implements Serializable {
    public int nodei;
    public LinkedList<Integer> parents_ind;
    public HashMap<List<Integer>, Integer> tthetas = new HashMap<>();

    public DFONode(Graph g, Sample sample, Double S, int node) {
        /*nodei = node;
        parents_ind = g.parents(node);
        LinkedList<LinkedList<Integer>> parents_vals = g.possibleParentValues(sample, parents_ind);
        for (LinkedList<Integer> parent_val : parents_vals) {
            double res_it = 0;
            for (int c = 0; c < s.getDomain(this.getDim()); c++) {


                for (int di = 0; di < s.getDomain(node); di++) {


                    for (LinkedList<Integer> parent_set : this.possibleParentValues(s, parents)) {

                        LinkedList<Integer> values = new LinkedList<>(List.of(di));                    // Vector with
                        values.addAll(new LinkedList<Integer>(List.of(c)));
                        values.addAll(parent_set); // variable values
                        // este vetor está ordenado (di, c, valores(pais))

                        int T_di_wi_c = s.count(vars.subList(0, vars.size()), values.subList(0, values.size()));
                        int T_wi_c = s.count(vars.subList(1, vars.size()), values.subList(1, values.size()));

                        res_it += T_di_wi_c *
                                BayesUtils.log2((double) (T_di_wi_c * T_c) / (T_di_c * T_wi_c));

                    }
                }
            }
        }
    }

    public double calculateTheta (int nodei){
        LinkedList<Integer> parents = getPossibleParents(nodei);
        LinkedList<LinkedList<Integer>> possibleParents = possibleParentValues(Sample, parents);
                for (LinkedList<Integer> combinacao : combinacoesParentais) {
                    countdiwi = subList(1,pais.size)
                    countwi = subList(0, pais.size)
                }
            }

            // Outros métodos...
        }






*/
    }

}
