package main.java;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalTime;

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

    public MIB(int s_key_size,
      int s_interval_update,
      int s_max_number_of_keys,
      int s_keys_ttl,
      byte[] c_master_key,
      int c_first_char_of_keys_alphabet,
      int c_cardinality_of_keys_alphabet){
        this.s_restart_date = MIB.get_current_date();
        this.s_restart_time = MIB.get_current_time();
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

      public int get_s_interval_update(){
        return this.s_interval_update;
      }

      public static int get_current_date(){
        LocalDate current_date = LocalDate.now();

        int year =  current_date.getYear();
        int month = current_date.getMonthValue();
        int day = current_date.getDayOfMonth();

        return year * 10000 + month * 100 + day;
      }

      public static int get_current_time(){
        LocalTime current_time = LocalTime.now();

        int hour = current_time.getHour();
        int minute = current_time.getMinute();
        int second = current_time.getSecond();

        return hour * 10000 + minute * 100 + second;
      }
}
