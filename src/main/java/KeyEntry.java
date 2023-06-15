package main.java;

public class KeyEntry {
    private byte[] key_value;
    private String key_requester;
    private int key_expiration_date;
    private int key_expiration_time;
    public int key_visibility;

    public KeyEntry(byte[] key_value, String key_requester, int key_expiration_date, int key_expiration_time, int key_visibility){
        this.key_value = key_value;
        this.key_requester = key_requester;
        this.key_expiration_date = key_expiration_date;
        this.key_expiration_time = key_expiration_time;
        this.key_visibility = key_visibility;
    }
}
