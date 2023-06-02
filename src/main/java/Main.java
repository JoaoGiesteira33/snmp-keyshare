package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.AbstractMap;

class Main{
    public static void main(String[] args){
        //Agente a = new Agente("default.conf");
        List<Entry<Integer,Integer>> l = new ArrayList<Entry<Integer,Integer>>();
        l.add(new AbstractMap.SimpleEntry<Integer,Integer>(1, 2));
        l.add(new AbstractMap.SimpleEntry<Integer,Integer>(3, 4));

        PDU p = new PDU(123, 1, l);
        System.out.println(p);

        byte[] b = p.encode();
        for(int i = 0; i < b.length; i++){
            System.out.println(String.format("0x%02X ", b[i]));
        }
    }
}