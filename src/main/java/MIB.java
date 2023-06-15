package main.java;

import java.util.Map;
import java.util.HashMap;

public class MIB {
    //System group
    private int s_restart_date;
    private int s_restart_time;
    public int s_key_size;
    public int s_interval_update;
    public int s_max_number_of_keys;
    public int s_keys_ttl;

    //Config group
    public byte[] c_master_key;
    public int c_first_char_of_keys_alphabet;
    public int c_cardinality_of_keys_alphabet;

    //Keys group
    private int d_number_of_valid_keys;
    private Map<Integer, KeyEntry> d_table_generated_keys;

    public MIB(int s_restart_date,
     int s_restart_time,
      int s_key_size,
      int s_interval_update,
      int s_max_number_of_keys,
      int s_keys_ttl,
      byte[] c_master_key,
      int c_first_char_of_keys_alphabet,
      int c_cardinality_of_keys_alphabet){
        this.s_restart_date = s_restart_date;
        this.s_restart_time = s_restart_time;
        this.s_key_size = s_key_size;
        this.s_interval_update = s_interval_update;
        this.s_max_number_of_keys = s_max_number_of_keys;
        this.s_keys_ttl = s_keys_ttl;

        this.c_master_key = c_master_key;
        this.c_first_char_of_keys_alphabet = c_first_char_of_keys_alphabet;
        this.c_cardinality_of_keys_alphabet = c_cardinality_of_keys_alphabet;

        this.d_number_of_valid_keys = 0;
        this.d_table_generated_keys = new HashMap<Integer, KeyEntry>();
      }
}
