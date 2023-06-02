package main.java;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
    private static final int PORT = 5050;
    private DatagramSocket socket;
    private byte[] buffer = new byte[1024];

    public Server(){
        try{
            socket = new DatagramSocket(PORT);
            System.out.println("Server started on port " + PORT);
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void serve() throws IOException{
        while(true){
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            socket.receive(request);

            String message = new String(request.getData(), 0, request.getLength());
            System.out.println("Received message: " + message);
            
            if(message.equals("end")){
                break;
            }
        }
        socket.close();
    }
}
