package com.example.projetoamc2;

/*Esta classe Sample tem um construtor que lê um ficheiro csv e constrói uma lista de arrays
 correspondente às linhas desse ficheiro. Faltam terminar várias funções do enunciado do projeto
de AMC de 2023. Neste exemplo, o ficheiro bcancer.csv deve estar na pasta do projeto para funcionar.
Não é na pasta src, é na pasta acima.
fmd
*/
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Sample {

    private ArrayList<int []> lista; // Lista de Samples
    private int [] domain = null;    // Array de domínios, deve ser actualizado no método add.

    public Sample() {
        this.lista = new ArrayList<int []>();
    }

    static int[] convert (String line) {
        String csvSplitBy = ",";
        String[] strings     = line.split(csvSplitBy);
        int[] stringToIntVec = new int[strings.length];
        for (int i = 0; i < strings.length; i++)
            stringToIntVec[i] = Integer.parseInt(strings[i]);
        return stringToIntVec;
    }

    public Sample(String csvFile) {
        this.lista = new ArrayList<int []>();
        this.domain = null;
        BufferedReader br = null;
        String line = "";


        try {br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                add(convert(line));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void add (int[] v){
        //Para atualizar os domínios - confirmar se está bem
        if (domain == null) {
            // Se os domínios ainda não foram inicializados, inicialize-os com os valores do primeiro vetor
            domain = Arrays.copyOf(v, v.length);
        }
        for (int i = 0; i < v.length; i++) {
            if (domain[i] <= v[i]) {
                domain[i]=v[i]+1;
            }
        }
        lista.add(v);
    }

    public int length (){
        return lista.size();
    }

    public int[] element (int i){
        return lista.get(i);
    }

    // Vamos ter de dar override a isto por completo pq isto não vai ser muito sloooooowwwww -> estava a pensar em usar nested hashmaps
    public int count(int[] var, int[] val) { //Falta testar
        int r = 0;
        if (var.length != val.length) {
            return r; // Retorna 0 se os comprimentos são diferentes
        }
        // Iterar sobre cada vetor dentro da lista de Samples
        for (int i = 0; i < lista.size(); i++) {
            int[] sample = lista.get(i); // Obter o vetor de Sample atual
            boolean match = true;
            // Verificar se os valores em 'sample' correspondem aos valores em 'val' nas posições 'var'
            for (int j = 0; j < var.length; j++) {
                // Verifica se a posição dada por var[j] é válida
                if (var[j] >= sample.length || sample[var[j]] != val[j]) {
                    match = false;
                    break; // Não é uma correspondência, não precisa verificar mais
                }
            }
            if (match) {
                r++;
            }
            // Todos os valores correspondem, incrementa o contador

        }
        return r;
    }


    public int domain(int[] var){
        if (domain==null) {
            int r = 0;
        }
        int r=1;
        for (int pos : var) {
            assert domain != null;
            r *= domain[pos];
        }
        return r;
    }


    @Override
    public String toString() {
        StringBuilder s= new StringBuilder("\n[\n");
        if (!lista.isEmpty()) s.append(Arrays.toString(lista.getFirst()));
        for (int i=1; i<lista.size();i++)
            s.append("\n").append(Arrays.toString(lista.get(i)));
        s.append("\n]");

        return "Sample " + s;
    }


}
