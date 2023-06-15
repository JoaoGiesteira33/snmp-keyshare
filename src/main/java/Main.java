package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.AbstractMap;

class Main{
    public static void main(String[] args){
        Agente a = new Agente("default.conf");

        //List<Entry<Integer,String>> r = new ArrayList<Entry<Integer,String>>();
        //r.add(new AbstractMap.SimpleEntry<Integer,String>(1, "erro1"));
        //r.add(new AbstractMap.SimpleEntry<Integer,String>(2, "erro2"));

        //List<Entry<Integer,String>> w = new ArrayList<Entry<Integer,String>>();
        //w.add(new AbstractMap.SimpleEntry<Integer,String>(1, "ola"));
        //w.add(new AbstractMap.SimpleEntry<Integer,String>(2, "adeus"));
        
        //List<Entry<Integer,Integer>> l = new ArrayList<Entry<Integer,Integer>>();
        //l.add(new AbstractMap.SimpleEntry<Integer,Integer>(1, 2));
        //l.add(new AbstractMap.SimpleEntry<Integer,Integer>(3, 4));

        //PDU set = new PDU(123, 2, w, 2);
        //System.out.println(p);

        //PDU response = new PDU(123, 2, 2, w, r);
        //System.out.println(response);

        //byte[] b = response.encode();
        //byte[] b = set.encode();
        /*
        for(int i = 0; i < b.length; i++){
            System.out.println(String.format("0x%02X ", b[i]));
        }
        */

        //System.out.println("Decoding...");
        //PDU decoded = PDU.decode(b);
        //System.out.println(decoded);
    }
}