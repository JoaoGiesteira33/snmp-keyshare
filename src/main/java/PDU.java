package main.java;

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

    public String encode(){
        return "ola";
    }
}
