package main.java;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.crypto.SecretKey;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/*
 * Autor: João Giesteira
 * 
 * Thread que vai receber todos os PDU's, processá-los, criar resposta e enviar a mesma
 */
public class Server implements Runnable{
    private static final int PORT = 5050;
    public DatagramSocket socket;
    private SecretKey SK;
    private byte[] buffer = new byte[2048];

    private Matrix matrix;
    private MIB mib;

    long last_update_time = System.currentTimeMillis();

    public Server(Matrix matrix, MIB mib, SecretKey SK){
        this.matrix = matrix;
        this.mib = mib;
        this.SK = SK;

        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            this.mib.update_number_valid_keys();
            buffer = new byte[2048];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);

            try{
                socket.receive(request);
            }catch(IOException e){
                System.out.println("Error: " + e.getMessage());
                break;
            }

            byte[] data = PDU.decrypt(request.getData(), this.SK, request.getLength());
            String message = new String(data);
            
            if(message.equals("end")){
                break;
            }
            if(message.equals("key")){
                print_key_info();
                continue;
            }

            PDU received_pdu = PDU.decode(data);
            System.out.println("Received PDU (P)" + received_pdu.getP() + " of (Y)" + received_pdu.getY());
            
            String pdu_sender = request.getAddress().toString() + ":" + request.getPort();
            PDU response = handle_pdu(received_pdu, pdu_sender);
            
            if(response == null){
                continue;
            }

            byte[] encrypted_data = PDU.encrypt(response.encode(), this.SK);
            buffer = encrypted_data;
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

    private PDU handle_pdu(PDU pdu, String pdu_sender){
        int pdu_type = pdu.getY();

        switch(pdu_type){
            case 1:
                return handle_get_request(pdu, pdu_sender);
            case 2:
                return handle_set_request(pdu, pdu_sender);
            default:
                System.out.println("Error: Unacceptable PDU type");
                return null;
        }
    }

    private PDU handle_get_request(PDU pdu, String pdu_sender){
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

                String value_from_iid = this.mib.get_iid_value(current_iid, pdu_sender);
                if(value_from_iid == null){
                    Nr++;
                    R.add(new AbstractMap.SimpleEntry<String,String>(current_iid, "No access to IID value!"));
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

    private PDU handle_set_request(PDU pdu, String pdu_sender){
        int P = pdu.getP();
        List<Entry<String, String>> receiving_W = pdu.getW();

        List<Entry<String,String>> W = new ArrayList<>();
        List<Entry<String,String>> R = new ArrayList<>();

        int Nw = 0;
        int Nr = 0;

        for(Entry<String,String> entry : receiving_W){
            String iid = entry.getKey();
            String value = entry.getValue();

            if(iid.equals("keyVisibility.0")){
                if(value.equals("0") || value.equals("1") || value.equals("2")){
                    byte[] key = this.matrix.generate_key(this.mib.get_s_key_size());
                    this.matrix.update_matrix();
                    
                    int i_value = Integer.parseInt(value);

                    int key_row = this.mib.save_key(key, pdu_sender, i_value);
                    if(key_row == 0){
                        Nr++;
                        R.add(new AbstractMap.SimpleEntry<String,String>(iid, "No space for new key!"));
                    }else{   
                        String new_iid = "keyVisibility." + key_row;
                        Nw++;
                        W.add(new AbstractMap.SimpleEntry<String,String>(new_iid, value));
                    }
                }else{
                    Nr++;
                    R.add(new AbstractMap.SimpleEntry<String,String>(iid, "Invalid Visibility Value (0,1,2)"));
                }
                continue;
            }

            String set_result = this.mib.set_iid_value(iid, value, pdu_sender);
            
            //Se valor devolvido é igual ao valor enviado, então o set foi bem sucedido
            //Em caso contrário, o valor devolvido é uma mensagem de erro
            if(set_result.equals(value)){
                Nw++;
                W.add(new AbstractMap.SimpleEntry<String,String>(iid, set_result));
            }else{
                Nr++;
                R.add(new AbstractMap.SimpleEntry<String,String>(iid, set_result));
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

    private void print_key_info(){
        for(KeyEntry ke : this.mib.get_keys()){
            System.out.println(ke);
        }
    }
}
