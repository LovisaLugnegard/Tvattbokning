package com.isisochbast.tvattbokning.Model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 Klass för att skapa användare, användare loggar in med mail, har ett lösenord
 Senare: hus/vilken tvättstuga de tillhör
 */
public class User extends GenericJson {

    @Key("_id")
    private String id;
    @Key
    private String email;
    @Key
    private String password;

    public String getEmail(){
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(){}


}
