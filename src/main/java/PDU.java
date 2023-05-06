package main.java;

public class PDU {
    private int S; //modelo_segurança
    private int Ns; //numero_parametros_mecanismos_segurança
    private int[] Q; //parametros_mecanismos_segurança

    private int P; //identificação do pedido
    private int Y; //identificação do tipo de primitiva

    private int Nl; //Número de pares na lista da primitiva get
    private int Nw; //Número de pares na lista das primitivas set e response
    private int Nr; //Número de elementos na lista de erros

    private int[] L; //Lista primitivas get
    private int[] W; //Lista primitiva set e response
    private int[] R; //Lista de erros e valores associados

    //Construtor primitiva get/set
    public PDU(int P, int size, int[] lista, int tipo){
        this.S = 0;
        this.Ns = 0;
        this.Q = new int[0];

        this.P = P;
        this.Y = tipo;

        if(tipo == 1){
            this.get_constructor(P, size, lista);
        }else if(tipo == 2){
            this.set_constructor(P, size, lista);
        }

        this.Nr = 0;
        this.R = new int[0];
    }

    //Construtor primitiva response
    public PDU(int P, int Nw, int Nr, int[] W, int[] R){
        this.S = 0;
        this.Ns = 0;
        this.Q = new int[0];

        this.P = P;
        this.Y = 0;

        this.Nl = 0;
        this.Nw = Nw;
        this.Nr = Nr;

        this.L = new int[0];
        this.W = W;
        this.R = R;
    }

    //Função auxiliar para construção primitiva get
    private void get_constructor(int P, int Nl, int[] L){
        this.Nl = Nl;
        this.Nw = 0;

        this.L = L;
        this.W = new int[0];
    }

    //Função auxiliar para construção primitiva set
    private void set_constructor(int P, int Nw, int[] W){
        this.Nl = 0;
        this.Nw = Nw;

        this.L = new int[0];
        this.W = W;
    }

    public String encode(){
        return "ola";
    }
}
