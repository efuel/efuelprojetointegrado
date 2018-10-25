package com.projeto.efuel.efuelprojeto.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.projeto.efuel.efuelprojeto.activity.MapsActivity;
import com.projeto.efuel.efuelprojeto.activity.RequisicoesActivity;
import com.projeto.efuel.efuelprojeto.config.ConfiguracaoFireBase;
import com.projeto.efuel.efuelprojeto.model.Usuario;

public class UsuarioFirebase {
    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFireBase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }
    public static boolean atualizarNomeUsuario(String nome){
        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName( nome)
                    .build();
            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.");
                    }

                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    public static void redirecionaUsuarioLogado(final Activity activity){
             FirebaseUser user = getUsuarioAtual();
        if(user != null){
            DatabaseReference usuariosRef = ConfiguracaoFireBase.getFirebaeDatabase()
                    .child("usuarios")
                    .child(getIdentificadorUsuario());
            usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                    String tipoUsuario = usuario.getTipo();
                    if (tipoUsuario.equals("F")) {
                        activity.startActivity(new Intent(activity, RequisicoesActivity.class));

                    } else {
                        activity.startActivity(new Intent(activity, MapsActivity.class));

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public static String getIdentificadorUsuario(){
        return getUsuarioAtual().getUid();
    }
}
