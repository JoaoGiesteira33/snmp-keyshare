package main.java;

public class Matrix {
    private byte[][] matrix;
    private byte[] key;
    
    public Matrix(int size, byte[] key){
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

        //First row is first half of the key
        for(int i = 0 ; i < size; i++){
            matrix[0][i] = m1[i];
        }

        //Following rows are rotations of m1
        for(int i = 1 ; i < size; i++){
            matrix[i] = rotate_right(m1, i);
        }
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
                sb.append(matrix[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}