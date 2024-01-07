// Acho que esta classe pode ser apagada para o jar da app2
package com.example.projetoamc2;

/*Esta classe Sample tem um construtor que lê um ficheiro csv e constrói uma lista de arrays
 correspondente às linhas desse ficheiro. Faltam terminar várias funções do enunciado do projeto
de AMC de 2023. Neste exemplo, o ficheiro bcancer.csv deve estar na pasta do projeto para funcionar.
Não é na pasta src, é na pasta acima.
fmd
*/
import java.io.*;
import java.util.*;

public class Sample implements Serializable {

    private HashMap<Integer, Integer[]> lista; // Lista de Samples
    private int len = 0;
    private Integer[] domain = null;    // Array de domínios, deve ser actualizado no método add.


    public Sample() {
        this.lista = new HashMap<>();
    }

    static Integer[] convert (String line) {
        String csvSplitBy = ",";
        String[] strings     = line.split(csvSplitBy);
        Integer[] stringToIntVec = new Integer[strings.length];
        for (int i = 0; i < strings.length; i++)
            stringToIntVec[i] = Integer.parseInt(strings[i]);
        return stringToIntVec;
    }

    public Sample(String csvFile) {
        this.lista = new HashMap<>();
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

    public void add(Integer[] v){
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
        lista.put(this.len, v);
        this.len+=1;
    }

    public int noColumns() {
        return lista.get(0).length;
    }

    public String head(int n) {
        StringBuilder s= new StringBuilder("\n[\n");
        if (!lista.isEmpty()) s.append(Arrays.toString(lista.get(0)));
        for (int i=1; i<n;i++)
            s.append("\n").append(Arrays.toString(lista.get(i)));
        s.append("\n]");

        return "Sample " + s;
    }

    public int length (){
        return lista.size();
    }

    public Integer[] element (int i){
        return lista.get(i);
    }

    public int count(List<Integer> var, List<Integer> val) { //Falta testar

        int r = 0;
        if (var.size() != val.size()) {
            return r; // Retorna 0 se os comprimentos são diferentes
        }

        // Iterar sobre cada vetor dentro da lista de Samples
        // Obter o vetor de Sample atual
        for (int i = 0; i < len; i++) {
            Integer[] sample = lista.get(i);
            boolean match = true;
            // Verificar se os valores em 'sample' correspondem aos valores em 'val' nas posições 'var'
            for (int j = 0; j < var.size(); j++) {
                // Verifica se a posição dada por var[j] é válida
                if (var.get(j) >= sample.length || !Objects.equals(sample[var.get(j)], val.get(j))) {
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

    public Integer[] getDomains() {
        return domain;
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

    public int getDomain(int i) {  return this.domain[i];  }

    public int getLen() {return lista.size(); }


    @Override
    public String toString() {
        StringBuilder s= new StringBuilder("\n[\n");
        if (!lista.isEmpty()) s.append(Arrays.toString(lista.get(0)));
        for (int i=1; i<lista.size();i++)
            s.append("\n").append(Arrays.toString(lista.get(i)));
        s.append("\n]");

        return "Sample " + s;
    }


}