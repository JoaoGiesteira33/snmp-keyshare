package main.java;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/*
 * Autor: João Giesteira
 * 
 * Classe que representa uma entrada da tabela de chaves
 * 
 * Controla o acesso a valores da chave, de acordo com a sua visibilidade e quem faz o pedido
 */
public class KeyEntry {
    private int key_id;
    private byte[] key_value;
    private String key_requester;
    private int key_expiration_date;
    private int key_expiration_time;
    public int key_visibility;

    public KeyEntry(int key_id, byte[] key_value, String key_requester,
     int key_expiration_date, int key_expiration_time, int key_visibility){
        
        this.key_id = key_id;
        this.key_value = key_value;
        this.key_requester = key_requester;
        this.key_expiration_date = key_expiration_date;
        this.key_expiration_time = key_expiration_time;
        this.key_visibility = key_visibility;
    }

    public KeyEntry(int key_id, byte[] key_value, String key_requester,
    int ttl_seconds, int key_visibility){
        this.key_id = key_id;
        this.key_value = key_value;
        this.key_requester = key_requester;
        this.key_visibility = key_visibility;

        calculate_expiration_date(ttl_seconds);
    }

    public String get_key_requester(){
        return this.key_requester;
    }

    public int get_key_visibility(){
        return this.key_visibility;
    }

    public String get_value_from_iid(String iid){
        switch(iid){
            case "keyId":
                return Integer.toString(this.key_id);
            case "keyValue":
                return new String(this.key_value);
            case "keyRequester":
                return this.key_requester;
            case "keyExpirationDate":
                return Integer.toString(this.key_expiration_date);
            case "keyExpirationTime":
                return Integer.toString(this.key_expiration_time);
            case "keyVisibility":
                return Integer.toString(this.key_visibility);
        }

        return null;
    }

    public String set_value(String column, String value, String pdu_sender){
        switch(column){
            case "keyId":
                return "Read-only";
            case "keyValue":
                return "Read-only";
            case "keyRequester":
                return "Read-only";
            case "keyExpirationDate":
                return "Read-only";
            case "keyExpirationTime":
                return "Read-only";
            case "keyVisibility":
                try{
                    this.key_visibility = Integer.parseInt(value);
                    if(this.key_requester.equals(pdu_sender)){
                        return value;
                    }else{
                        return "Unauthorized";
                    }
                }catch(NumberFormatException e){
                    return "Wrong type";
                }
        }
        
        return "Unkown behavior";
    }

    // Calcula os campos de data e hora de expiração da chave
    // Baseando-se no TTL na MIB
    private void calculate_expiration_date(int ttl_seconds){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration_date = now.plusSeconds(ttl_seconds);

        LocalDate ld_expiration_date = expiration_date.toLocalDate();
        LocalTime lt_expiration_time = expiration_date.toLocalTime();

        this.key_expiration_date = MIB.get_date(ld_expiration_date);
        this.key_expiration_time = MIB.get_time(lt_expiration_time);
    }

    public boolean is_expired(){
        int now_date = MIB.get_current_date();
        int now_time = MIB.get_current_time();

        if(this.key_expiration_date < now_date){
            return true;
        }else if(this.key_expiration_date == now_date){
            if(this.key_expiration_time < now_time){
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(this.key_id); sb.append(")");
        sb.append(" "); sb.append(new String(this.key_value));
        sb.append(" | (C) "); sb.append(this.key_requester);
        sb.append(" | (D) "); sb.append(this.key_expiration_date);
        sb.append(" | (T) "); sb.append(this.key_expiration_time);
        sb.append(" | (V) "); sb.append(this.key_visibility);
        
        boolean is_expired = this.is_expired();
        if(is_expired){
            sb.append(" | (E) "); sb.append("Expired");
        }else{
            sb.append(" | (E) "); sb.append("Valid");
        }

        return sb.toString();
    }
}
