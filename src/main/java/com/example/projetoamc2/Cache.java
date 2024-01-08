// Acho que esta classe pode ser apagada para o jar da app2
package com.example.projetoamc2;

import java.io.Serializable;
import java.util.*;

public class Cache implements Serializable {
    private Sample sample;
    // HashMap (class value, map(feature index, map(feature value, bitset)))
    //private HashMap<Integer, HashMap<Integer, HashMap<Integer, BitSet>>> map;
    private HashMap<Integer, HashMap<Integer, HashMap<Integer, BitSet>>> map;

    private int sSize;
    private int noFeatures;
    private int noClasses;


    public Cache(Sample sample) {
        this.sample = sample;
        this.sSize = sample.getLen();
        this.noFeatures = sample.noColumns()-1;
        this.noClasses = sample.getDomain(noFeatures);
        this.map = new HashMap<>();

        for (int c = 0; c<noClasses; c++) {
            for (int f = 0; f<sample.noColumns(); f++) {
                for (int v = 0; v< sample.getDomain(f); v++) {
                    if (!map.containsKey(c)) {
                        map.put(c, new HashMap<>());
                    }
                    if (!map.get(c).containsKey(f)) {
                        map.get(c).put(f, new HashMap<>());
                    }
                    if (!map.get(c).get(f).containsKey(v)) {
                        map.get(c).get(f).put(v, new BitSet(sSize));
                    }

                }
            }
        }

        for (int i = 0; i<sSize; i++) {
            int c = sample.getClassValue(i);
            for (int f = 0; f<noFeatures; f++) {
                int v = sample.getFeatureValue(i, f);
                map.get(c).get(f).get(v).set(i);
            }
        }
    }
    // fazer getter que, dado (c, di, i), devolve a key da lista de índices correspondente.

    // Debug
    public static String bitsetstr(BitSet bitSet) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitSet.size(); ++i) {
            sb.append(bitSet.get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    public int updated_count(List<Integer> vars, List<Integer> vals) {
        BitSet res = new BitSet(sSize);
        res.set(0,sSize);
        List<Integer> varsCopy = new ArrayList<>(vars);
        List<Integer> valsCopy = new ArrayList<>(vals);

        int c = valsCopy.get(1);
        varsCopy.remove(1);
        valsCopy.remove(1);



        //System.out.println(bitsetstr(res));
        for (int i = 0;i < varsCopy.size(); i++) {
            //List<Integer> key = Arrays.asList(c, vars.get(i), vals.get(i));
            System.out.println("c="+c+",i="+vars.get(i)+",di="+vals.get(i));
            if (map.containsKey(c) && map.get(c).containsKey(varsCopy.get(i)) && map.get(c).get(varsCopy.get(i)).containsKey(valsCopy.get(i))) {
                BitSet b = map.get(c).get(varsCopy.get(i)).get(valsCopy.get(i));
                res.and(b);
            } else {
                System.out.println("Key not found");
                res.set(0,sSize, false);
            }
        }
        //System.out.println("isto está a funcionar?");
        //ystem.out.println(bitsetstr(res));
        return res.cardinality();
    }

}
