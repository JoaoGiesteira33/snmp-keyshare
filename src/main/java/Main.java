package main.java;

/*
 * Autor: João Giesteira
 * 
 * Inicialização de agente
 */
class Main{
    public static void main(String[] args){
        Agente a = new Agente("default.conf");
        a.init();
    }
}