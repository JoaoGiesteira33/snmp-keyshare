package main.java;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Gestor {
    public static void main(String[] args){
        try{
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            
            byte[] buffer = new byte[1024];

            List<Entry<Integer,String>> r = new ArrayList<Entry<Integer,String>>();
            r.add(new AbstractMap.SimpleEntry<Integer,String>(1, "erro1"));
            r.add(new AbstractMap.SimpleEntry<Integer,String>(2, "erro2"));

            List<Entry<Integer,String>> w = new ArrayList<Entry<Integer,String>>();
            w.add(new AbstractMap.SimpleEntry<Integer,String>(1, "ola"));
            w.add(new AbstractMap.SimpleEntry<Integer,String>(2, "adeus"));

            PDU response = new PDU(123, 2, 2, w, r);
            
            buffer = response.encode();

            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, 5050);
            socket.send(request);

            Thread.sleep(10000, 0);

            buffer = "end".getBytes();
            
            request = new DatagramPacket(buffer, buffer.length, address, 5050);
            socket.send(request);
        
            socket.close();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
