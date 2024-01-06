package com.example.projetoamc2;

import java.io.Serializable;
import java.util.*;
import java.util.Arrays;

public class Graph implements Serializable {

    private final int dim;
    private final HashMap<Integer, LinkedList<Integer>> adj_lists = new HashMap<>();
    private final HashMap<Integer, Double> partial_MDLs = new HashMap<>();

    Graph(int d) {
        this.dim = d; // nota: dim deve ser igual ao número de variáveis menos a classe.
        for (int i = 0; i<this.dim; i++) {
            adj_lists.put(i, new LinkedList<>());

        }
    }

    /**
     *
     * @return o nº de nós do gráfico.
     */
    public int getDim() {
        return dim;
    }

    /**
     * Adiciona uma aresta de <i>o</i> para <i>d</i>.
     * @param o origem
     * @param d destino
     */
    public void addEdge(int o, int d) {
        if (!this.edgeQ(o,d)) {
            adj_lists.get(o).add(d);
        } else {
            System.out.println("Edge already existed.");
        }
    }

    /**
     * Remove a aresta de <i>o</i> para <i>d</i>.
     * @param o origem
     * @param d destino
     */
    public void removeEdge(int o, int d) {
        if (this.edgeQ(o,d)) {
            // Integer está entre parênteses porque, normalmente, se o método remove receber um inteiro, acha
            // que é o índice do elemento que queremos remover. Temos de especificar que estamos a passar o
            // objeto que queremos que seja removido.
            adj_lists.get(o).remove((Integer) d);
        } else {
            System.out.println("Edge ("+o+","+d+") can't be removed because it doesn't exist.");
        }
    }

    /**
     * Se existir uma aresta de <i>o</i> para <i>d</i>, este método inverte-a.
     * @param o origem
     * @param d destino
     */
    public void invertEdge(int o, int d) {
        if (this.edgeQ(o, d)) {
            this.addEdge(d,o);
            this.removeEdge(o,d);
        } else {
            System.out.println("Edge ("+o+","+d+") can't be inverted because it doesn't exist.");
        }
    }


    /**
     *
     * @param o origem
     * @param d destino
     * @return <b>true</b> se existir uma aresta de <i>o</i> para <i>d</i>, <b>false</b> caso contrário
     */
    public boolean edgeQ(int o, int d) {
        if (this.adj_lists.containsKey(o)) {
            return adj_lists.get(o).contains(d);
        } else {
            return false;
        }
    }

    /**
     *
     * @param o origem
     * @param d destino
     * @return <i>true</i> se existir um caminho de <i>o</i> para <i>d</i>, <i>false</i> caso contrário.
     */
//este pode ser o haspath
    public boolean connected(int o, int d) {
        return !(DFStraversal(o,d) == null);
    }

    /**
     *
     * @param child Node for which you want the list of parents
     * @return List of parents of node <i>child</i>.
     */
    public LinkedList<Integer> parents(int child) {
        System.out.println("Calculating parents");
        LinkedList<Integer> res = new LinkedList<>();
        for (int parent = 0; parent < this.dim; parent++) {
            if (edgeQ(parent, child)) {
                res.add(parent);
            }
        }
        return res;
    }

    @Override
    public String toString() {
        String res = "Graph: \n dim=" + dim + ",\n";
        res += "edges = {\n";
        for (int o = 0; o < dim; o++) {
            res += "From "+o+":\n";
            for (int d = 0; d < dim; d++) {
                if (edgeQ(o, d)) {
                    res += o + " -> " + d + "\n";
                }
            }
        }
        return res;
    }

    /**
     *
     * @param o origem
     * @return a lista de sucessores do nó <i>o</i>, isto é, a lista de nós para os quais há uma aresta vinda de <i>o</i>.
     */
    public LinkedList<Integer> offspring(int o) {
        LinkedList<Integer> res = new LinkedList<>();
        for (int d = 0; d < dim; d++) {
            if (edgeQ(o, d)) {
                res.add(d);
            }
        }
        return res;
    }

    /**
     *
     * @param startNode
     * @return the list of nodes reachable from the <i>startNode</i>.
     */
    //provavelmente podemos apagar
    public LinkedList<Integer> BFS(int startNode) {
        return BFStraversal(startNode, -1);
    }
    public LinkedList<Integer> DFS(int origin) {
        return DFStraversal(origin, -1);
    }

    private LinkedList<Integer> DFStraversal(Integer origin, Integer target) {
        LinkedList<Integer> res = new LinkedList<>();
        LinkedList<Integer> visited = new LinkedList<>();
        Queue<Integer> to_visit = new LinkedList<>();
        to_visit.add(origin);
        visited.add(origin);

        while (!to_visit.isEmpty()) {
            int node = to_visit.poll();
            if (node == target) {
                return visited; // Return immediately if target is found
            }
            res.add(node);
            for(int child : offspring(node)) {
                if(!visited.contains(child)) {
                    to_visit.add(child);
                    visited.add(child);
                }
            }
        }
        return target < 0 ? res : null;
    }
//provavelmente podemos apagar
    private LinkedList<Integer> BFStraversal(Integer startNode, Integer target) {
        /*
         * to_visit é a fila (queue) com os nós a serem
         * visitados. visited é a lista dos nós que em algum
         * ponto já foram adicionados à queue.
         * Quando um nó é visitado, ele é adicionado ao res
         * e os seus filhos que não estão no visited são
         * adicionados à queue (para não serem visitados 2x).
         */
        LinkedList<Integer> visited = new LinkedList<>();
        Queue<Integer> to_visit = new LinkedList<>();
        to_visit.add(startNode);
        visited.add(startNode);

        while (!to_visit.isEmpty()) {
            int node = to_visit.poll();
            if (node == target) {
                return visited; // Return immediately if target is found
            }
            for (int child : offspring(node)) {
                if (!visited.contains(child)) {
                    to_visit.add(child);
                    visited.add(child);
                }
            }

        }

        return target < 0 ? visited : null; // Return visited list or null based on the target (if target is < 0
        // return visited, else return null. Só chega ao caso em que devolve null SE não encontrar o target na BFStraversal a
        // partir do start node.
    }
//podemos apagar porque este é o connected
    private boolean haspathDFS(int o, int d, Set<Integer> visited) {
        if (o == d) {
            return true;
        }

        visited.add(o);
        if (adj_lists.containsKey(o)) {
            for (int neighbor : adj_lists.get(o)) {
                if (!visited.contains(neighbor)) {
                    if (haspathDFS(neighbor, d, visited)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean operationAllowed(int o, int d, int operation) {
        return (operation <=2 && operation >= 0) && !((this.createsCycle(o,d,operation)) || this.exceedsK(o,d,operation));
    }

    /**
     *
     * @param o
     * @param d
     */

    private boolean addcreatesCycle(int o, int d) {
        return !(DFStraversal(d,o)==null);
    }

    /**
     *
     * @param o
     * @param d
     */
    private boolean invertcreatesCycle(int o, int d) {
        if (!adj_lists.containsKey(o)) {
            System.out.println("Erro: o nó não tem vizinhos, não há arestas");
        }
        // Remove o nó de o para d mudar para removeedge
        if (adj_lists.containsKey(o)) {
            adj_lists.get(o).remove((Integer) d);
        }
        // Check if adding the edge in the reverse direction creates a cycle
        boolean createsCycle = addcreatesCycle(d, o);
        // Add the edge back to its original direction, usar addedge
        adj_lists.get(o).add(d);

        return createsCycle;
    }
    /**
     *
     * @param o
     * @param d
     * @param operation Can take the value 1 - Edge inversion - and 2 - Edge addition.
     * @return Whether the (2) addition of edge o->d or (1) inversion of edge o->d (obtaining edge d->o) creates a cycle in the graph or not.
     */
    private boolean createsCycle(int o, int d, int operation) {
        if (operation == 2) {
            return addcreatesCycle(o,d);
        }
        else if (operation==1) {
            return invertcreatesCycle(o,d);
        }
        System.out.println("Operation not allowed");
        return false;
    }


    private boolean exceedsK(int o, int d, int operation) {
        if (operation==2) {
            return (parents(d).size()>1);
        }
        else if (operation==1) {
            return (parents(o).size()>1);
        }
        else {
            System.out.println("Operation not allowed");
            return false;
        }
    }


    public Graph copy() {
        Graph g = new Graph(this.dim);
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
    public double MDL(Sample s, boolean recalculate) {
        //começamos pelo termo independente de i
       double res = -(BayesUtils.log2(s.length())/2)*(s.getDomain(this.getDim())-1);
       for (int i = 0; i < s.no_features(); i++) {
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
    public double MDLdelta(Sample s, int o, int d, int operation) {

        if (this.operationAllowed(o,d,operation)) {
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
        } else {
            System.out.println("Operation not allowed.");
            return -1;
        }
    }

    /**
     *
     * @param s Sample
     * @param node Node for which we desire to calculate a partial MDL term (including both the i-dependent complexity term and the i-dependent LL term)
     * @param recalculate If we desire to recalculate the property. Else, the function behaves as a getter.
     * @return Partial MDL score for a given node, storing it in the instance variable.
     */
    public double node_MDL(Sample s, int node, boolean recalculate) {
        // Pais do nó
        LinkedList<Integer> parents = this.parents(node);
        if (recalculate) {
            // Primeiro, o termo do log e do produtório.
            double res_prod = (-BayesUtils.log2(s.length()) / 2) *
                    (s.getDomain(this.getDim())) * // |Dc|
                    (s.getDomain(node) - 1); // |Di - 1|
            for (int j : parents) {
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


            double res_it = 0;
            for (int c = 0; c < s.getDomain(this.getDim()); c++) {

                int T_c = s.count(List.of(this.getDim()), List.of(c));

                for (int di = 0; di < s.getDomain(node); di++) {

                    int T_di_c = s.count(List.of(node, this.getDim()), List.of(di, c));

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
    private LinkedList<LinkedList<Integer>> possibleParentValues(Sample s, LinkedList<Integer> parents) {
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
            LinkedList<LinkedList<Integer>> combs = this.possibleParentValues(s, parents); // Chamamos a função para os pais restantes
            for (int i = 0; i<s.getDomain(first_parent);i++) {
                for (int j = 0; j<combs.size(); j++) {
                    LinkedList<Integer> temp = new LinkedList<>(List.of(i));
                    temp.addAll(combs.get(j));
                    options.add(temp);
                }
            }
        }
        return options;
    }

    public static void main(String[] args) {
        Sample s = new Sample("/Users/gabrielagomes/IdeaProjects/projeto-amc/data/raw/bcancer.csv");
        Graph g = new Graph(s.no_features()-1);
        System.out.println(g);
        g.addEdge(0, 2);
        g.addEdge(1, 0);
        g.addEdge(2, 3);
        g.addEdge(1,3);
        System.out.println(g.addcreatesCycle(2,3));
        System.out.println(g.invertcreatesCycle(1,3));


    }

}

