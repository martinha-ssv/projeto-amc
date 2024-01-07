// Acho que esta classe pode ser apagada para o jar da app2
package com.example.projetoamc2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Cache extends Sample implements Serializable {
    private Sample sample;
    private HashMap<List<Integer>, List<Integer>> map;

    public Cache(Sample sample) {
        // Acho que vale a pena guardar, para cada variável, as combinações dos valores q essa variável pode tomar e os valores da classe
        // Ou seja, no total, \sum_Xi |Di|*|DC|
    }
    // fazer getter que, dado (c, di, i), devolve a key da lista de índices correspondente.

    // fazer função que devolve lista de todos os vetores com C==c.

    // fazer função que devolve o tamanho de uma lista de indices quando é count(di, c)

    // fazer função que itera sobre a amostra no subconjunto de índices e faz count aí
        // "modular" a função count: match devia ser uma função of its own, em que estamos só a ver se o vetor cumpre as condições
        // assim, podia reutilizar esse bocado de código.

}
