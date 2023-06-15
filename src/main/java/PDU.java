package main.java;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class PDU {
    private int S; //modelo_segurança
    private int Ns; //numero_parametros_mecanismos_segurança
    private int[] Q; //parametros_mecanismos_segurança

    private int P; //identificação do pedido
    private int Y; //identificação do tipo de primitiva

    private int Nl; //Número de pares na lista da primitiva get
    private int Nw; //Número de pares na lista das primitivas set e response
    private int Nr; //Número de elementos na lista de erros

    private List<Entry<Integer,Integer>> L; //Lista primitivas get
    private List<Entry<Integer,String>> W; //Lista primitiva set e response
    private List<Entry<Integer,String>> R; //Lista de erros e valores associados

    //Construtor primitiva get
    public PDU(int P, int size, List<Entry<Integer,Integer>> lista){
        this.S = 0;
        this.Ns = 0;
        this.Q = new int[0];

        this.P = P;
        this.Y = 1;

        this.get_constructor(P, size, lista);
        
        this.Nr = 0;
        this.R = new ArrayList<Entry<Integer,String>>();
    }

    //Construtor primitiva set
    public PDU(int P, int size, List<Entry<Integer,String>> lista, int tipo){
        this.S = 0;
        this.Ns = 0;
        this.Q = new int[0];

        this.P = P;
        this.Y = tipo;

        this.Nl = 0;
        this.L = new ArrayList<Entry<Integer,Integer>>();

        this.set_constructor(P, size, lista);
        
        this.Nr = 0;
        this.R = new ArrayList<Entry<Integer,String>>();
    }

    //Construtor primitiva response
    public PDU(int P, int Nw, int Nr, List<Entry<Integer,String>> W, List<Entry<Integer,String>> R){
        this.S = 0;
        this.Ns = 0;
        this.Q = new int[0];

        this.P = P;
        this.Y = 0;

        this.Nl = 0;
        this.Nw = Nw;
        this.Nr = Nr;

        this.L = new ArrayList<Entry<Integer,Integer>>();
        this.W = W;
        this.R = R;
    }

    //Função auxiliar para construção primitiva get
    private void get_constructor(int P, int Nl, List<Entry<Integer,Integer>> L){
        this.Nl = Nl;
        this.Nw = 0;

        this.L = L;
        this.W = new ArrayList<Entry<Integer,String>>();
    }

    //Função auxiliar para construção primitiva set
    private void set_constructor(int P, int Nw, List<Entry<Integer,String>> W){
        this.Nl = 0;
        this.Nw = Nw;

        this.L = new ArrayList<Entry<Integer,Integer>>();
        this.W = W;
    }

    public int getY(){
        return this.Y;
    }

    public byte[] encode(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //Convert all fiels strings
        //Ignore S, Ns and Q
        String P = Integer.toString(this.P) + '\0';
        String Y = Integer.toString(this.Y) + '\0';

        String Nl = Integer.toString(this.Nl) + '\0';
        String Nw = Integer.toString(this.Nw) + '\0';
        String Nr = Integer.toString(this.Nr) + '\0';

        //Encode all Strings as ASCII terminated by 0x00
        byte[] P_bytes = P.getBytes(StandardCharsets.US_ASCII);
        byte[] Y_bytes = Y.getBytes(StandardCharsets.US_ASCII);

        byte[] Nl_bytes = Nl.getBytes(StandardCharsets.US_ASCII);
        byte[] Nw_bytes = Nw.getBytes(StandardCharsets.US_ASCII);
        byte[] Nr_bytes = Nr.getBytes(StandardCharsets.US_ASCII);

        StringBuilder sb = new StringBuilder();
        for(Entry<Integer,Integer> entry : this.L) {
            sb.append(entry.getKey());
            sb.append('\0');
            sb.append(entry.getValue());
            sb.append('\0');
        }
        for(Entry<Integer,String> entry : this.W){
            sb.append(entry.getKey());
            sb.append('\0');
            sb.append(entry.getValue());
            sb.append('\0');
        }
        for(Entry<Integer,String> entry : this.R){
            sb.append(entry.getKey());
            sb.append('\0');
            sb.append(entry.getValue());
            sb.append('\0');
        }
        String L_W_R = sb.toString();
        byte[] L_W_R_bytes = L_W_R.getBytes(StandardCharsets.US_ASCII);

        try{
            stream.write(P_bytes);
            stream.write(Y_bytes);

            stream.write(Nl_bytes);
            stream.write(Nw_bytes);
            stream.write(Nr_bytes);

            stream.write(L_W_R_bytes);
            
            byte[] encoded_PDU = stream.toByteArray();
            stream.close();
            return encoded_PDU;
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }        
    }

    private static String read_full_string(ByteArrayInputStream encoded_PDU){
        StringBuilder sb = new StringBuilder();

        int current_byte = encoded_PDU.read();
        while(current_byte != 0){
            sb.append((char)current_byte);
            current_byte = encoded_PDU.read();
        }

        return sb.toString();
    }

    public static PDU decode(byte[] encoded_PDU){
        List<Entry<Integer,Integer>> L = new ArrayList<Entry<Integer,Integer>>();
        List<Entry<Integer,String>> W = new ArrayList<Entry<Integer,String>>();
        List<Entry<Integer,String>> R = new ArrayList<Entry<Integer,String>>();

        ByteArrayInputStream stream = new ByteArrayInputStream(encoded_PDU);
        
        //Ignore S, Ns and Q
        String P = read_full_string(stream);
        String Y = read_full_string(stream);
        String Nl = read_full_string(stream);
        String Nw = read_full_string(stream);
        String Nr = read_full_string(stream);
        
        Integer P_value = Integer.parseInt(P);
        Integer Y_value = Integer.parseInt(Y);
        Integer L_size = Integer.parseInt(Nl);
        Integer W_size = Integer.parseInt(Nw);	
        Integer R_size = Integer.parseInt(Nr);

        for(int i = 0; i < L_size; i++){
            String key = read_full_string(stream);
            String value = read_full_string(stream);
            L.add(new AbstractMap.SimpleEntry<Integer,Integer>(Integer.parseInt(key),Integer.parseInt(value)));
        }

        for(int i = 0; i < W_size; i++){
            String key = read_full_string(stream);
            String value = read_full_string(stream);
            W.add(new AbstractMap.SimpleEntry<Integer,String>(Integer.parseInt(key),value));
        }

        for(int i = 0; i < R_size; i++){
            String key = read_full_string(stream);
            String value = read_full_string(stream);
            R.add(new AbstractMap.SimpleEntry<Integer,String>(Integer.parseInt(key),value));
        }

        
        if(Y_value == 0){//Response
            PDU response = new PDU(P_value, W_size, R_size, W, R);
            return response;
        }else if(Y_value == 1){//Get
            PDU get = new PDU(P_value, L_size, L);
            return get;
        }else if(Y_value == 2){//Set
            PDU set = new PDU(P_value, W_size, W, 2);
            return set;
        }else{
            System.out.println("Error: Could not decode PDU");
            return null;
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("==================\n");
        sb.append("S: " + this.S + '\n');
        sb.append("Ns: " + this.Ns + '\n');

        sb.append("Q: ");
        for(int i = 0; i < this.Q.length; i++){
            sb.append(this.Q[i] + " ");
        }
        sb.append('\n');

        sb.append("P: " + this.P + '\n');
        sb.append("Y: " + this.Y + '\n');

        sb.append("Nl: " + this.Nl + '\n');
        sb.append("Nw: " + this.Nw + '\n');
        sb.append("Nr: " + this.Nr + '\n');

        sb.append("L: ");
        for(Entry<Integer,Integer> entry : this.L){
            sb.append(entry.getKey() + "->" + entry.getValue() + " | ");
        }
        sb.append('\n');

        sb.append("W: ");
        for(Entry<Integer,String> entry : this.W){
            sb.append(entry.getKey() + "->" + entry.getValue() + " | ");
        }

        sb.append("R: ");
        for(Entry<Integer,String> entry : this.R){
            sb.append(entry.getKey() + "->" + entry.getValue() + " | ");
        }

        return sb.toString();
    }
}
