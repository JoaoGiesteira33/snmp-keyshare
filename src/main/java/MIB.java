package main.java;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalTime;

public class MIB {
    //System group
    private int s_restart_date;
    private int s_restart_time;
    public int s_key_size; //Also size of the matrix
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

      public int get_s_key_size(){
        return this.s_key_size;
      }

      public byte[] get_c_master_key(){
        return this.c_master_key;
      }

      public static int get_current_date(){
        LocalDate current_date = LocalDate.now();

        int year =  current_date.getYear();
        int month = current_date.getMonthValue();
        int day = current_date.getDayOfMonth();

        return year * 10000 + month * 100 + day;
      }

      public static int get_date(LocalDate date){
        int year =  date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        return year * 10000 + month * 100 + day;
      }

      public static int get_current_time(){
        LocalTime current_time = LocalTime.now();

        int hour = current_time.getHour();
        int minute = current_time.getMinute();
        int second = current_time.getSecond();

        return hour * 10000 + minute * 100 + second;
      }

      public static int get_time(LocalTime time){
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();

        return hour * 10000 + minute * 100 + second;
      }

      public String get_iid_value(String iid){
        switch(iid){
          case "system.1.0":
            return Integer.toString(this.s_restart_date);
          case "system.2.0":
            return Integer.toString(this.s_restart_time);
          case "system.3.0":
            return Integer.toString(this.s_key_size);
          case "system.4.0":
            return Integer.toString(this.s_interval_update);
          case "system.5.0":
            return Integer.toString(this.s_max_number_of_keys);
          case "system.6.0":
            return Integer.toString(this.s_keys_ttl);
          case "config.1.0":
            return new String(this.c_master_key);
          case "config.2.0":
            return Integer.toString(this.c_first_char_of_keys_alphabet);
          case "config.3.0":
            return Integer.toString(this.c_cardinality_of_keys_alphabet);
          case "data.1.0":
            return Integer.toString(this.d_number_of_valid_keys);
          default:
            break;
        }

        String[] iid_split = iid.split("\\.");
        if(iid_split.length == 2){
          String column = iid_split[0];
          int row = Integer.parseInt(iid_split[1]);

          KeyEntry key_entry = this.d_table_generated_keys.get(row);
          if(key_entry != null){
            return key_entry.get_value_from_iid(column);
          }
        }

        return null;
      }

      public String set_iid_value(String iid, String value){
        switch(iid){
          case "system.1.0":
            return "Read-only";

          case "system.2.0":
            return "Read-only";

          case "system.3.0":
            try{
              int temp_key_size = Integer.parseInt(value);
              if(temp_key_size * 2 <= this.c_master_key.length){
                this.s_key_size = temp_key_size;
                return value;
              }else{
                return "Key size too big";
              }
            }catch(NumberFormatException e){
              return "Wrong type";
            }

          case "system.4.0":
            this.s_interval_update = Integer.parseInt(value);
            return value;
          case "system.5.0":
            try{
              this.s_max_number_of_keys = Integer.parseInt(value);
              return value;
            }catch(NumberFormatException e){
              return "Wrong type";
            }

          case "system.6.0":
            try{
              this.s_keys_ttl = Integer.parseInt(value);
              return value;
            }catch(NumberFormatException e){
              return "Wrong type";
            }

          case "config.1.0":
            if(value.getBytes().length >= this.s_key_size * 2){
              this.c_master_key = value.getBytes();
              return value;
            }else{
              return "Key too short";
            }
          case "config.2.0":
            try{
              this.c_first_char_of_keys_alphabet = Integer.parseInt(value);
              return value;
            }catch(NumberFormatException e){
              return "Wrong type";
            }

          case "config.3.0":
            this.c_cardinality_of_keys_alphabet = Integer.parseInt(value);
            return value;

          case "data.1.0":
            return "Read-only";

          default:
            break;
        }

        String[] iid_split = iid.split("\\.");
        if(iid_split.length == 2){
          String column = iid_split[0];
          int row = Integer.parseInt(iid_split[1]);

          KeyEntry key_entry = this.d_table_generated_keys.get(row);
          if(key_entry != null){
            return key_entry.set_value(column, value);
          }else{
            return "Non-existent key";
          }
        }

        return "Unknown behavior";
      }

      public int save_key(byte[] key){
        

        return 0;
      }
}
