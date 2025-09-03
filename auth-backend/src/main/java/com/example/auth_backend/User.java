package com.example.auth_backend;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "users") // Mongo collection
public class User {

    @Id // Mongo ID
    private String Id;
    private String name;
    private String email;
    private String password;

    public String getId(){
        return Id;
    }

    public void setId(String Id){
        this.Id = Id;
    }
    
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
    
    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
