package main.java;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

public class Gestor {
    private static final int V = 1; //Time to wait for a response (seconds)
    private static int request_numer = 0;
    public static void main(String[] args){
        try{
            Scanner scanner = new Scanner(System.in);
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(Gestor.V * 1000);
            InetAddress address = InetAddress.getByName("localhost");
            
            byte[] buffer = new byte[2048];

            PDU request = create_request(scanner);

            while(request != null){
                System.out.println("Sending request (P)" + request.getP() + " of (Y)" + request.getY());
                buffer = request.encode();

                DatagramPacket dpr = new DatagramPacket(buffer, buffer.length, address, 5050);
                socket.send(dpr);
                
                buffer = new byte[2048];
                DatagramPacket dps = new DatagramPacket(buffer, buffer.length);

                try{
                    socket.receive(dps);
                }catch(SocketTimeoutException ste){
                    System.out.println("No response received!");
                    request = create_request(scanner);
                    continue;
                }

                System.out.println("Received following data: " + new String(dps.getData(), 0, dps.getLength()) + "\n");
                PDU response = PDU.decode(dps.getData());
                System.out.println("Received response\n" + response.toString());

                request = create_request(scanner);
            }
            
            buffer = "end".getBytes();
            DatagramPacket dpr = new DatagramPacket(buffer, buffer.length, address, 5050);
            socket.send(dpr);

            socket.close();
            scanner.close();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static PDU create_request(Scanner scanner){
        String input;

        System.out.println("1) Create Get Request");
        System.out.println("2) Create Set Request");
        System.out.print("3) Exit\n> ");

        input = scanner.nextLine();
        if(input.equals("1")){
            return create_get_request(scanner);
        }else if(input.equals("2")){
            return create_set_request(scanner);
        }else if(input.equals("3")){
            return null;
        }else{
            System.out.println("Invalid input!");
            return null;
        }
    }

    private static PDU create_get_request(Scanner scanner){
        List<Entry<String,Integer>> L = new ArrayList<Entry<String,Integer>>();

        String input;        
        System.out.println("Size of L\n> ");
        input = scanner.nextLine();
        int size = 1;

        try{
            size = Integer.parseInt(input);

            for(int i = 0; i < size; i++){
                String iid;
                String n;
                int n_value;

                System.out.print("Enter I-ID\n> ");
                iid = scanner.nextLine();

                System.out.println("Enter N\n>");
                n = scanner.nextLine();
                n_value = Integer.parseInt(n);

                L.add(new AbstractMap.SimpleEntry<String,Integer>(iid, n_value));
            }
        }catch(NumberFormatException e){
            System.out.println("Invalid input!");
            return null;
        }  

        return new PDU(Gestor.request_numer++,size,L);
    }

    private static PDU create_set_request(Scanner scanner){
        List<Entry<String,String>> W = new ArrayList<Entry<String,String>>();
        String input;

        System.out.println("Size of W: \n> ");
        input = scanner.nextLine();
        int size = 1;

        try{
            size = Integer.parseInt(input);

            for(int i = 0; i < size; i++){
                String iid;
                String h;

                System.out.print("Enter I-ID\n> ");
                iid = scanner.nextLine();

                System.out.println("Enter H\n>");
                h = scanner.nextLine();

                W.add(new AbstractMap.SimpleEntry<String,String>(iid, h));
            }
        }catch(NumberFormatException e){
            System.out.println("Invalid input!");
            return null;
        }

        return new PDU(Gestor.request_numer++,size,W,2);
    }
}
