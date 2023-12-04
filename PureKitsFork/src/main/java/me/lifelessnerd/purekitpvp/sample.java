package me.lifelessnerd.purekitpvp;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class sample {

    public static void main(String[] args) {
        Map<Integer, Integer> treeMap = new TreeMap<>();

        // Adding elements to the tree map
        treeMap.put(1, 0);
        treeMap.put(2, 0);
        treeMap.put(3, 0);

        ArrayList<String> array = new ArrayList<>();

        // Iterating over the elements of the tree map
        int enumerator = 0;
        for (Integer key : treeMap.keySet()) {
            if (enumerator >= 6){
                break;
            }
            array.add("Key: " + key + ", Value: " + treeMap.get(key));
            enumerator++;
        }

        var dab = "dab";
        System.out.println(array);


    }
}
