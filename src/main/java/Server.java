package main.java;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server implements Runnable{
    private static final int PORT = 5050;
    private DatagramSocket socket;
    private byte[] buffer = new byte[1024];

    private Matrix matrix;
    private MIB mib;

    long last_update_time = System.currentTimeMillis();

    public Server(Matrix matrix, MIB mib){
        this.matrix = matrix;
        this.mib = mib;

        try{
            socket = new DatagramSocket(PORT);
            System.out.println("Server started on port " + PORT);
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            socket.close();
        }
    }

    public void run(){
        while(true){
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            try{
                socket.receive(request);
            }catch(IOException e){
                System.out.println("Error: " + e.getMessage());
                socket.close();
                break;
            }

            String message = new String(request.getData(), 0, request.getLength());
            System.out.println("Received message: " + message);
            
            if(message.equals("end")){
                break;
            }

            PDU received_pdu = PDU.decode(request.getData());
            System.out.println("Received PDU: " + received_pdu.toString());
            handle_pdu(received_pdu);
        }
        socket.close();
    }

    private void handle_pdu(PDU pdu){
        int pdu_type = pdu.getY();

        switch(pdu_type){
            case 1:
                handle_get_request(pdu);
                break;
            case 2:
                handle_set_request(pdu);
                break;
            default:
                System.out.println("Error: Unacceptable PDU type");
                break;
        }
    }

    private void handle_get_request(PDU pdu){
        int P = pdu.getP();

        
    }

    private void handle_set_request(PDU pdu){
        int P = pdu.getP();
    }
}
