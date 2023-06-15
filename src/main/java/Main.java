package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.AbstractMap;

class Main{
    public static void main(String[] args){
        //Agente a = new Agente("default.conf");

        List<Entry<String,String>> r = new ArrayList<Entry<String,String>>();
        r.add(new AbstractMap.SimpleEntry<String,String>("data.1.0", "erro1"));
        r.add(new AbstractMap.SimpleEntry<String,String>("data.2.0", "erro2"));

        List<Entry<String,String>> w = new ArrayList<Entry<String,String>>();
        w.add(new AbstractMap.SimpleEntry<String,String>("keyEntry.1", "ola"));
        w.add(new AbstractMap.SimpleEntry<String,String>("keyEntry.3", "adeus")); 
        
        //List<Entry<String,Integer>> l = new ArrayList<Entry<String,Integer>>();
        //l.add(new AbstractMap.SimpleEntry<String,Integer>(1, 2));
        //l.add(new AbstractMap.SimpleEntry<String,Integer>(3, 4));

        //PDU set = new PDU(123, 2, w, 2);
        //System.out.println(p);

        PDU response = new PDU(123, 2, 2, w, r);
        System.out.println(response);

        byte[] b = response.encode();
        //byte[] b = set.encode();
        /*
        for(int i = 0; i < b.length; i++){
            System.out.println(String.format("0x%02X ", b[i]));
        }
        */

        System.out.println("Decoding...");
        PDU decoded = PDU.decode(b);
        System.out.println(decoded);
    }
}