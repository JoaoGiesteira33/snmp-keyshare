package main.java;

public class MatrixHandler implements Runnable{
    private Matrix matrix;
    private long last_update_time = System.currentTimeMillis();
    private MIB mib;

    public MatrixHandler(Matrix m, MIB mib){
        this.matrix = m;
        this.mib = mib;
    }

    public void run(){   
        int mib_key_size = this.mib.get_s_key_size();

        while(true){
            if(this.mib.get_s_key_size() != mib_key_size){
                System.out.println("Matrix size has changed from " + mib_key_size + " to " + this.matrix.size());
                mib_key_size = this.matrix.size();

                int K = this.mib.get_s_key_size();
                byte[] M = this.mib.get_c_master_key();

                Matrix a = new Matrix(K, M, 0);
                Matrix b = new Matrix(K, M, 1);
                Matrix s = new Matrix(K, Agente.SEED);

                this.matrix = new Matrix(K, a.getMatrix(), b.getMatrix(), s.getMatrix());
            }

            long current_time_ms = System.currentTimeMillis();
            long time_since_last_update = current_time_ms - last_update_time;
            
            if(time_since_last_update >= this.mib.get_s_interval_update()){
                System.out.println("Updating matrix because " + time_since_last_update + "ms have passed since last update.");
                System.out.println("==========================================");
                System.out.println(matrix.toString());
                this.matrix.update_matrix();
                this.last_update_time = current_time_ms;
            }    
        }
    }
}
