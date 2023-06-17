package main.java;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server implements Runnable{
    private static final int PORT = 5050;
    private DatagramSocket socket;
    private byte[] buffer = new byte[2048];

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
                break;
            }

            String message = new String(request.getData(), 0, request.getLength());
            
            if(message.equals("end")){
                break;
            }

            PDU received_pdu = PDU.decode(request.getData());
            System.out.println("Received PDU (P)" + received_pdu.getP() + " of (Y)" + received_pdu.getY());
            
            PDU response = handle_pdu(received_pdu);
            
            if(response == null){
                continue;
            }

            buffer = response.encode();
            
            System.out.println("Sending response PDU\n" + response.toString());
            DatagramPacket response_packet = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
            
            try{
                socket.send(response_packet);
            }catch(IOException e){
                System.out.println("Error: " + e.getMessage());
                break;
            }

        }
        System.out.println("Stopping server!");
        socket.close();
    }

    private PDU handle_pdu(PDU pdu){
        int pdu_type = pdu.getY();

        switch(pdu_type){
            case 1:
                return handle_get_request(pdu);
            case 2:
                return handle_set_request(pdu);
            default:
                System.out.println("Error: Unacceptable PDU type");
                return null;
        }
    }

    private PDU handle_get_request(PDU pdu){
        int P = pdu.getP();
        List<Entry<String, Integer>> L = pdu.getL();

        List<Entry<String,String>> W = new ArrayList<>();
        List<Entry<String,String>> R = new ArrayList<>();

        int Nw = 0;
        int Nr = 0;

        for(Entry<String,Integer> entry : L){
            String iid = entry.getKey();
            int value = entry.getValue();

            if(value < 0){
                Nr++;
                R.add(new AbstractMap.SimpleEntry<String,String>(iid, "Negative value not allowed!"));
                continue;
            }

            String[] iid_split = iid.split("\\.");
            int depth = iid_split.length;
            int line = Integer.parseInt(iid_split[depth - 1]);

            for(int i = 0 ; i <= value ; i++){ 
                int current_line = line + i;
                iid_split[depth - 1] = Integer.toString(current_line);
                String current_iid = String.join(".", iid_split);
                System.out.println("Getting value for iid: " + current_iid);

                String value_from_iid = this.mib.get_iid_value(current_iid);
                if(value_from_iid == null){
                    Nr++;
                    R.add(new AbstractMap.SimpleEntry<String,String>(current_iid, "No such iid"));
                    continue;
                }

                Nw++;
                W.add(new AbstractMap.SimpleEntry<String,String>(current_iid, value_from_iid));
            }
        }

        if(W.size() == 0){
            Nw = 1;
            W.add(new AbstractMap.SimpleEntry<String,String>("0", "0"));
        }

        if(R.size() == 0){
            Nr = 1;
            R.add(new AbstractMap.SimpleEntry<String,String>("0", "0"));
        }

        return new PDU(P, Nw, Nr, W, R);
    }

    private PDU handle_set_request(PDU pdu){
        int P = pdu.getP();
        return new PDU(1,1,new ArrayList<>());
    }
}
