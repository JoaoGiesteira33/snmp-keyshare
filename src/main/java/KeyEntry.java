package main.java;

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

    public String set_value(String column, String value){
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
                    return value;
                }catch(NumberFormatException e){
                    return "Wrong type";
                }
        }
        
        return "Unkown behavior";
    }
}
