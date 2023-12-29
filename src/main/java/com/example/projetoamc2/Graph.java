package com.example.projetoamc2;

import java.util.*;

public class Graph {

    private final int dim;
    private final HashMap<Integer, LinkedList<Integer>> adj_lists = new HashMap<>();

    Graph(int d) {
        this.dim = d;
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
            System.out.println("Edge ("+o+","+d+") can't be inverted because it doesn't exist.");
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
    public boolean connected (int o, int d) {
        return !(BFStraversal(o, d) == null);
    } //não sei se é melhor usar o BFS ou o DFS, mas acho que é indiferente maybe

    public LinkedList<Integer> parents(int child) {
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

    public LinkedList<Integer> BFS(int startNode) {
        return BFStraversal(startNode, -1);
    }


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

    public LinkedList<Integer> DFS(int o) {
        /*
         * to_visit é a pilha (stack) com os nós a serem
         * visitados. visited é a lista dos nós que em algum
         * ponto já foram adicionados à stack.
         * Quando um nó é visitado, ele é adicionado ao res
         * e os seus filhos que não estão no visited são
         * adicionados à stack.
         */
        LinkedList<Integer> res = new LinkedList<>();
        Stack<Integer> to_visit = new Stack<>();
        LinkedList<Integer> visited = new LinkedList<>();
        to_visit.push(o);
        visited.add(o);
        while (!to_visit.empty()) {
            int node = to_visit.pop();
            res.add(node);
            for (int child : offspring(node)) {
                if (!visited.contains(child)) {
                    to_visit.push(child);
                    visited.add(child);
                }
            }
        }
        return res;
    }

    // comentei tudo o que está abaixo disto porque ainda não acabei :P
    /*

    public double MDL(Sample s) {

    }

    // nota: esta expressão não é multiplicada por m porque está simplificada. aka aquelas contas q vos mandei no whatsapp para verificarem (depois de acabarem eu mando as minhas mas não quero alterar os resultados experimentais HAHHA rct)
    public double sum_I_T(Sample s) {
        for (int c = 0; c < s.domain[this.dim]; c++) {
            int count_c = s.count(new int[]{this.dim},new int []{c});
            for (int i = 0; i < this.dim; i++) {
                for (int di = 0; di < s.domain[i]; di++) {
                    int count_di_c = s.count(di_c_inds, di_c_vals);
                    for (wi : this.possibleParentValues(i)) {
                        System.out.print("Recuso-me a implementar isto (que devia ser uma conta fácil) até alterar o count (detesto arrays) :C");
                    }
                }
            }
        }
    }
     */
}

