package main.java;

class Main{
    public static void main(String[] args){
        byte[] key = new byte[]{1,2,3,4,5,6,7,8};

        Matrix a = new Matrix(4, key, 0);
        Matrix b = new Matrix(4, key, 1);
        Matrix s = new Matrix(4, 650487);
        Matrix xored = new Matrix(4, a.getMatrix(), b.getMatrix(), s.getMatrix());
        
        System.out.println(a);
        System.out.println(b);
        System.out.println(s);
        System.out.println(xored);

        xored.update_matrix();
        System.out.println(xored);

        byte[] generated_key = xored.generate_key(4);
        for(int i = 0; i < generated_key.length; i++){
            String to_print = String.format("0x%02X ", generated_key[i]);
            System.out.print(to_print);
        }
    }
}