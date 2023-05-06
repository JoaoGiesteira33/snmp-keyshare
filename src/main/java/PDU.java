package main.java;

public class PDU {
    int S; //modelo_segurança
    int Ns; //numero_parametros_mecanismos_segurança
    int[] Q; //parametros_mecanismos_segurança

    int P; //identificação do pedido
    int Y; //identificação do tipo de primitiva

    int Nl; //Número de pares na lista da primitiva get
    int Nw; //Número de pares na lista das primitivas set e response
    int Nr; //Número de elementos na lista de erros

    int[] L; //Lista primitivas get
    int[] W; //Lista primitiva set e response
    int[] R; //Lista de erros e valores associados

    public PDU(){
        this.S = 0;
        this.Ns = 0;
        this.Q = new int[0];
    }

    public String encode(){
        return "ola";
    }
}
