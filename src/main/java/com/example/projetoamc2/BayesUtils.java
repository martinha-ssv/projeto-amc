package com.example.projetoamc2;

import java.io.*;

public class BayesUtils {
    public static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }
    public static Object import_object(String path) {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = (Object) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            System.out.println("A Rede Bayesiana foi importada com sucesso.");
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void export_object(String path, Object obj) {
        if (path == null || path.isEmpty()) path = "bayesNet.txt";
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(obj);
            objectOut.close();
            fileOut.close();
            System.out.println("A Rede Bayesiana foi exportada com sucesso.");
            System.out.print("To find it, check the "); System.out.print(path); System.out.print(" directory.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
