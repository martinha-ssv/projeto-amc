package com.example.projetoamc2;

import java.io.Serializable;
import java.util.*;

public class Graph implements Serializable {

    private  int dim;
    public int k = 2;

    private Sample sample;
    private HashMap<Integer, LinkedList<Integer>> parents_lists = new HashMap<>();
    private HashMap<Integer, Double> partial_MDLs = new HashMap<>();


    Graph(int d, Sample s) {
        System.out.println("Building empty graph");
        this.dim = d; // nota: dim deve ser igual ao número de variáveis menos a classe.
        this.sample = s;
        for (int i = 0; i<this.dim; i++) {
            parents_lists.put(i, new LinkedList<Integer>());
            System.out.println(this.parents(i));
        }
        MDL(s,true);
    }

    public Graph(int d, Sample s, String random) {
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
            parents_lists.get(d).add(o);
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
            parents_lists.get(d).remove(o);
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
        if (this.parents_lists.containsKey(d)) {
            return parents_lists.get(d).contains(o);
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

    // DEBUG ONLY

    private boolean hasCycle() {
        boolean hasCycle = false;
        for (int o = 0; o<this.dim; o++) {
            for (int d = 0; d<this.dim; d++) {
                if (o!=d) {
                    if (this.connected(o,d) && this.connected(d,o)) return true;
                }
            }
        }
        return false;
    }
    /**
     *
     * @param child Node for which you want the list of parents
     * @return List of parents of node <i>child</i>.
     */
    public LinkedList<Integer> parents(int child) {
        if (parents_lists.containsKey(child)) return parents_lists.get(child);
        else return new LinkedList<>();
    }

    @Override
    public String toString() {
        String res = "Graph: \n dim=" + dim + ",\n";
        res += "edges = {\n";
        for (int o = 0; o < dim; o++) {
            res += "Parents of "+o+":\n";
            LinkedList<Integer> parents = this.parents(o);
            for (int d = 0; d < parents.size(); d++) {
                res += parents.get(d) + " -> " + o + "\n";
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



    public boolean operationAllowed(int o, int d, int operation) {
        if (o==d) return false;
        if (!(operation <= 2 && operation >= 0)) return false;
        if (this.edgeQ(o,d)) {
            if (operation == 0) return true;
            if (operation == 2) return false;
            return !((this.createsCycle(o, d, operation)) || this.exceedsK(o, d, operation));
        } else {
            return (operation == 1 && !((this.createsCycle(o, d, operation)) || this.exceedsK(o, d, operation)));
        }
    }

    /**
     *
     * @param o
     * @param d
     */

    private boolean addcreatesCycle(int o, int d) {
        return (DFStraversal(d,o)!=null);
    }

    /**
     *
     * @param o
     * @param d
     */
    private boolean invertcreatesCycle(int o, int d) {
        this.removeEdge(o,d);
        // Check if adding the edge in the reverse direction creates a cycle
        boolean createsCycle = addcreatesCycle(d, o);
        this.addEdge(o,d);

        return createsCycle;
    }
    /**

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

    public void applyOperation(int o, int d, int operation){
        if (operation == 0){
            this.removeEdge(o, d);
        } else if (operation == 1) {
            this.invertEdge(o, d);
        } else if (operation == 2) {
            this.addEdge(o,d);
        }
    }

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
            System.out.println(g.DFStraversal(0,1)!=null);
            System.out.println(g.addcreatesCycle(1,0));
            System.out.println(g);
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

