package me.lifelessnerd.purekitpvp;


import me.lifelessnerd.purekitpvp.utils.MyStringUtils;

import java.util.*;

public class sample {

    public static void main(String[] args) {

        String[] argsArr = {"1", "2", "3"};
        argsArr = Arrays.stream(argsArr, 0, argsArr.length - 1).toArray(String[]::new);
        System.out.println(Arrays.stream(argsArr).toList());

    }


}
