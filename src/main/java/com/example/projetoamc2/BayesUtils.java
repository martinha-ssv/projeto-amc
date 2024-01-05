package com.example.projetoamc2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class BayesUtils {
    public static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }
    public BayesianNetwork import_network(String path) {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            BayesianNetwork bayesNet = (BayesianNetwork) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            System.out.println("A Rede Bayesiana foi importada com sucesso.");
            return bayesNet;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
