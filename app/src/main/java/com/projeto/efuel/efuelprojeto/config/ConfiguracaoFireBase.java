package com.projeto.efuel.efuelprojeto.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFireBase {

    private static DatabaseReference database;
    private static FirebaseAuth auth;

    //retorna a instancia do FirebaseDatabase
    public static DatabaseReference getFirebaeDatabase(){
        if(database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }
    //Retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if ( auth == null ){
            auth = FirebaseAuth.getInstance();
        }return auth;
    }
}
