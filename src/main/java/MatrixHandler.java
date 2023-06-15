package main.java;

public class MatrixHandler implements Runnable{
    private Matrix matrix;
    private long last_update_time = System.currentTimeMillis();
    private int max_interval_time;

    public MatrixHandler(Matrix m, int max_interval_time){
        this.matrix = m;
        this.max_interval_time = max_interval_time;
    }

    public void run(){   
        while(true){

            long current_time_ms = System.currentTimeMillis();
            long time_since_last_update = current_time_ms - last_update_time;
            
            if(time_since_last_update >= this.max_interval_time){
                System.out.println("Updating matrix because " + time_since_last_update + "ms have passed since last update.");
                this.matrix.update_matrix();
                this.last_update_time = current_time_ms;
            }    
        }
    }
}
