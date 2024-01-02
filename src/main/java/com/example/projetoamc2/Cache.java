package com.example.projetoamc2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Cache implements Serializable {
    private Sample sample;
    private HashMap<List<Integer>, Integer> map;

    public Cache(Sample sample) {

    }
}
