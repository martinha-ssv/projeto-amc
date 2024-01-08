package com.example.projetoamc2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GraphStructure implements Serializable {

    protected int dim;
    public int k = 2;
    protected final HashMap<Integer, LinkedList<Integer>> parents_lists= new HashMap<>();

    GraphStructure(int d) {
        System.out.println("Building empty graph");
        this.dim = d; // nota: dim deve ser igual ao número de variáveis menos a classe.
        for (int i = 0; i<this.dim; i++) {
            parents_lists.put(i, new LinkedList<Integer>());
            System.out.println(this.parents(i));
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
            parents_lists.get(d).remove((Integer) o);
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
}
