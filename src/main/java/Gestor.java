package main.java;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Gestor {
    public static void main(String[] args){
        try{
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            
            byte[] buffer = new byte[1024];
            buffer = "end".getBytes();
            
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, 5050);
            socket.send(request);
        
            socket.close();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
