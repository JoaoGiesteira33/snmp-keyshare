package main.java;

import java.util.Random;

public class Matrix {
    private byte[][] matrix;
    private byte[] key;
    private int update_count = 0;
    
    //Type 0 or Type 1 matrix
    public Matrix(int size, byte[] key, int type){
        this.matrix = new byte[size][size];
        this.key = key;

        byte[] m1 = new byte[size];
        byte[] m2 = new byte[size];

        //m1 is first half of key
        for(int i = 0; i < size; i++){
            m1[i] = this.key[i];
        }
        //m2 is rest of the key
        for(int i = 0; i < size; i++){
            m2[i] = this.key[i + size];
        }

        if(type == 0){
            type_0_matrix_creation(m1, size);
        }else if(type == 1){
            type_1_matrix_creation(m2, size);
        }
    
    }

    //Random matrix with a given seed
    public Matrix(int size, long seed){
        this.matrix = new byte[size][size];

        Random rand = new Random(seed);
        final int min = 0;
        final int max = 255;

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                this.matrix[i][j] = (byte)(rand.nextInt(max + 1 - min) + min);
            }
        }

    }

    //XOR of 3 matrices
    public Matrix(int size, byte[][] matrix_a, byte[][] matrix_b, byte[][] matrix_s){
        this.matrix = new byte[size][size];

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                this.matrix[i][j] = (byte)(matrix_a[i][j] ^ matrix_b[i][j] ^ matrix_s[i][j]);
            }
        }
    }

    //Rows based on first half of key
    private void type_0_matrix_creation(byte[] m1, int size){
        //First row is first half of the key
        for(int i = 0 ; i < size; i++){
            this.matrix[0][i] = m1[i];
        }

        //Following rows are rotations of m1
        for(int i = 1 ; i < size; i++){
            this.matrix[i] = rotate_right(m1, i);
        }
    }

    //Columns based on second half of key
    private void type_1_matrix_creation(byte[] m2, int size){
        //First column is second half of the key
        for(int i = 0 ; i < size; i++){
            this.matrix[i][0] = m2[i];
        }

        //Following columns are rotations of m2
        for(int i = 1 ; i < size; i++){
            byte[] new_column = rotate_right(m2, i);

            for(int j = 0; j < size; j++){
                this.matrix[j][i] = new_column[j];
            }
        }
    }

    public byte[][] getMatrix(){
        byte[][] new_matrix = new byte[this.matrix.length][this.matrix.length];

        for(int i = 0; i < this.matrix.length; i++){
            for(int j = 0; j < this.matrix.length; j++){
                new_matrix[i][j] = this.matrix[i][j];
            }
        }

        return new_matrix;
    }

    public byte[] getColumn(int index){
        byte[] column = new byte[this.matrix.length];

        for(int i = 0; i < this.matrix.length; i++){
            column[i] = this.matrix[i][index];
        }

        return column;
    }

    public void update_matrix(){
        int size = this.matrix.length;

        //Rotate rows randomly
        for(int i = 0; i < size; i++){
            byte first_row_value = this.matrix[i][0];
            Random rand = new Random((long)first_row_value);
            int rotate_value = rand.nextInt(size);

            this.matrix[i] = rotate_right(this.matrix[i], rotate_value);
        }

        //Rotate columns randomly
        for(int j = 0 ; j < size; j++){
            byte first_column_value = this.matrix[0][j];
            Random rand = new Random((long)first_column_value);
            int rotate_value = rand.nextInt(size);

            byte[] new_column = rotate_right(this.getColumn(j), rotate_value);

            for(int i = 0; i < size; i++){
                this.matrix[i][j] = new_column[i];
            }
        }

        this.update_count++;
    }

    //Geração de uma chave
    public byte[] generate_key(int size){
        byte[] key = new byte[size];

        long seed = this.update_count + this.matrix[0][0];
        Random rand = new Random(seed);
        int row = rand.nextInt(size);

        seed = this.matrix[row][0];
        rand = new Random(seed);
        int column = rand.nextInt(size);

        byte[] row_bytes = this.matrix[row];
        byte[] column_bytes = this.getColumn(column);

        for(int i = 0; i < size; i++){
            key[i] = (byte)(row_bytes[i] ^ column_bytes[i]);
        }

        return key;
    }

    private byte[] rotate_right(byte[] line, int value){
        byte[] new_line = new byte[line.length];

        for(int i = 0; i < line.length; i++){
            new_line[(i + value) % line.length] = line[i];
        }

        return new_line;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                sb.append(String.format("0x%02X ", matrix[i][j]));
                sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}