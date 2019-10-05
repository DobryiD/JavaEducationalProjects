package KMean;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Knap {

    private Item[] itemList;
    private int len;
    private int capacity;
    private int bestSoFarValue;
    private int bestSoFarWeight;
    private boolean[] solution;
    private boolean[] current;



    private Knap(int capacity, Item[] items) {
        this.len = items.length;
        itemList = items;
        this.capacity = capacity;

        bestSoFarValue = Integer.MIN_VALUE;
        bestSoFarWeight=0;
        solution = new boolean[len];
        current = new boolean[len];

        solve(len - 1);

        printSolution();
    }

    private void solve(int k) {
        if (k < 0) {
            int wt = 0;
            int val = 0;
            for (int i = 0; i < len; i++) {
                if (current[i]) {
                    wt += itemList[i].weight();
                    val += itemList[i].value();
                }
            }

            if (wt <= capacity && val > bestSoFarValue) {
                bestSoFarValue = val;
                bestSoFarWeight=wt;
                copySolution();
            }
            return;
        }
        current[k] = true;
        solve(k - 1);

        current[k] = false;
        solve(k - 1);
    }


    private void printSolution() {
        System.out.println("Best value: " + bestSoFarValue+" Best weight: "+bestSoFarWeight);
        System.out.println(" Item Weight Value");
        for (int i = 0; i < len; i++) {
            if (solution[i]) {
                System.out.printf("%4d %5d %5d\n", i, itemList[i].weight(),
                        itemList[i].value());
            }
        }
        System.out.println();

        for (boolean b : solution) {
            System.out.print((b?1:0)+ " ");

        }
        System.out.println();
    }



    private void copySolution() {
        if (len >= 0)
            System.arraycopy(current, 0, solution, 0, len);
    }


    public static void main(String[] args) throws IOException {
        long startTime   = System.nanoTime();
        readFromFile(new File("knapsack"));
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        long milisec=totalTime/1000000;
        long sec=milisec/1000;
        System.out.println( sec+" seconds");
    }

    private static void readFromFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        int cap = Integer.parseInt(reader.readLine());
        List<Item> list = new ArrayList<>();
        while (reader.ready()) {
            String[] temp = reader.readLine().split(" ");
            list.add(new Item(Integer.parseInt(temp[1]), Integer.parseInt(temp[0])));
        }
        Item[] units = list.toArray(new Item[0]);
        new Knap(cap, units);
    }
}

