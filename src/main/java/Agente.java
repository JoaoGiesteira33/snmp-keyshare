package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Agente {
    private static final String DEFAULT_CONFIG_FILE = "default.conf";
    public static final long SEED = 33;

    private int K; //Tamanho matriz
    private byte[] M; //Chave geração matriz
    private int T; //Intervalo entre atualização da matriz (ms)
    private int V; //Tempo validade Chaves, etc. (s)
    private int X; //Máximo entradas tabela de informações

    private Matrix matrix;
    private MIB mib;

    public Agente(String config_file){
        read_file(config_file);
        gerar_matriz();
        gerar_mib();
    }

    public void init(){
        Runnable server = new Server(this.matrix, this.mib);
        Thread server_thread = new Thread(server);

        Runnable matrix_handler = new MatrixHandler(this.matrix, this.mib);
        Thread matrix_handler_thread = new Thread(matrix_handler);

        matrix_handler_thread.start();
        server_thread.start();
    }

    private void read_file(String file_name){
        String config_files_folder = "config_files";
        String file_path = config_files_folder + "/" + file_name;

        try{
            File configFile = new File(file_path);
            Scanner myReader = new Scanner(configFile);

            String K = myReader.nextLine();
            String M = myReader.nextLine();
            String T = myReader.nextLine();
            String V = myReader.nextLine();
            String X = myReader.nextLine();   

            if(myReader.hasNextLine()){
                System.out.println("Ficheiro de configuração inválido!");
                System.out.println("Utilizando valores por default.");
                read_file(DEFAULT_CONFIG_FILE);
            }
            myReader.close();

            //Parse de valores lidos
            this.K = Integer.parseInt(K);
            this.M = M.getBytes();
            this.T = Integer.parseInt(T);
            this.V = Integer.parseInt(V);
            this.X = Integer.parseInt(X);

        }catch(FileNotFoundException e){
            System.out.println("Ficheiro de configuração não encontrado!");
            System.out.println("Utilizando valores por default.");
            read_file(DEFAULT_CONFIG_FILE);
        }
    }

    private void gerar_matriz(){
        Matrix a = new Matrix(this.K, this.M, 0);
        Matrix b = new Matrix(this.K, this.M, 1);
        Matrix s = new Matrix(this.K, SEED);

        this.matrix = new Matrix(this.K, a.getMatrix(), b.getMatrix(), s.getMatrix());
    }

    private void gerar_mib(){
        this.mib = new MIB(this.K, this.T, this.X, this.V, this.M, 0, 256);
    }

    public void print_matrix(){
        System.out.println(this.matrix.toString());
    }
}
