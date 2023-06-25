package main.java;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Scanner;

/*
 * Autor: João Giesteira
 * 
 * Classe responsável por simular o comportamento de um gestor
 * 
 * Menu interativo CLI para o gestor criar e enviar diferentes PDU's
 * 
 * O gestor envia um PDU e espera pela resposta, se não receber resposta em V segundos pode reenviar o PDU
 */
public class Gestor {
    private static final int V = 1; //Time to wait for a response (seconds)
    private static int request_numer = 0;
    public static void main(String[] args){
        byte[] B_SK = "ditmb8ehavz52mg0".getBytes();
        SecretKey SK = new SecretKeySpec(B_SK, "AES");
        
        try{
            Scanner scanner = new Scanner(System.in);
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(Gestor.V * 1000);
            InetAddress address = InetAddress.getByName("localhost");
            
            byte[] buffer = new byte[2048];

            PDU request = create_request(scanner);

            while(request != null){
                System.out.println("Sending request (P)" + request.getP() + " of (Y)" + request.getY());
                if(request.getY() == 3){
                    buffer = "key".getBytes();   
                }else{
                    buffer = request.encode();
                }

                byte[] encrypted_buffer = PDU.encrypt(buffer, SK);
                DatagramPacket dpr = new DatagramPacket(encrypted_buffer, encrypted_buffer.length, address, 5050);
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

                byte[] plain_data = PDU.decrypt(dps.getData(), SK, dps.getLength());
                PDU response = PDU.decode(plain_data);
                System.out.println("Received response\n" + response.toString());

                request = create_request(scanner);
            }
            
            buffer = "end".getBytes();
            byte[] encrypted_buffer = PDU.encrypt(buffer, SK);
            DatagramPacket dpr = new DatagramPacket(encrypted_buffer, encrypted_buffer.length, address, 5050);
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
        System.out.println("3) Exit");
        System.out.print("4) Keys\n> ");

        input = scanner.nextLine();
        if(input.equals("1")){
            return create_get_request(scanner);
        }else if(input.equals("2")){
            return create_set_request(scanner);
        }else if(input.equals("3")){
            return null;
        }else if(input.equals("4")){
            return creat_keys_debug_request();
        }
        else{
            System.out.println("Invalid input!");
            return null;
        }
    }

    private static PDU create_get_request(Scanner scanner){
        List<Entry<String,Integer>> L = new ArrayList<Entry<String,Integer>>();

        String input;        
        System.out.print("Size of L\n> ");
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

                System.out.print("Enter N\n>");
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

        System.out.print("Size of W: \n> ");
        input = scanner.nextLine();
        int size = 1;

        try{
            size = Integer.parseInt(input);

            for(int i = 0; i < size; i++){
                String iid;
                String h;

                System.out.print("Enter I-ID\n> ");
                iid = scanner.nextLine();

                System.out.print("Enter H\n> ");
                h = scanner.nextLine();

                W.add(new AbstractMap.SimpleEntry<String,String>(iid, h));
            }
        }catch(NumberFormatException e){
            System.out.println("Invalid input!");
            return null;
        }

        return new PDU(Gestor.request_numer++,size,W,2);
    }

    //PDU apenas para efeitos de debug, para servidor fazer print das chaves
    private static PDU creat_keys_debug_request(){
        return new PDU(Gestor.request_numer++,3);
    }
}
