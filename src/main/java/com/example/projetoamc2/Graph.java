package com.example.projetoamc2;

import java.io.Serializable;
import java.util.*;

public class Graph extends GraphStructure implements Serializable {

    private Sample sample;

    private final HashMap<Integer, Double> partial_MDLs = new HashMap<>();


    Graph(int d, Sample s) {
        super(d);
        this.sample = s;
        for (int i = 0; i<this.dim; i++) {
            parents_lists.put(i, new LinkedList<Integer>());
            System.out.println(this.parents(i));
        }
        MDL(s,true);
    }

    public Graph(int d, Sample s, String random) {
        super(d);
        System.out.println("Building random graph");
        this.sample = s;
        this.dim = d; // Nota: dim deve ser igual ao número de variáveis menos a classe, porque não representamos a classe no grafo (redundante).
        for (int i = 0; i<this.dim; i++) {
            parents_lists.put(i, new LinkedList<Integer>());
            System.out.println(this.parents(i));
        }

        // List of nodes
        LinkedList<Integer> nodes = new LinkedList<>();
        for (int i = 0; i<d; i++) nodes.add(i);

        // Random topological ordering of nodes -> Fischer-Yates
        int temp;
        int j;
        Random rand = new Random();
        for (int i = d-1; i>0; i--) {
            // swap i for the element at j in {0, ..., i-1}
            j = rand.nextInt(i);
            temp = nodes.get(i);
            nodes.set(i, nodes.get(j));
            nodes.set(j, temp);
        }

        System.out.println(nodes);
        // Random assignment of incoming degree (0, 1 or 2)
        // For each node (in int order), for each j from 0 to incoming degree, get random int (0->order) to determine the source of that edge.
        for (int i = d-1; i>0; i--) {
            for (int in_degree = rand.nextInt(k+1); in_degree > 0; in_degree--) {
                this.addEdge(rand.nextInt(i), i);
            }
        }
        MDL(s,true);
    }

    public void addNode() {
        parents_lists.put(this.getDim(), new LinkedList<>());
        this.dim += 1;
    }

    /**
     *
     * @return o nº de nós do gráfico.
     */


    public Graph copy() {
        Graph g = new Graph(this.dim, this.sample);
        for (int i = 0; i < g.dim; i++) {
            for (int j = 0; j < g.dim; j++) {
                if (this.edgeQ(i,j)) {
                    g.addEdge(i,j);
                }
            }
        }
        return g;
    }

    /**
     *
     * @param s Sample
     * @param recalculate If we desire to recalculate the property. Else, the function behaves as a getter.
     * @return MDL score for the graph given the sample s.
     */
    // nota: estamos a usar a 2ª fórmula
    public Double MDL(Sample s, boolean recalculate) {
        System.out.println("Getting MDL");
        //começamos pelo termo independente de i
       double res = -(BayesUtils.log2(s.length())/2)*(s.getDomain(this.getDim())-1);
       for (int i = 0; i < s.noColumns(); i++) {
           double node_res = node_MDL(s, i, recalculate);
           res += node_res;
       }
       return res;
    }

    /**
     *
     * @param s Sample
     * @param o Edge Origin
     * @param d Edge Destination
     * @param operation 0 - Remove Edge;
     *                  1 - Invert Edge;
     *                  2 - Add Edge;
     * @return MDL variation
     */
    public Double MDLdelta(Sample s, int o, int d, int operation) {
            System.out.println("Getting MDLdelta");
            Graph temp = this.copy();

            if (operation == 1) {
                temp.invertEdge(o,d);
                return (temp.node_MDL(s, d, true) + temp.node_MDL(s,o,true))-(this.partial_MDLs.get(d)+this.partial_MDLs.get(o));

            } else if (operation == 0) {
                temp.removeEdge(o,d);
            } else if (operation == 2) {
                temp.addEdge(o,d);
            }

            return temp.node_MDL(s, d, true) - this.partial_MDLs.get(d);
    }

    /**
     *
     * @param s Sample
     * @param node Node for which we desire to calculate a partial MDL term (including both the i-dependent complexity term and the i-dependent LL term)
     * @param recalculate If we desire to recalculate the property. Else, the function behaves as a getter.
     * @return Partial MDL score for a given node, storing it in the instance variable.
     */
    public Double node_MDL(Sample s, int node, boolean recalculate) {
        System.out.println("Getting node MDL");
        // Pais do nó
        LinkedList<Integer> parents = this.parents(node);
        if (recalculate) {
            // Primeiro, o termo do log e do produtório.
            Double res_prod = (-BayesUtils.log2(s.length()) / 2) *
                    (s.getDomain(this.getDim())) * // |Dc|
                    (s.getDomain(node) - 1); // |Di - 1|
            for (int j = 0; j<parents.size(); j++) {
                res_prod *= s.getDomain(j);
            }

            // Depois, o termo da informação mútua.
            // Não acabei.


            LinkedList<Integer> vars = new LinkedList<>(List.of(node));                             //Vector with variable
            vars.addAll(new LinkedList<Integer>(List.of(this.getDim())));
            vars.addAll(parents); // indices (for count conditions)
            // este vetor está ordenado (i, indice(c), indices(pais))

            // subList(0,2) -> di, c
            // subList(1,size) -> wi,c
            // subList(0, size) -> di, wi, c
            // subList(1,2) -> c


            Double res_it = 0.0;
            for (int c = 0; c < s.getDomain(this.getDim()); c++) {

                int T_c = s.count(List.of(this.getDim()), List.of(c));

                for (int di = 0; di < s.getDomain(node); di++) {

                    int T_di_c = s.count(List.of(node, this.getDim()), List.of(di, c));

                    for (LinkedList<Integer> parent_set : this.possibleParentValues(s, new LinkedList<>(parents))) {

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
            this.partial_MDLs.put(node, res_prod+res_it);
            System.out.print("I_t for "); System.out.print(node); System.out.print(": "); System.out.println(res_it);
        }

        return this.partial_MDLs.get(node);
    }


    /**
     *
     * @param s Sample de onde obtém o domain das variáveis
     * @param parents Os pais (quando se chama a função em recursão, são os que ainda não foram processados) do nó
     * @return LinkedList de LinkedLists de
     */
    public LinkedList<LinkedList<Integer>> possibleParentValues(Sample s, LinkedList<Integer> parents) {
        //System.out.println("Getting possible Parents");
        LinkedList<LinkedList<Integer>> options = new LinkedList<>();
        // Caso de terminação: se só sobrar uma variável para combinar, devolvemos lista com os valores possíveis da variável.
        if (parents.size() == 1) {
            for (int i = 0; i<s.getDomain(parents.getFirst()); i++) {
                options.add(new LinkedList<>(List.of(i)));
            }
        // Caso de recursão: se ainda tivermos variáveis para combinar, então pegamos na primeira e, para cada valor
        // que ela toma, concatenamo-lo com cada vetor de combinações das restantes variáveis.
        } else if (parents.size() > 1) {
            int first_parent = parents.removeFirst(); // Tiramos a 1ª variável do vetor de pais
            LinkedList<LinkedList<Integer>> combs = this.possibleParentValues(s, new LinkedList<>(parents)); // Chamamos a função para os pais restantes
            for (int i = 0; i<s.getDomain(first_parent);i++) {
                for (LinkedList<Integer> comb : combs) {
                    LinkedList<Integer> temp = new LinkedList<>(List.of(i));
                    temp.addAll(comb);
                    options.add(temp);
                }
            }
        }
        return options;
    }

    public static void main(String[] args) {
        Sample s = new Sample("data/raw/bcancer.csv");
        //Graph g = new Graph(s.noColumns()-1,s,"random");



            Graph g = new Graph (4,s);
            System.out.println(g);
            //g.addEdge(0,3);
            g.addEdge(3,1);
            g.addEdge(0,1);
            System.out.println(g.operationAllowed(0,1, 0));
            //System.out.println(g.addcreatesCycle(1,0));
            System.out.println(g);
            g.operationAllowed(0,1,0);
            /*g.addEdge(0,5);
            g.addEdge(2,1);
            g.addEdge(2,3);
            g.addEdge(3,2);
            g.addEdge(2,4);
            g.addEdge(4,4);
            g.addEdge(4,3);
            g.addEdge(4,5);
            g.addEdge(5,4);
            System.out.println(g.offspring(4));*/


    }

}

